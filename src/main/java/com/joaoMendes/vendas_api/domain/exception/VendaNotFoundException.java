package com.joaoMendes.vendas_api.domain.exception;

public class VendaNotFoundException extends RuntimeException {

    public VendaNotFoundException(Long id) {
        super("Vendedor não encontrado com ID: " + id);
    }
}
