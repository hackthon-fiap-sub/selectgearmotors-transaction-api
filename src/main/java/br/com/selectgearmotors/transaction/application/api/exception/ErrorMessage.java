package br.com.selectgearmotors.transaction.application.api.exception;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode //(callSuper = true)
//@ToString(callSuper = true, of = { "name" })
@ToString
@Builder
@Data
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
}
