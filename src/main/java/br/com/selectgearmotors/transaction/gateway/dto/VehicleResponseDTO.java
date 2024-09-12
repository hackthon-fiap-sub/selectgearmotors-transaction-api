package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleResponseDTO {
    private Long id;
    private String code;
    private String cor;
    private String mediaId;
    private String vehicleYear;
    private String description;
    private BigDecimal price;
    private Long vehicleCategoryId;
    private String vehicleCategoryName;
    private Long modelId;
    private String modelName;
    private Long brandId;
    private String brandName;
    private String vehicleStatus;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private String location;
    private String plate;
    private String chassis;
    private String renavam;
}