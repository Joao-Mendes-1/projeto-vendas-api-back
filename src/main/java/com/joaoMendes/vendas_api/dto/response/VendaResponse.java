package com.joaoMendes.vendas_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendaResponse(Long id,
                            @JsonFormat(pattern = "dd/MM/yyyy")
                            LocalDate dataVenda,
                            BigDecimal valor,
                            Long idVendedor,
                            String nomeVendedor) {
}
