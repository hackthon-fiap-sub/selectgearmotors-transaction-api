package br.com.selectgearmotors.transaction.application.api.exception;

import br.com.selectgearmotors.transaction.application.api.dto.response.GenericErrorResponse;
import br.com.selectgearmotors.transaction.commons.exception.CPFFoundException;
import br.com.selectgearmotors.transaction.commons.exception.CNPJFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class GenericResourcesAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GenericErrorResponse> handleException(Exception ex) {
        String errorMessage = "Erro ao processar a requisição. Detalhes: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericErrorResponse(errorMessage));
    }

    @ExceptionHandler(CNPJFoundException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleCnpjFoundException(final CNPJFoundException ex) { //AlreadyExistsException
        ErrorMessage errorMessage = createErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CPFFoundException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleCpfFoundException(final CPFFoundException ex) { //AlreadyExistsException
        ErrorMessage errorMessage = createErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorMessage errorMessage = createErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolationException(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage().concat(";"));
        }

        ErrorMessage errorMessage = createErrorMessage(message.toString());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {EmptyResultDataAccessException.class, EntityNotFoundException.class})
    public void handleNotFound() {
    }

    private ErrorMessage createErrorMessage(String exceptionMessage) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exceptionMessage);
    }
}
