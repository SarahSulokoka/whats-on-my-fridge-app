package gr.aueb.cf.recipeapp.service.impl;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.model.entity.*;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.repository.*;
import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            UserRepository userRepository,
            IngredientRepository ingredientRepository,
            RecipeIngredientRepository recipeIngredientRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public List<Recipe> findRecipesByIngredientNames(List<String> ingredientNames) {
        if (ingredientNames == null || ingredientNames.isEmpty()) return List.of();

        Set<String> fridge = ingredientNames.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());

        if (fridge.isEmpty()) return List.of();

        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(r -> r.getRecipeIngredients() != null)
                .filter(r -> {
                    Set<String> recipeIngs = r.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName().trim().toLowerCase())
                            .collect(Collectors.toSet());

                    return fridge.containsAll(recipeIngs);
                })
                .toList();
    }

    @Override
    public void createRecipe(CreateRecipeDTO dto) {

        User user = userRepository.findByUsername("demo")
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername("demo");
                    u.setPassword("demo");
                    return userRepository.save(u);
                });

        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setOwner(user);
        recipeRepository.save(recipe);

        if (dto.getIngredients() == null) return;

        for (CreateRecipeDTO.IngredientDTO ingDto : dto.getIngredients()) {

            if (ingDto == null) continue;

            String ingName = ingDto.getName() == null ? "" : ingDto.getName().trim();
            if (ingName.isBlank()) continue;

            Ingredient ingredient = ingredientRepository
                    .findByNameIgnoreCase(ingName)
                    .orElseGet(() -> {
                        Ingredient newIng = new Ingredient();
                        newIng.setName(ingName.toLowerCase());
                        return ingredientRepository.save(newIng);
                    });

            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredient);

            Double qty = ingDto.getQuantity() == null ? 0.0 : ingDto.getQuantity();
            ri.setQuantity(qty);

            Unit unit = Unit.PIECE;
            if (ingDto.getUnit() != null && !ingDto.getUnit().isBlank()) {
                unit = Unit.valueOf(ingDto.getUnit().trim().toUpperCase());
            }
            ri.setUnit(unit);

            recipeIngredientRepository.save(ri);
        }
    }
}
