package com.joaoMendes.vendas_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VendedorRequest {

    @NotBlank(message = "O nome do vendedor é obrigatório.")
    @Size(max = 60, message = "O nome pode ter no máximo 60 caracteres.")
    private String nome;
}
