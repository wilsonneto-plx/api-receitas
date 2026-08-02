package com.wilson.api_receitas.controller;

import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
        public ResponseEntity <Page<ReceitaResponseDTO>> listarReceitas(
            @PageableDefault(size = 10, page = 0, sort = "nomeTraduzido") Pageable pageable
            ) {

        Page<ReceitaResponseDTO> response = service.listarReceitas(pageable);

        return ResponseEntity.ok(response);

    }


}
