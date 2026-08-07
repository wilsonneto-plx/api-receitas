package com.wilson.api_receitas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🍳 Receitas AI API")
                        .description("API RESTful inteligente que une o poder da IA generativa do Gemini com o " +
                                "TheMealDB para traduzir receitas.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Wilson Neto")
                                .email("wilsonnetoplx10@gmail.com")
                                .url("https://www.linkedin.com/in/wilson-neto-5b1207398")));
    }
}
