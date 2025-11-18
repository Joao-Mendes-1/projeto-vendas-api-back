package com.joaoMendes.vendas_api.domain.exception;

public class VendedorNotFoundException extends RuntimeException {

    public VendedorNotFoundException(Long id) {
        super("Vendedor não encontrado com ID: " + id);
    }
    public VendedorNotFoundException(String message) {
        super(message);
    }
}
