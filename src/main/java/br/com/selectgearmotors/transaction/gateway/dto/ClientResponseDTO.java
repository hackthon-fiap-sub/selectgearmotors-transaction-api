package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

@Data
public class ClientResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String email;
    private String socialId;
    private String mobile;
}