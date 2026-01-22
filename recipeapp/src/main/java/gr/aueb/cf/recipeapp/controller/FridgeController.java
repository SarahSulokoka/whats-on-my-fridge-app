package gr.aueb.cf.recipeapp.controller;

import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/fridge")
public class FridgeController {

    private final RecipeService recipeService;

    public FridgeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String fridgeForm() {
        return "fridge";
    }

    @PostMapping("/search")
    public String search(@RequestParam("ingredients") String ingredients, Model model) {
        List<String> names = Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        model.addAttribute("recipes", recipeService.findRecipesByIngredientNames(names));
        model.addAttribute("ingredients", ingredients);
        return "fridge";
    }
}
