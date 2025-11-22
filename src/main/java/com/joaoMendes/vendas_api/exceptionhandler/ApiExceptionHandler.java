package com.joaoMendes.vendas_api.exceptionhandler;

import com.joaoMendes.vendas_api.domain.exception.PeriodoInvalidoException;
import com.joaoMendes.vendas_api.domain.exception.VendaNotFoundException;
import com.joaoMendes.vendas_api.domain.exception.VendedorNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ex.getMessage(),
                List.of("INVALID_ARGUMENT"),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(

            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ApiErrorResponse body = new ApiErrorResponse(
                "Erro de validação",
                errors,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(VendedorNotFoundException.class)
    public ResponseEntity<Object> handleVendedorNotFound(VendedorNotFoundException ex) {

        ApiErrorResponse body = new ApiErrorResponse(
                ex.getMessage(),
                List.of(HttpStatus.NOT_FOUND.name()),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(VendaNotFoundException.class)
    public ResponseEntity<Object> handleVendaNotFound(VendaNotFoundException ex) {

        ApiErrorResponse body = new ApiErrorResponse(
                ex.getMessage(),
                List.of(HttpStatus.NOT_FOUND.name()),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(PeriodoInvalidoException.class)
    public ResponseEntity<Object> handlePeriodoInvalido(PeriodoInvalidoException ex) {

        ApiErrorResponse body = new ApiErrorResponse(
                ex.getMessage(),
                List.of(HttpStatus.BAD_REQUEST.name()),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        ApiErrorResponse body = new ApiErrorResponse(
                "Erro ao salvar dados no banco de dados.",
                List.of("DATA_INTEGRITY_VIOLATION"),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}