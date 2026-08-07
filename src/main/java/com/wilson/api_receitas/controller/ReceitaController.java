package com.wilson.api_receitas.controller;

import com.wilson.api_receitas.dto.ReceitaRequestDTO;
import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.service.ReceitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/receitas")
@RequiredArgsConstructor
public class ReceitaController implements ReceitaControllerOpenApi{

    private final ReceitaService service;

    @Override
    @GetMapping("/buscar")
    public ResponseEntity<ReceitaResponseDTO> buscarReceita(@RequestParam("nome") String nomeReceita) {

        ReceitaResponseDTO response = service.buscarETraduzirReceita(nomeReceita);

        return ResponseEntity.ok(response);

    }

    @Override
    @GetMapping
    public ResponseEntity <Page<ReceitaResponseDTO>> listarReceitas(
            @ParameterObject
            @PageableDefault(size = 10, page = 0, sort = "nomeTraduzido") Pageable pageable
            ) {

        Page<ReceitaResponseDTO> response = service.listarReceitas(pageable);

        return ResponseEntity.ok(response);

    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(@PathVariable Long id) {

        ReceitaResponseDTO response = service.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id) {

        service.deletarReceita(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> atualizarReceita(
            @PathVariable Long id,
            @RequestBody @Valid ReceitaRequestDTO dto) {

        ReceitaResponseDTO receitaAtualziada = service.atualizarReceita(id, dto);

        return ResponseEntity.ok(receitaAtualziada);

    }
}
