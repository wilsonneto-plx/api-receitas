package com.wilson.api_receitas.client;

import com.wilson.api_receitas.dto.TheMealDbResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mealdb-client", url = "${mealdb.api.url}")
public interface ReceitaApiClient {

    @GetMapping("/search.php")
    TheMealDbResponseDTO buscarReceita(@RequestParam("s") String nomeReceita);
}
