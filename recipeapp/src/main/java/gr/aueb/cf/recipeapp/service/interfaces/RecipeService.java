package gr.aueb.cf.recipeapp.service.interfaces;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.dto.RecipeMatchDTO;
import gr.aueb.cf.recipeapp.model.entity.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> findAllRecipesAlphabetical();

    List<RecipeMatchDTO> recommendRecipes(List<String> ingredientNames);

    void createRecipe(CreateRecipeDTO dto, String username);

    List<Recipe> findMyRecipes(String username);

    void deleteRecipe(Long recipeId, String username);

    CreateRecipeDTO getRecipeForEdit(Long recipeId, String username);

    void updateRecipe(Long recipeId, CreateRecipeDTO dto, String username);
}
