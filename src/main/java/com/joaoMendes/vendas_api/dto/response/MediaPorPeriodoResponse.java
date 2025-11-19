package com.joaoMendes.vendas_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MediaPorPeriodoResponse(Long idVendedor,
                                      String nomeVendedor,
                                      BigDecimal totalVendido,
                                      BigDecimal mediaDiaria,
                                      @JsonFormat(pattern = "dd/MM/yyyy")
                                      LocalDate dataInicio,
                                      @JsonFormat(pattern = "dd/MM/yyyy")
                                      LocalDate dataFim,
                                      Long dias) {
}

