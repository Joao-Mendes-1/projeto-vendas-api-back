package com.joaoMendes.vendas_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaResponse(Long id,
                            LocalDateTime dataVenda,
                            BigDecimal valor,
                            Long idVendedor,
                            String nomeVendedor) {
}
