package com.wilson.api_receitas.service;

import com.wilson.api_receitas.client.ReceitaApiClient;
import com.wilson.api_receitas.client.TranslationApiClient;
import com.wilson.api_receitas.dto.MealDTO;
import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.dto.TheMealDbResponseDTO;
import com.wilson.api_receitas.model.Receita;
import com.wilson.api_receitas.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository repository;

    private final ReceitaApiClient receitaApiClient;

    private final TranslationApiClient translationApiClient;

    @Transactional
    public ReceitaResponseDTO buscarETraduzirReceita(String nomeReceitaPtBr) {

        String nomeReceitaEn = translationApiClient.
                traduzirEAdaptar(nomeReceitaPtBr, "pt-br","en");

        TheMealDbResponseDTO response = receitaApiClient.buscarReceita(nomeReceitaEn);

        if (response == null || response.meals() == null || response.meals().isEmpty()) {
            throw new RuntimeException("Receita não encontrada. 😥");
        }

        MealDTO mealDTO = response.meals().get(0);
        String exernalId = mealDTO.idMeal();

        Optional<Receita> receitaExistente = repository.findByExternalId(exernalId);

        if (receitaExistente.isPresent()){
            return new ReceitaResponseDTO(receitaExistente.get());
        }

        String nomeTraduzido = translationApiClient
                .traduzirEAdaptar(mealDTO.strMeal(),"en","pt-br");

        String categoriaTraduzida = translationApiClient
                .traduzirEAdaptar(mealDTO.strCategory(),"en","pt-br");

        String instrucoesTraduzidas = translationApiClient
                .traduzirEAdaptar(mealDTO.strInstructions(),"en","pt-br");

        List<String> ingredientesOriginais = extrairIngredientes(mealDTO);
        String ingredientesTexto = String.join(" | ", ingredientesOriginais);
        String ingredientesTraduzidosTexto = translationApiClient.traduzirEAdaptar(ingredientesTexto, "en", "pt-br");
        List<String> ingredientesTraduzidos = Arrays.asList(ingredientesTraduzidosTexto.split(" \\| "));

        Receita novaReceita = mealDTO.toEntity(
                nomeTraduzido,
                categoriaTraduzida,
                instrucoesTraduzidas,
                ingredientesTraduzidos);

        repository.save(novaReceita);

        return new ReceitaResponseDTO(novaReceita);

    }



    private List<String> extrairIngredientes(MealDTO mealDTO) {

        List<String> ingredientes = new ArrayList<>();

        adicionarSeValido(ingredientes, mealDTO.strIngredient1());
        adicionarSeValido(ingredientes, mealDTO.strIngredient2());
        adicionarSeValido(ingredientes,mealDTO.strIngredient4());
        adicionarSeValido(ingredientes,mealDTO.strIngredient5());
        adicionarSeValido(ingredientes,mealDTO.strIngredient6());
        adicionarSeValido(ingredientes,mealDTO.strIngredient7());
        adicionarSeValido(ingredientes,mealDTO.strIngredient8());
        adicionarSeValido(ingredientes,mealDTO.strIngredient9());
        adicionarSeValido(ingredientes,mealDTO.strIngredient10());
        adicionarSeValido(ingredientes,mealDTO.strIngredient11());

        return ingredientes;
    }

    private void adicionarSeValido(List<String> lista, String ingrediente) {

        if (ingrediente != null && !ingrediente.trim().isEmpty()) {
            lista.add(ingrediente.trim());
        }

    }




}
