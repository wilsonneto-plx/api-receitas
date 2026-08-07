package com.wilson.api_receitas.controller;

import com.wilson.api_receitas.dto.ReceitaRequestDTO;
import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Receitas", description = "Endpoints para busca, tradução (via Gemini) e gerenciamento local de receitas")
public interface ReceitaControllerOpenApi {

    @Operation(summary = "Busca e traduz uma receita pelo nome",
            description = "Consulta no TheMealDB, traduz para o português utilizando IA (Gemini) e salva no banco de dados local (Cache).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita encontrada, traduzida e salva com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma receita encontrada com este nome")
    })
    ResponseEntity<ReceitaResponseDTO> buscarReceita(
            @Parameter(description = "Nome da receita em português", example = "Bolo de chocolate") String nomeReceita);


    @Operation(summary = "Lista todas as receitas salvas",
            description = "Retorna uma lista paginada das receitas que já foram buscadas e estão salvas no banco local.")
    @ApiResponse(responseCode = "200", description = "Lista de receitas retornada com sucesso")
    ResponseEntity<Page<ReceitaResponseDTO>> listarReceitas(Pageable pageable);


    @Operation(summary = "Busca uma receita pelo ID", description = "Retorna os detalhes de uma receita específica salva" +
            " no banco.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada com o ID informado")
    })
    ResponseEntity<ReceitaResponseDTO> buscarPorId(
            @Parameter(description = "ID da receita no banco de dados") Long id);


    @Operation(summary = "Exclui uma receita", description = "Remove permanentemente uma receita do banco de dados local.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Receita excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada com o ID informado")
    })
    ResponseEntity<Void> deletarReceita(
            @Parameter(description = "ID da receita a ser excluída") Long id);


    @Operation(summary = "Atualiza uma receita salva", description = "Permite alterar dados de uma receita já existente" +
            " no banco local.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada com o ID informado")
    })
    ResponseEntity<ReceitaResponseDTO> atualizarReceita(
            @Parameter(description = "ID da receita a ser atualizada") Long id,
            ReceitaRequestDTO dto);
}
