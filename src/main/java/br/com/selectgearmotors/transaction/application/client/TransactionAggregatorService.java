package br.com.selectgearmotors.transaction.application.client;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionCreateRequest;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionPaymentResponse;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionResponse;
import br.com.selectgearmotors.transaction.application.api.mapper.TransactionApiMapper;
import br.com.selectgearmotors.transaction.application.client.dto.TransactionDTO;
import br.com.selectgearmotors.transaction.application.database.repository.TransactionRepositoryAdapter;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.in.transaction.FindByIdTransactionPort;
import br.com.selectgearmotors.transaction.gateway.client.ClientWebClient;
import br.com.selectgearmotors.transaction.gateway.company.CarSellerWebClient;
import br.com.selectgearmotors.transaction.gateway.dto.*;
import br.com.selectgearmotors.transaction.gateway.payment.PaymentWebClient;
import br.com.selectgearmotors.transaction.gateway.reservation.ReservationWebClient;
import br.com.selectgearmotors.transaction.gateway.vehicle.VehicleWebClient;
import br.com.selectgearmotors.transaction.infrastructure.entity.domain.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TransactionAggregatorService {

    private final TransactionRepositoryAdapter transactionRepositoryAdapter;
    private final PaymentWebClient paymentWebClient;
    private final ClientWebClient clientWebClient;
    private final VehicleWebClient vehicleWebClient;
    private final CarSellerWebClient carSellerWebClient;
    private final FindByIdTransactionPort findByIdTransactionPort;
    private final TransactionApiMapper transactionApiMapper;
    private final ReservationWebClient reservationWebClient;

    public TransactionAggregatorService(TransactionRepositoryAdapter transactionRepositoryAdapter, PaymentWebClient paymentWebClient, ClientWebClient clientWebClient, VehicleWebClient vehicleWebClient, CarSellerWebClient carSellerWebClient, FindByIdTransactionPort findByIdTransactionPort, TransactionApiMapper transactionApiMapper, ReservationWebClient reservationWebClient) {
        this.transactionRepositoryAdapter = transactionRepositoryAdapter;
        this.paymentWebClient = paymentWebClient;
        this.clientWebClient = clientWebClient;
        this.vehicleWebClient = vehicleWebClient;
        this.carSellerWebClient = carSellerWebClient;
        this.findByIdTransactionPort = findByIdTransactionPort;
        this.transactionApiMapper = transactionApiMapper;
        this.reservationWebClient = reservationWebClient;
    }

    public TransactionPaymentResponse createTransaction(TransactionCreateRequest request) {
        try {
            Transaction byVehicleCode = findByIdTransactionPort.findByVehicleCode(request.getVehicleCode());
            if (byVehicleCode != null) {
                throw new IllegalStateException("Veículo já reservado para compra");
            }

            if (request.getVehicleCode() == null) {
                throw new IllegalStateException("Tipo de pessoa não informado");
            }

            if (request.getClientCode() == null) {
                throw new IllegalStateException("Cliente não informado");
            }

            if (request.getCarSellerCode() == null) {
                throw new IllegalStateException("Vendedor não informado");
            }

            ReservationResponseDTO reservationStatus = reservationWebClient.getStatus(request.getVehicleCode());
            if (reservationStatus != null && reservationStatus.getStatusReservation().equals("SOLD")) {
                throw new IllegalStateException("Veículo já vendido");
            }

            if (reservationStatus != null && !reservationStatus.getBuyerId().equals(request.getClientCode())) {
                throw new IllegalStateException("Veículo já reservado para outro cliente!");
            }

            // Buscar dados do veículo
            VehicleResponseDTO vehicleResponseDTO = getVehicleDTO(request.getVehicleCode());
            ClientResponseDTO clientResponseDTO = getClientDTO(request.getClientCode());
            CarSellerResponseDTO carSellerDTO = getCarSellerDTO(request.getCarSellerCode());

            // Criar transação Inicial
            Transaction transaction = new Transaction();
            transaction.setVehicleCode(vehicleResponseDTO.getCode());
            transaction.setClientCode(clientResponseDTO.getCode());
            transaction.setCarSellerCode(carSellerDTO.getCode());
            transaction.setPrice(request.getPrice());
            transaction.setTransactionStatus(TransactionStatus.RESERVED.name());
            transaction.setTransactionTypeId(request.getTransactionTypeId());
            transaction.setCode(UUID.randomUUID().toString());
            transaction.setPersonType(request.getPersonType());

            Transaction transactionSaved = transactionRepositoryAdapter.save(transaction);
            transactionSaved.setPersonType(request.getPersonType());
            PaymentResponseDto paymentResponseDto = createPayment(transactionSaved);
            if (paymentResponseDto != null) {
                transactionSaved.setTransactionStatus(paymentResponseDto.getTransactionStatus());

                String vehicleCode = transactionSaved.getVehicleCode();
                ReservationResponseDTO status = reservationWebClient.getStatus(vehicleCode);
                reservationWebClient.setStatus(status.getId());

                vehicleWebClient.setStatus(vehicleCode);
            }
            TransactionResponse transactionResponse = transactionApiMapper.fromEntity(transactionSaved);

            return TransactionPaymentResponse.builder()
                    .transactionData(transactionResponse)
                    .paymentData(paymentResponseDto)
                    .build();
        } catch (Exception e) {
            log.info("Erro ao salvar transação: " + e.getMessage());
            throw new IllegalStateException("Erro ao criar transação");
        }
    }

    private PaymentResponseDto createPayment(Transaction transaction) {
        // Preparar o payload para o serviço de pagamento
        PaymentDto paymentDto = PaymentDto.builder()
                .transactionId(transaction.getCode())
                .clientId(transaction.getClientCode())
                .transactionAmount(transaction.getPrice())
                .personType(transaction.getPersonType())
                .build();

        // Chamar a API de pagamentos
        try {
            PaymentResponseDto paymentResponseDto = paymentWebClient.setPayment(paymentDto);
            if (paymentResponseDto == null) {
                throw new IllegalStateException("Erro ao chamar o serviço de pagamento");
            }
            // Atualizar o status da transação
            String status = paymentResponseDto.getStatus();
            TransactionStatus transactionStatus = null;
            switch (status) {
                case "pending" -> transactionStatus = TransactionStatus.RESERVED;
                case "approved" -> transactionStatus = TransactionStatus.CONFIRMED;
                case "rejected" -> transactionStatus = TransactionStatus.CANCELED;
                default -> throw new IllegalStateException("Status de pagamento inválido: " + status);
            }

            paymentResponseDto.setTransactionStatus(transaction.getTransactionStatus());
            return paymentResponseDto;
        } catch (Exception e) {
            // Log de erro e tratamento de exceção
            log.error("Erro ao chamar o serviço de pagamento: " + e.getMessage());
        }

        return null;
    }

    public TransactionDTO getTransaction(Long transactionId) {
        Transaction transaction = transactionRepositoryAdapter.findById(transactionId);
        if (transaction == null) {
            throw new IllegalStateException("Transação não encontrada");
        }

        VehicleResponseDTO vehicleResponseDTO = getVehicleDTO(transaction.getVehicleCode());
        ClientResponseDTO clientResponseDTO = getClientDTO(transaction.getClientCode());
        CarSellerResponseDTO carSellerDTO = getCarSellerDTO(transaction.getCarSellerCode());

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id(transaction.getId())
                .vehicleId(vehicleResponseDTO.getId())
                .clientId(clientResponseDTO.getId())
                .carSellerId(carSellerDTO.getId())
                .price(transaction.getPrice())
                .transactionStatus(transaction.getTransactionStatus())
                .transactionTypeId(transaction.getTransactionTypeId())
                .build();

        log.info("Transação encontrada: " + transactionDTO);
        return transactionDTO;
    }

    private VehicleResponseDTO getVehicleDTO(String vehicleCode) {
        VehicleResponseDTO vehicleResponseDTO = vehicleWebClient.get(vehicleCode);
        if (vehicleResponseDTO == null) {
            throw new IllegalStateException("Veículo não disponível para compra");
        }
        return vehicleResponseDTO;
    }

    private ClientResponseDTO getClientDTO(String clientCode) {
        ClientResponseDTO clientResponseDTO = clientWebClient.get(clientCode);
        if (clientResponseDTO == null) {
            throw new IllegalStateException("Comprador não encontrado");
        }
        return clientResponseDTO;
    }

    private CarSellerResponseDTO getCarSellerDTO(String carSellerCode) {
        CarSellerResponseDTO carSellerResponseDTO = carSellerWebClient.get(carSellerCode);
        if (carSellerResponseDTO == null) {
            throw new IllegalStateException("Vendedor não encontrado");
        }
        return carSellerResponseDTO;
    }

}
