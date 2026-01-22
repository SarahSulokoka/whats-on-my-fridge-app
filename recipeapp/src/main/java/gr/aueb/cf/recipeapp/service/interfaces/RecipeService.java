package gr.aueb.cf.recipeapp.service.interfaces;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.model.entity.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findRecipesByIngredientNames(List<String> ingredientNames);
    void createRecipe(CreateRecipeDTO dto);
}
