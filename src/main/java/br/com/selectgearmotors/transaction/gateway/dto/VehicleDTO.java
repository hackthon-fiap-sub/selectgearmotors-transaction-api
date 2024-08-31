package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Long id;
    private String code;
    private String cor;
    private String pic;
    private String vehicleYear;
    private String description;
    private BigDecimal price;
    private String vehicleStatus;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private Long modelId;
    private String modelName;
    private Long brandId;
    private String brandName;
}