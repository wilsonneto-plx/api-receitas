package com.wilson.api_receitas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wilson.api_receitas.model.Receita;

import java.util.List;

public record MealDTO(

        @JsonProperty("idMeal")
        String idMeal,

        @JsonProperty("strMeal")
        String strMeal,

        @JsonProperty("strCategory")
        String strCategory,

        @JsonProperty("strCountry")
        String strCountry,

        @JsonProperty("strInstructions")
        String strInstructions,

        @JsonProperty("strMealThumb")
        String strMealThumb,

        @JsonProperty("strIngredient1")
        String strIngredient1,

        @JsonProperty("strIngredient2")
        String strIngredient2,

        @JsonProperty("strIngredient3")
        String strIngredient3,

        @JsonProperty("strIngredient4")
        String strIngredient4,

        @JsonProperty("strIngredient5")
        String strIngredient5,

        @JsonProperty("strIngredient6")
        String strIngredient6,

        @JsonProperty("strIngredient7")
        String strIngredient7,

        @JsonProperty("strIngredient8")
        String strIngredient8,

        @JsonProperty("strIngredient9")
        String strIngredient9,

        @JsonProperty("strIngredient10")
        String strIngredient10,

        @JsonProperty("strIngredient11")
        String strIngredient11

) {

        public Receita toEntity(String nomePtBr, String categoriaPtBr,
                                String instrucoesPtBr, List<String> ingredientesPtBr) {

                return new Receita(
                        this.idMeal(),
                        this.strMeal(),
                        nomePtBr,
                        categoriaPtBr,
                        this.strCountry(),
                        instrucoesPtBr,
                        this.strMealThumb(),
                        ingredientesPtBr
                );

        }
}
