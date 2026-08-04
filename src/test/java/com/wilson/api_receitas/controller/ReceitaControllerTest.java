package com.wilson.api_receitas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.api_receitas.dto.ReceitaRequestDTO;
import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.service.ReceitaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReceitaController.class)
public class ReceitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ReceitaService service;

    @Test
    @DisplayName("GET /buscar - Deve retornar 200 OK e o DTO da receita buscada")
    void buscarReceita_Sucesso() throws Exception {

        String nomeBusca = "panqueca";
        ReceitaResponseDTO responseDto = criarResponseDtoMock();

        given(service.buscarETraduzirReceita(nomeBusca)).willReturn(responseDto);

        mockMvc.perform(get("/api/receitas/buscar")
                        .param("nome", nomeBusca)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Panqueca"))
                .andExpect(jsonPath("$.categoria").value("Sobremesa"));

        then(service).should().buscarETraduzirReceita(nomeBusca);
    }

    @Test
    @DisplayName("GET /buscar - Deve retornar 404 Not Found quando receita não existir")
    void buscarReceita_Falha_NaoEncontrado() throws Exception {

        String nomeBusca = "comida inventada";

        given(service.buscarETraduzirReceita(nomeBusca))
                .willThrow(new EntityNotFoundException("Receita não encontrada. 😥"));

        mockMvc.perform(get("/api/receitas/buscar")
                        .param("nome", nomeBusca)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET / - Deve retornar 200 OK e uma página de receitas")
    void listarReceitas_Sucesso() throws Exception {

        ReceitaResponseDTO responseDto = criarResponseDtoMock();
        Page<ReceitaResponseDTO> paginaMock = new PageImpl<>(List.of(responseDto));

        given(service.listarReceitas(any(Pageable.class))).willReturn(paginaMock);

        mockMvc.perform(get("/api/receitas")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Panqueca"));
    }

    @Test
    @DisplayName("GET /{id} - Deve retornar 200 OK quando o ID existir")
    void buscarPorId_Sucesso() throws Exception {

        Long id = 1L;
        ReceitaResponseDTO responseDto = criarResponseDtoMock();

        given(service.buscarPorId(id)).willReturn(responseDto);

        mockMvc.perform(get("/api/receitas/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Panqueca"));
    }

    @Test
    @DisplayName("GET /{id} - Deve retornar 404 Not Found quando buscar por ID inexistente")
    void buscarPorId_Falha_NaoEncontrado() throws Exception {

        Long idInvalido = 99L;

        given(service.buscarPorId(idInvalido))
                .willThrow(new EntityNotFoundException("Não foi encontrado receita com o ID 99"));

        mockMvc.perform(get("/api/receitas/{id}", idInvalido)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /{id} - Deve retornar 204 No Content ao deletar")
    void deletarReceita_Sucesso() throws Exception {

        Long id = 1L;

        doNothing().when(service).deletarReceita(id);

        mockMvc.perform(delete("/api/receitas/{id}", id))
                .andExpect(status().isNoContent());

        then(service).should().deletarReceita(id);
    }

    @Test
    @DisplayName("DELETE /{id} - Deve retornar 404 Not Found ao tentar deletar ID inexistente")
    void deletarReceita_Falha_NaoEncontrado() throws Exception {

        Long idInvalido = 99L;

        doThrow(new EntityNotFoundException("Receita não encontrada."))
                .when(service).deletarReceita(idInvalido);

        mockMvc.perform(delete("/api/receitas/{id}", idInvalido))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /{id} - Deve atualizar e retornar 200 OK com o DTO atualizado")
    void atualizarReceita_Sucesso() throws Exception {

        Long id = 1L;
        ReceitaRequestDTO requestDto = new ReceitaRequestDTO("Panqueca Turbinada", "Café da Manhã");

        ReceitaResponseDTO responseDto = new ReceitaResponseDTO(1L,"Panqueca Turbinada", "Café da Manhã",
                "EUA",List.of("Misture tudo", "Frite na frigideira"),"http://imagem.com/panqueca.jpg",
                List.of("leite", "ovo", "farinha"));

        given(service.atualizarReceita(eq(id), any(ReceitaRequestDTO.class))).willReturn(responseDto);

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/receitas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Panqueca Turbinada"))
                .andExpect(jsonPath("$.categoria").value("Café da Manhã"));
    }

    @Test
    @DisplayName("PUT /{id} - Deve retornar 404 Not Found ao tentar atualizar ID inexistente")
    void atualizarReceita_Falha_NaoEncontrado() throws Exception {

        Long idInvalido = 99L;
        ReceitaRequestDTO requestDto = new ReceitaRequestDTO("Panqueca Turbinada", "Café da Manhã");

        given(service.atualizarReceita(eq(idInvalido), any(ReceitaRequestDTO.class)))
                .willThrow(new EntityNotFoundException("Receita não encontrada."));

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/receitas/{id}", idInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /{id} - Deve retornar 400 Bad Request quando o DTO for inválido (@Valid)")
    void atualizarReceita_Falha_DadosInvalidos() throws Exception {

        Long id = 1L;

        ReceitaRequestDTO requestDtoInvalido = new ReceitaRequestDTO("", "Café da Manhã");

        String jsonBody = objectMapper.writeValueAsString(requestDtoInvalido);

        mockMvc.perform(put("/api/receitas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        then(service).shouldHaveNoInteractions();
    }

    private ReceitaResponseDTO criarResponseDtoMock() {
        return new ReceitaResponseDTO(
                1L,
                "Panqueca",
                "Sobremesa",
                "EUA",
                List.of("Misture tudo", "Frite na frigideira"),
                "http://imagem.com/panqueca.jpg",
                List.of("leite", "ovo", "farinha")
        );
    }

}
