package com.joaoMendes.vendas_api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VendaRequest {

    @PastOrPresent(message = "A data não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data é obrigatória")
    private LocalDate dataVenda;

    @NotNull(message = "O valor da venda é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "O id do vendedor é obrigatório.")
    private Long idVendedor;
}
