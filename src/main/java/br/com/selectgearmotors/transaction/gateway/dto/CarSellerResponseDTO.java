package br.com.selectgearmotors.transaction.gateway.dto;

import lombok.Data;

@Data
public class CarSellerResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String mobile;
    private String socialId;
    private String socialIdDispatchDate;
    private String documentId;
    private String documentDistrict;
    private String documentDispatchDate;
    private String birthDate;
    private String companyId;
    private String mediaId;
}