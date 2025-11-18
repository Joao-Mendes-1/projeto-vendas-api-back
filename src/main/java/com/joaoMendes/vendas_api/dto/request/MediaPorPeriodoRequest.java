package com.joaoMendes.vendas_api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaPorPeriodoRequest {
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data de inicio é obrigatórioa")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data de fim é obrigatórioa")
    private LocalDate dataFim;
}
