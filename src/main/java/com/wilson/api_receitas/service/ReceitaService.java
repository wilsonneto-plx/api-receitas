package com.wilson.api_receitas.service;

import com.wilson.api_receitas.client.ReceitaApiClient;
import com.wilson.api_receitas.client.TranslationApiClient;
import com.wilson.api_receitas.dto.MealDTO;
import com.wilson.api_receitas.dto.ReceitaRequestDTO;
import com.wilson.api_receitas.dto.ReceitaResponseDTO;
import com.wilson.api_receitas.dto.TheMealDbResponseDTO;
import com.wilson.api_receitas.model.Receita;
import com.wilson.api_receitas.repository.ReceitaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            throw new EntityNotFoundException("Receita não encontrada. 😥");
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

    public Page<ReceitaResponseDTO> listarReceitas(Pageable pageable) {

        return repository.findAll(pageable)
                .map(ReceitaResponseDTO::new);

    }

    public ReceitaResponseDTO buscarPorId(Long id) {

        Receita receita = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado receita com o ID " + id +
                        " no banco de dados." ));

        return new ReceitaResponseDTO(receita);
    }

    public void deletarReceita(Long id) {

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Não é possível deletar. Receita com o ID " + id + " não encontrada.");
        }

        repository.deleteById(id);

    }

    @Transactional
    public ReceitaResponseDTO atualizarReceita(Long id, ReceitaRequestDTO dto) {

        Receita receita = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado receita com o ID " + id +
                        " no banco de dados."));

        receita.atualizar(dto.nomeTraduzido(), dto.categoria());

        repository.save(receita);

        return new ReceitaResponseDTO(receita);

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
