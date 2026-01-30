package gr.aueb.cf.recipeapp.controller;

import gr.aueb.cf.recipeapp.dto.RecipeMatchDTO;
import gr.aueb.cf.recipeapp.model.entity.Recipe;
import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FridgeController {

    private final RecipeService recipeService;

    public FridgeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"/", "/fridge"})
    public String fridge(Model model) {
        List<Recipe> allRecipes = recipeService.findAllRecipesAlphabetical();

        model.addAttribute("items", "");
        model.addAttribute("searched", false);
        model.addAttribute("exactFound", false);
        model.addAttribute("matches", List.of());
        model.addAttribute("allRecipes", allRecipes);

        return "fridge";
    }

    @PostMapping("/fridge")
    public String search(@RequestParam("items") String items, Model model) {

        List<String> parsed = parseIngredients(items);
        List<RecipeMatchDTO> matches = recipeService.recommendRecipes(parsed);

        boolean exactFound = matches.stream().anyMatch(m -> m.getMissingCount() == 0);

        model.addAttribute("items", items == null ? "" : items);
        model.addAttribute("searched", true);
        model.addAttribute("exactFound", exactFound);
        model.addAttribute("matches", matches);
        model.addAttribute("allRecipes", List.of());

        return "fridge";
    }

    private List<String> parseIngredients(String input) {
        if (input == null) return List.of();

        String cleaned = input.toLowerCase()
                .replace("και", " ")
                .replace("kai", " ");

        return Arrays.stream(cleaned.split("[,\\n\\t ]+"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }
}
