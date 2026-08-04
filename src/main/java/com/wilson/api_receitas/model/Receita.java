package com.wilson.api_receitas.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_receitas")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String nomeOriginal;

    @Column(nullable = false)
    private String nomeTraduzido;

    private String categoria;

    private String origem;

    @Column(columnDefinition = "TEXT")
    private String instrucoes;

    @Column(length = 500)
    private String imagemUrl;

    @ElementCollection
    @CollectionTable(name = "tb_receitas_ingredientes", joinColumns = @JoinColumn(name = "receita_id"))
    @Column(name = "ingrediente", columnDefinition = "TEXT")
    private List<String> ingredientes;

    public Receita(String externalId, String nomeOriginal, String nomeTraduzido, String categoria, String origem,
                   String instrucoes, String imagemUrl, List<String> ingredientes) {

        this.externalId = externalId;
        this.nomeOriginal = nomeOriginal;
        this.nomeTraduzido = nomeTraduzido;
        this.categoria = categoria;
        this.origem = origem;

        if (instrucoes!= null) {
            this.instrucoes = instrucoes.replace("\\n", "\n");

        }
        this.imagemUrl = imagemUrl;
        this.ingredientes = ingredientes;
    }

    public void atualizar(String nomeTraduzido, String categoria) {
        if(nomeTraduzido != null) { this.nomeTraduzido = nomeTraduzido;}
        if(categoria !=null) { this.categoria = categoria;}
    }
}
