package com.joaoMendes.vendas_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PeriodoEstatisticaResponse(Long idVendedor,
                                         String nomeVendedor,
                                         BigDecimal totalVendido,
                                         BigDecimal mediaDiaria,
                                         LocalDate dataInicio,
                                         LocalDate dataFim) {
}