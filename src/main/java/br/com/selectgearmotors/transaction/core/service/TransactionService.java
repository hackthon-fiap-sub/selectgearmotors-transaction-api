package br.com.selectgearmotors.transaction.core.service;

import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.application.event.DocumentSenderAdapter;
import br.com.selectgearmotors.transaction.application.event.NotificationSenderAdapter;
import br.com.selectgearmotors.transaction.application.event.dto.DocumentDTO;
import br.com.selectgearmotors.transaction.application.event.dto.NotificationDTO;
import br.com.selectgearmotors.transaction.application.event.dto.VehicleDTO;
import br.com.selectgearmotors.transaction.commons.util.PhoneNumberFormatter;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.in.transaction.*;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionRepositoryPort;
import br.com.selectgearmotors.transaction.gateway.client.ClientWebClient;
import br.com.selectgearmotors.transaction.gateway.company.CarSellerWebClient;
import br.com.selectgearmotors.transaction.gateway.dto.CarSellerResponseDTO;
import br.com.selectgearmotors.transaction.gateway.dto.ClientResponseDTO;
import br.com.selectgearmotors.transaction.gateway.dto.VehicleResponseDTO;
import br.com.selectgearmotors.transaction.gateway.vehicle.VehicleWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TransactionService implements CreateTransactionPort, UpdateTransactionPort, FindByIdTransactionPort, FindTransactionsPort, DeleteTransactionPort {

    private final TransactionRepositoryPort transactionRepository;
    private final NotificationSenderAdapter notificationSenderAdapter;
    private final DocumentSenderAdapter documentSenderAdapter;
    private final ClientWebClient clientWebClient;
    private final VehicleWebClient vehicleWebClient;
    private final CarSellerWebClient carSellerWebClient;

    @Autowired
    public TransactionService(TransactionRepositoryPort transactionRepository, NotificationSenderAdapter notificationSenderAdapter, DocumentSenderAdapter documentSenderAdapter, ClientWebClient clientWebClient, VehicleWebClient vehicleWebClient, CarSellerWebClient carSellerWebClient) {
        this.transactionRepository = transactionRepository;
        this.notificationSenderAdapter = notificationSenderAdapter;
        this.documentSenderAdapter = documentSenderAdapter;
        this.clientWebClient = clientWebClient;
        this.vehicleWebClient = vehicleWebClient;
        this.carSellerWebClient = carSellerWebClient;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, transaction);

            return transactionRepository.save(resultById);
        }

        return null;
    }

    @Override
    public Transaction updateStatus(String transactionId, String status) {
        Transaction resultById = findByCode(transactionId);
        if (resultById != null) {
            resultById.setTransactionStatus(status);
            resultById.update(resultById.getId(), resultById);

            Transaction transactionSaved = transactionRepository.save(resultById);
            //TODO: Se o status for confirmado para pagamenento, enviar nas filas SQS de gerar documentação e enviar sms

            String clientCode = resultById.getClientCode();
            String vehicleCode = resultById.getVehicleCode();
            String carSellerCode = resultById.getCarSellerCode();

            String transactionCode = transactionSaved.getCode();
            sendNotification(clientCode, vehicleCode);
            sendDocument(transactionCode, carSellerCode, clientCode, vehicleCode);

            return transactionSaved;
        }

        return null;
    }

    private void sendDocument(String transactionCode, String sellerCode, String clientCode, String vehicleCode) {
        ClientResponseDTO clientResponseDTO = getClientData(clientCode);
        VehicleResponseDTO vehicleData = getVehicleData(vehicleCode);
        CarSellerResponseDTO carSellerResponseDTO = getSellerData(sellerCode);

        VehicleDTO vehicleDTO = getVehicleDTO(vehicleData);
        String sellerName = carSellerResponseDTO.getName();

        DocumentDTO document = getDocumentDTO(transactionCode, sellerName, clientResponseDTO, vehicleDTO);
        documentSenderAdapter.sendMessage(document);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction findByCode(String code) {
        return transactionRepository.findByCode(code);
    }

    @Override
    public Transaction findByVehicleCode(String vehicleCode) {
        return transactionRepository.findByVehicleCode(vehicleCode);
    }

    @Override
    public List<Transaction> findAll() {
        try {
            return transactionRepository.findAll();
        } catch (Exception e) {
            log.error("Erro ao buscar produtos: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public boolean remove(Long id) {
        try {
            Transaction transactionById = findById(id);
            if (transactionById == null) {
                throw new ResourceFoundException("Transaction not found");
            }

            transactionRepository.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover produto: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }

    private DocumentDTO getDocumentDTO(String transactionCode, String sallerName, ClientResponseDTO clientResponseDTO, VehicleDTO vehicleDTO) {
        return new DocumentDTO(
                transactionCode,
                clientResponseDTO.getCode(),
                clientResponseDTO.getName(),
                sallerName,
                vehicleDTO
        );
    }

    private VehicleDTO getVehicleDTO(VehicleResponseDTO vehicleData) {
        return new VehicleDTO(
                vehicleData.getId(),
                vehicleData.getCode(),
                vehicleData.getCor(),
                Integer.parseInt(vehicleData.getVehicleYear()),
                vehicleData.getDescription(),
                vehicleData.getPrice(),
                vehicleData.getVehicleCategoryName(),
                vehicleData.getModelName(),
                vehicleData.getBrandName(),
                vehicleData.getVehicleStatus(),
                vehicleData.getLocation(),
                vehicleData.getPlate(),
                vehicleData.getChassis(),
                vehicleData.getRenavam()
        );
    }

    private void sendNotification(String clientCode, String vehicleCode) {
        ClientResponseDTO clientResponseDTO = getClientData(clientCode);
        VehicleResponseDTO vehicleData = getVehicleData(vehicleCode);

        String clientMobile = PhoneNumberFormatter.formatPhoneNumber(clientResponseDTO.getMobile());
        String message = "Compra realizada com sucesso! Veiculo: " + vehicleData.getCode() + " - Modelo: - " + vehicleData.getModelName() + " Cor: " + vehicleData.getCor() + " Ano - " + vehicleData.getVehicleYear() + " Preço: " + vehicleData.getPrice();

        NotificationDTO notification = new NotificationDTO(
                clientMobile,
                message
        );
        notificationSenderAdapter.sendMessage(notification);
    }

    private ClientResponseDTO getClientData(String clientCode) {
        return clientWebClient.get(clientCode);
    }

    private VehicleResponseDTO getVehicleData(String vehicleCode) {
        return vehicleWebClient.get(vehicleCode);
    }

    private CarSellerResponseDTO getSellerData(String sellerCode) {
        return carSellerWebClient.get(sellerCode);
    }
}
