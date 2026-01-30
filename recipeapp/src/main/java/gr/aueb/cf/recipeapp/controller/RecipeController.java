package gr.aueb.cf.recipeapp.controller;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.model.entity.Recipe;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setIngredients(new ArrayList<>());
        dto.getIngredients().add(new CreateRecipeDTO.IngredientDTO());

        model.addAttribute("recipe", dto);
        model.addAttribute("units", Unit.values());
        return "recipe-create";
    }

    @PostMapping("/add-ingredient")
    public String addIngredientRow(@ModelAttribute("recipe") CreateRecipeDTO dto, Model model) {
        if (dto.getIngredients() == null) {
            dto.setIngredients(new ArrayList<>());
        }
        dto.getIngredients().add(new CreateRecipeDTO.IngredientDTO());

        model.addAttribute("recipe", dto);
        model.addAttribute("units", Unit.values());
        return "recipe-create";
    }

    @PostMapping
    public String saveRecipe(@ModelAttribute("recipe") CreateRecipeDTO dto, Authentication authentication) {
        String username = authentication.getName();
        recipeService.createRecipe(dto, username);
        return "redirect:/fridge";
    }

    @GetMapping("/mine")
    public String myRecipes(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Recipe> recipes = recipeService.findMyRecipes(username);
        model.addAttribute("recipes", recipes);
        return "my-recipes";
    }

    @PostMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        recipeService.deleteRecipe(id, username);
        return "redirect:/recipes/mine";
    }

    @GetMapping("/{id}/edit")
    public String editRecipeForm(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        CreateRecipeDTO dto = recipeService.getRecipeForEdit(id, username);

        model.addAttribute("recipeId", id);
        model.addAttribute("recipe", dto);
        model.addAttribute("units", Unit.values());
        return "recipe-edit";
    }

    @PostMapping("/{id}/edit/add-ingredient")
    public String addIngredientRowEdit(@PathVariable Long id,
                                       @ModelAttribute("recipe") CreateRecipeDTO dto,
                                       Model model) {
        if (dto.getIngredients() == null) {
            dto.setIngredients(new ArrayList<>());
        }
        dto.getIngredients().add(new CreateRecipeDTO.IngredientDTO());

        model.addAttribute("recipeId", id);
        model.addAttribute("recipe", dto);
        model.addAttribute("units", Unit.values());
        return "recipe-edit";
    }

    @PostMapping("/{id}/edit")
    public String editRecipeSubmit(@PathVariable Long id,
                                   @ModelAttribute("recipe") CreateRecipeDTO dto,
                                   Authentication authentication) {
        String username = authentication.getName();
        recipeService.updateRecipe(id, dto, username);
        return "redirect:/recipes/mine";
    }
}
