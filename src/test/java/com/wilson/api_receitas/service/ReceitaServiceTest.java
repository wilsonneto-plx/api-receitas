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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ReceitaServiceTest {

    @Mock
    private ReceitaRepository repository;

    @Mock
    private ReceitaApiClient receitaApiClient;

    @Mock
    private TranslationApiClient translationApiClient;

    @InjectMocks
    private ReceitaService service;

    @Test
    @DisplayName("Deve buscar, traduzir e salvar uma nova receita com sucesso.")
    void deveBuscarETraduzirReceitaComSucesso() {

        String nomeReceitaPtBr = "panqueca";
        String nomeReceitaEn = "pancake";

        given(translationApiClient.traduzirEAdaptar(nomeReceitaPtBr,"pt-br","en"))
                .willReturn(nomeReceitaEn);

        MealDTO mealDtoMock = criarMealDtoMock();

        TheMealDbResponseDTO responseApi = new TheMealDbResponseDTO(List.of(mealDtoMock));

        given(receitaApiClient.buscarReceita(nomeReceitaEn)).willReturn(responseApi);

        given(repository.findByExternalId("123")).willReturn(Optional.empty());

        given(translationApiClient.traduzirEAdaptar("Pancake", "en", "pt-br"))
                .willReturn("Panqueca");
        given(translationApiClient.traduzirEAdaptar("Dessert", "en", "pt-br"))
                .willReturn("Sobremesa");
        given(translationApiClient.traduzirEAdaptar("Mix it all", "en", "pt-br"))
                .willReturn("Misture tudo");

        String ingredientesUnidos = "Flour | Milk | Egg";
        String ingredientesTraduzidos = "Farinha | Leite | Ovo";
        given(translationApiClient.traduzirEAdaptar(ingredientesUnidos, "en", "pt-br"))
                .willReturn(ingredientesTraduzidos);

        given(repository.save(any(Receita.class))).willAnswer(returnsFirstArg());

        ReceitaResponseDTO resultado = service.buscarETraduzirReceita(nomeReceitaPtBr);

        assertNotNull(resultado);

        assertEquals("Panqueca", resultado.nome());
        assertEquals("Sobremesa", resultado.categoria());

        then(repository).should().findByExternalId("123");
        then(translationApiClient).should(times(5))
                .traduzirEAdaptar(anyString(), anyString(), anyString());
        then(repository).should().save(any(Receita.class));

    }

    @Test
    @DisplayName("Deve lançar exceção EntityNotFoundException quando a API TheMealDB não encontrar a receita")
    void buscarETraduzirReceita_CenarioFalhaApiTheMealDb_LancaExcecao() {

        String nomeReceitaPtBr = "comida inventada";
        String nomeReceitaEn = "invented food";

        given(translationApiClient.traduzirEAdaptar(nomeReceitaPtBr, "pt-br", "en"))
                .willReturn(nomeReceitaEn);

        TheMealDbResponseDTO apiResponse = new TheMealDbResponseDTO(null);
        given(receitaApiClient.buscarReceita(nomeReceitaEn))
                .willReturn(apiResponse);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.buscarETraduzirReceita(nomeReceitaPtBr);
        });

        assertEquals("Receita não encontrada. 😥", exception.getMessage());

        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve retornar direto do banco e não traduzir nada se a receita já existir")
    void buscarETraduzirReceita_CenarioJaExiste_RetornaDoBanco() {
        String nomeReceitaPtBr = "panqueca";
        String nomeReceitaEn = "pancake";

        given(translationApiClient.traduzirEAdaptar(nomeReceitaPtBr, "pt-br", "en"))
                .willReturn(nomeReceitaEn);

        MealDTO mealDtoMock = criarMealDtoMock();
        TheMealDbResponseDTO responseApi = new TheMealDbResponseDTO(List.of(mealDtoMock));

        given(receitaApiClient.buscarReceita(nomeReceitaEn)).willReturn(responseApi);

        Receita receitaJaSalvaNoBanco = criarReceitaMock();

        given(repository.findByExternalId("123")).willReturn(Optional.of(receitaJaSalvaNoBanco));

        ReceitaResponseDTO resultado = service.buscarETraduzirReceita(nomeReceitaPtBr);

        assertNotNull(resultado);

        assertEquals("Panqueca do Banco", resultado.nome());
        assertEquals("Sobremesa", resultado.categoria());

        then(repository).should().findByExternalId("123");
        then(repository).should(never()).save(any(Receita.class));

        then(translationApiClient).should(times(1))
                .traduzirEAdaptar(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve retornar uma página de receitas com sucesso")
    void listarReceitas_Sucesso() {

        Pageable pageable = PageRequest.of(0, 10);


        Receita receitaMock = criarReceitaMock();
        List<Receita> listaReceitas = List.of(receitaMock);

        Page<Receita> paginaDeReceitasMock = new PageImpl<>(listaReceitas, pageable, listaReceitas.size());

        given(repository.findAll(pageable)).willReturn(paginaDeReceitasMock);

        Page<ReceitaResponseDTO> resultado = service.listarReceitas(pageable);

        assertNotNull(resultado);

        assertEquals(1, resultado.getTotalElements());

        ReceitaResponseDTO dtoRetornado = resultado.getContent().get(0);
        assertEquals("Panqueca do Banco", dtoRetornado.nome());

        then(repository).should().findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar ReceitaResponseDTO quando buscar por ID existente")
    void buscarPorId_Sucesso() {

        Long id = 1L;
        Receita receitaMock = criarReceitaMock();

        given(repository.findById(id)).willReturn(Optional.of(receitaMock));

        ReceitaResponseDTO resultado = service.buscarPorId(id);

        assertNotNull(resultado);

        assertEquals("Panqueca do Banco", resultado.nome());
        assertEquals("Sobremesa", resultado.categoria());

        then(repository).should().findById(id);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando buscar por ID inexistente")
    void buscarPorId_Falha_NaoEncontrado() {

        Long idInvalido = 99L;

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.buscarPorId(idInvalido);
        });

        assertEquals("Não foi encontrado receita com o ID 99 no banco de dados.", exception.getMessage());

        then(repository).should().findById(idInvalido);
    }

    @Test
    @DisplayName("Deve deletar a receita quando o ID existir")
    void deletarReceita_Sucesso() {

        Long id = 1L;

        given(repository.existsById(id)).willReturn(true);

        service.deletarReceita(id);

        then(repository).should().existsById(id);

        then(repository).should().deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar deletar ID inexistente")
    void deletarReceita_Falha_NaoEncontrado() {

        Long idInvalido = 99L;

        given(repository.existsById(idInvalido)).willReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.deletarReceita(idInvalido);
        });

        assertEquals("Não é possível deletar. Receita com o ID 99 não encontrada.", exception.getMessage());

        then(repository).should().existsById(idInvalido);

        then(repository).should(never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve atualizar e salvar a receita com sucesso")
    void atualizarReceita_Sucesso() {

        Long id = 1L;
        ReceitaRequestDTO dtoRequest = new ReceitaRequestDTO("Panqueca Turbinada",
                "Café da Manhã ");

        Receita receitaMock = criarReceitaMock();

        given(repository.findById(id)).willReturn(Optional.of(receitaMock));

        // Configuramos o save para retornar a própria entidade
        given(repository.save(any(Receita.class))).willAnswer(returnsFirstArg());

        // WHEN
        ReceitaResponseDTO resultado = service.atualizarReceita(id, dtoRequest);

        assertNotNull(resultado);

        assertEquals("Panqueca Turbinada", resultado.nome());
        assertEquals("Café da Manhã Especial", resultado.categoria());

        then(repository).should().findById(id);
        then(repository).should().save(receitaMock);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar ID inexistente")
    void atualizarReceita_Falha_NaoEncontrado() {

        Long idInvalido = 99L;
        ReceitaRequestDTO dtoRequest = new ReceitaRequestDTO("Panqueca Turbinada", "Café da Manhã");

        given(repository.findById(idInvalido)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.atualizarReceita(idInvalido, dtoRequest);
        });

        assertEquals("Não foi encontrado receita com o ID 99 no banco de dados.", exception.getMessage());

        then(repository).should().findById(idInvalido);

        then(repository).should(never()).save(any(Receita.class));
    }
















    private MealDTO criarMealDtoMock() {
        return new MealDTO(
                "123",
                "Pancake",
                "Dessert",
                "EUA",
                "Mix it all",
                null,
                "Flour", "Milk", "Egg",
                null, null, null, null, null,
                null, null, null

        );
    }

    private Receita criarReceitaMock() {
        return new Receita(
                "123",
                "Pancake",
                "Panqueca do Banco",
                "Sobremesa",
                "EUA",
                "Instruções do banco",
                "http://imagem.com",
                List.of("leite", "ovo")
        );
    }

}
