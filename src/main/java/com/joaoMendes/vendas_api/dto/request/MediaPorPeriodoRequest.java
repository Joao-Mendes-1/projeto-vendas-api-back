package com.joaoMendes.vendas_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaPorPeriodoRequest {

    @NotNull(message = "A data de inicio é obrigatórioa")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatórioa")
    private LocalDate dataFim;
}
