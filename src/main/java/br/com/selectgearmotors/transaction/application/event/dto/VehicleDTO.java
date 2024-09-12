package br.com.selectgearmotors.transaction.application.event.dto;

import java.math.BigDecimal;

public record VehicleDTO(Long id,
                         String code,
                         String cor,
                         int vehicleYear,
                         String description,
                         BigDecimal price,
                         String vehicleCategoryName,
                         String modelName,
                         String brandName,
                         String vehicleStatus,
                         String location,
                         String plate,
                         String chassis,
                         String renavam) {}
