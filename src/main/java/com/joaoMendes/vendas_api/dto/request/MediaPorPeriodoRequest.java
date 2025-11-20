package com.joaoMendes.vendas_api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaPorPeriodoRequest {

    @NotNull(message = "A data de inicio é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;


    @PastOrPresent(message = "A data fim não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data de fim é obrigatória")
    private LocalDate dataFim;
}
