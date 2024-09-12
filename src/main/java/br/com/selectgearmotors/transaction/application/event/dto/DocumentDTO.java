package br.com.selectgearmotors.transaction.application.event.dto;

public record DocumentDTO(String transactionId,
                          String clientId,
                          String clientName,
                          String seller,
                          VehicleDTO vehicle) {
}
