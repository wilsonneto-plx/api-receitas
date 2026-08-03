package com.wilson.api_receitas.dto;

import jakarta.validation.constraints.NotBlank;

public record ReceitaRequestDTO(

        @NotBlank(message = "O nome da receita é obrigatório e não pode ficar em branco.")
        String nomeTraduzido,

        @NotBlank(message = "A categoria é obrigatória e não pode ficar em branco.")
        String categoria
) {
}
