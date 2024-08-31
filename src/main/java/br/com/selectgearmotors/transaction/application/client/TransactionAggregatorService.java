package br.com.selectgearmotors.transaction.application.client;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionCreateRequest;
import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionRequest;
import br.com.selectgearmotors.transaction.application.client.dto.TransactionDTO;
import br.com.selectgearmotors.transaction.application.database.repository.TransactionRepositoryAdapter;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.in.transaction.FindByIdTransactionPort;
import br.com.selectgearmotors.transaction.gateway.client.ClientWebClient;
import br.com.selectgearmotors.transaction.gateway.dto.ClientDTO;
import br.com.selectgearmotors.transaction.gateway.dto.VehicleDTO;
import br.com.selectgearmotors.transaction.gateway.vehicle.VehicleWebClient;
import br.com.selectgearmotors.transaction.infrastructure.entity.domain.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionAggregatorService {

    private final TransactionRepositoryAdapter transactionRepositoryAdapter;
    private final ClientWebClient clientWebClient;
    private final VehicleWebClient vehicleWebClient;
    private final FindByIdTransactionPort findByIdTransactionPort;

    public TransactionAggregatorService(TransactionRepositoryAdapter transactionRepositoryAdapter, ClientWebClient clientWebClient, VehicleWebClient vehicleWebClient, FindByIdTransactionPort findByIdTransactionPort) {
        this.transactionRepositoryAdapter = transactionRepositoryAdapter;
        this.clientWebClient = clientWebClient;
        this.vehicleWebClient = vehicleWebClient;
        this.findByIdTransactionPort = findByIdTransactionPort;
    }

    public Transaction createTransaction(TransactionCreateRequest request) {
        Transaction byVehicleCode = findByIdTransactionPort.findByVehicleCode(request.getVehicleCode());
        if (byVehicleCode != null) {
            throw new IllegalStateException("Veículo já reservado para compra");
        }
        // Buscar dados do veículo
        VehicleDTO vehicleDTO = getVehicleDTO(request.getVehicleCode());
        ClientDTO clientDTO = getClientDTO(request.getClientCode());

        // Criar transação Inicial
        Transaction transaction = new Transaction();
        transaction.setVehicleCode(vehicleDTO.getCode());
        transaction.setClientCode(clientDTO.getCode());
        transaction.setPrice(vehicleDTO.getPrice());
        transaction.setTransactionStatus(TransactionStatus.RESERVED.name());
        transaction.setTransactionTypeId(request.getTransactionTypeId());
        transaction.setCode(UUID.randomUUID().toString());

        return transactionRepositoryAdapter.save(transaction);
    }

    public TransactionDTO getTransaction(Long transactionId) {
        Transaction transaction = transactionRepositoryAdapter.findById(transactionId);
        if (transaction == null) {
            throw new IllegalStateException("Transação não encontrada");
        }

        VehicleDTO vehicleDTO = getVehicleDTO(transaction.getVehicleCode());
        ClientDTO clientDTO = getClientDTO(transaction.getClientCode());

        TransactionDTO transacaoDTO = TransactionDTO.builder()
                .id(transaction.getId())
                .vehicleId(vehicleDTO.getId())
                .clientId(clientDTO.getId())
                .price(transaction.getPrice())
                .transactionStatus(transaction.getTransactionStatus())
                .transactionTypeId(transaction.getTransactionTypeId())
                .build();

        return transacaoDTO;
    }

    private VehicleDTO getVehicleDTO(String vehicleCode) {
        VehicleDTO vehicleDTO = vehicleWebClient.get(vehicleCode);
        if (vehicleDTO == null) {
            throw new IllegalStateException("Veículo não disponível para compra");
        }
        return vehicleDTO;
    }

    private ClientDTO getClientDTO(String clientCode) {
        ClientDTO clientDTO = clientWebClient.get(clientCode);
        if (clientDTO == null) {
            throw new IllegalStateException("Comprador não encontrado");
        }
        return clientDTO;
    }

}
