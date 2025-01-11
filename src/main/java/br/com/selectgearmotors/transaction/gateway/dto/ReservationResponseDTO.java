package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

@Data
public class ReservationResponseDTO {
    private Long id;
    private String vehicleId;
    private String buyerId;
    private String statusReservation;
}