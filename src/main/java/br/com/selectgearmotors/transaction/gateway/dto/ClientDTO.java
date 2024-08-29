package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String documentId;
    private String email;
    private String phone;
    private String status;
}
