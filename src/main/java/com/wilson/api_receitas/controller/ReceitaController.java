package com.wilson.api_receitas.controller;

import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService service;

    @GetMapping("/buscar")
    public ResponseEntity<ReceitaResponseDTO> buscarReceita(@RequestParam("nome") String nomeReceita) {

        ReceitaResponseDTO response = service.buscarETraduzirReceita(nomeReceita);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity <List<ReceitaResponseDTO>> listarReceitas() {

        List<ReceitaResponseDTO> response = service.listarReceitas();

        return ResponseEntity.ok(response);

    }


}
