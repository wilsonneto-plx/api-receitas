package com.wilson.api_receitas.dto;

import java.util.List;

public record ReceitaResponseDTO(

        Long id,
        String nome,
        String categoria,
        String origem,
        String instrucoes,
        String imagemUrl,
        List<String> ingredientes

) {
}
