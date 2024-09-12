package br.com.selectgearmotors.transaction.application.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationDTO(@JsonProperty("phone_number") String phoneNumber, String message) {
}
