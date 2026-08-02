package com.wilson.api_receitas.client.impl;

import com.wilson.api_receitas.client.TranslationApiClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class GeminiTranslationClientImpl implements TranslationApiClient {

    private final ChatClient chatClient;

    public GeminiTranslationClientImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String traduzirEAdaptar(String texto, String idiomaOrigem, String idiomaDestino) {

        String prompt = String.format(
                "Você é um chef de cozinha e tradutor profissional. " +
                        "Traduza o seguinte texto culinário do %s para o %s. " +
                        "Regras: " +
                        "1. Traduza o texto mantendo ESTRITAMENTE o mesmo número de elementos e preservando os" +
                        " separadores '|' exatamente onde estão. Não adicione vírgula, ou pontuações extras. " +
                        "2. Adapte medidas imperiais (oz, lb, etc) para o sistema métrico (gramas, ml). " +
                        "3. Adapte termos culinários para o vocabulário comum brasileiro. " +
                        "4. Responda APENAS com o texto traduzido, sem explicações adicionais.\n\n" +
                        "Texto original: %s",
                idiomaOrigem, idiomaDestino, texto
        );

        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();

    }
}
