package com.joaoMendes.vendas_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VendaRequest {

    private LocalDateTime dataVenda;

    @NotNull(message = "O valor da venda é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "O id do vendedor é obrigatório.")
    private Long idVendedor;
}
