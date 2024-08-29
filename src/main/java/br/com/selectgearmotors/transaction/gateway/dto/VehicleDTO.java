package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Long id;
    private String brand;
    private String model;
    private int year;
    private String cor;
    private BigDecimal price;
    private String status;
}
