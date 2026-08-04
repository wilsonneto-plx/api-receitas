package com.wilson.api_receitas.dto;

import com.wilson.api_receitas.model.Receita;

import java.util.List;

public record ReceitaResponseDTO(

        Long id,
        String nome,
        String categoria,
        String origem,
        List<String> instrucoes,
        String imagemUrl,
        List<String> ingredientes

) {

    public ReceitaResponseDTO(Receita receita) {
        this (  receita.getId(),
                receita.getNomeTraduzido(),
                receita.getCategoria(),
                receita.getOrigem(),

                receita.getInstrucoes() != null ? List.of(receita.getInstrucoes().split("\n+"))
                        : List.of(),

                receita.getImagemUrl(),
                receita.getIngredientes()

        );
    }
}
