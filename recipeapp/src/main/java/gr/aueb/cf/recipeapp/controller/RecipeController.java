package gr.aueb.cf.recipeapp.controller;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public String saveRecipe(@ModelAttribute("recipe") CreateRecipeDTO dto) {
        recipeService.createRecipe(dto);
        return "redirect:/fridge";
    }
}
