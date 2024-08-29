package br.com.selectgearmotors.transaction.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "CNPJ já cadastrado.")
public class CNPJFoundException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Resource found exception.
     *
     * @param message the message
     */
    public CNPJFoundException(final String message) {
        super(message);
    }
}
