package gr.aueb.cf.recipeapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateRecipeDTO {

    @NotBlank
    private String title;

    private String description;

    @NotEmpty
    private List<IngredientDTO> ingredients;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class IngredientDTO {
        private String name;
        private Double quantity;
        private String unit;
    }
}
