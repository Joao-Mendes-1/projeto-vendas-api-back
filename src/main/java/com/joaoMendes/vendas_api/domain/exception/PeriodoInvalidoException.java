package com.joaoMendes.vendas_api.domain.exception;

import java.time.LocalDate;

public class PeriodoInvalidoException extends RuntimeException {

    public PeriodoInvalidoException(LocalDate inicio, LocalDate fim) {
        super("Data fim não pode ser antes da data início.");
    }
}
