package com.joaoMendes.vendas_api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiErrorResponse {

    private String mensagem;
    private List<String> erros;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiErrorResponse(String mensagem, List<String> erros, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.erros = erros;
        this.timestamp = timestamp;
    }
}