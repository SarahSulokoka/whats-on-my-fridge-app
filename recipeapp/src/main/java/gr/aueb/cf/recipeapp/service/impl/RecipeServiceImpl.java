package gr.aueb.cf.recipeapp.service.impl;

import gr.aueb.cf.recipeapp.dto.CreateRecipeDTO;
import gr.aueb.cf.recipeapp.dto.RecipeMatchDTO;
import gr.aueb.cf.recipeapp.model.entity.Ingredient;
import gr.aueb.cf.recipeapp.model.entity.Recipe;
import gr.aueb.cf.recipeapp.model.entity.RecipeIngredient;
import gr.aueb.cf.recipeapp.model.entity.User;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.repository.IngredientRepository;
import gr.aueb.cf.recipeapp.repository.RecipeIngredientRepository;
import gr.aueb.cf.recipeapp.repository.RecipeRepository;
import gr.aueb.cf.recipeapp.repository.UserRepository;
import gr.aueb.cf.recipeapp.service.interfaces.RecipeService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Recipe> findAllRecipesAlphabetical() {
        return recipeRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    @Override
    public List<RecipeMatchDTO> recommendRecipes(List<String> ingredientNames) {
        if (ingredientNames == null || ingredientNames.isEmpty()) return List.of();

        LinkedHashSet<String> fridge = ingredientNames.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (fridge.isEmpty()) return List.of();

        int n = fridge.size();
        int requiredMatch = (n <= 2) ? n : (n - 1);

        List<Recipe> allRecipes = recipeRepository.findAll();
        List<RecipeMatchDTO> results = new ArrayList<>();

        for (Recipe r : allRecipes) {
            if (r.getRecipeIngredients() == null || r.getRecipeIngredients().isEmpty()) continue;

            LinkedHashSet<String> recipeIngs = r.getRecipeIngredients().stream()
                    .map(ri -> ri.getIngredient() == null ? null : ri.getIngredient().getName())
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            if (recipeIngs.isEmpty()) continue;

            List<String> matched = new ArrayList<>();
            for (String x : fridge) {
                if (recipeIngs.contains(x)) matched.add(x);
            }

            int matchedFromInput = matched.size();
            if (matchedFromInput < requiredMatch) continue;

            List<String> missingFromRecipe = recipeIngs.stream()
                    .filter(x -> !fridge.contains(x))
                    .toList();

            RecipeMatchDTO dto = new RecipeMatchDTO();
            dto.setRecipeId(r.getId());
            dto.setTitle(r.getTitle());
            dto.setDescription(r.getDescription());
            dto.setHaveCount(matchedFromInput);
            dto.setTotalIngredients(recipeIngs.size());
            dto.setMatched(matched);
            dto.setMissing(missingFromRecipe);
            dto.setMissingCount(missingFromRecipe.size());

            results.add(dto);
        }

        results.sort(
                Comparator
                        .comparingInt(RecipeMatchDTO::getMissingCount)
                        .thenComparingInt(RecipeMatchDTO::getHaveCount).reversed()
                        .thenComparing(RecipeMatchDTO::getTitle, String.CASE_INSENSITIVE_ORDER)
        );

        return results;
    }


    @Override
    public void createRecipe(CreateRecipeDTO dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setOwner(user);
        recipeRepository.save(recipe);

        saveIngredientsForRecipe(recipe, dto);
    }

    @Override
    public List<Recipe> findMyRecipes(String username) {
        return recipeRepository.findByOwnerUsernameOrderByIdDesc(username);
    }

    @Override
    public void deleteRecipe(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findByIdAndOwnerUsername(recipeId, username).orElseThrow();

        if (recipe.getRecipeIngredients() != null) {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                recipeIngredientRepository.delete(ri);
            }
        }

        recipeRepository.delete(recipe);
    }

    @Override
    public CreateRecipeDTO getRecipeForEdit(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findByIdAndOwnerUsername(recipeId, username).orElseThrow();

        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());

        List<CreateRecipeDTO.IngredientDTO> list = new ArrayList<>();
        if (recipe.getRecipeIngredients() != null) {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                CreateRecipeDTO.IngredientDTO ing = new CreateRecipeDTO.IngredientDTO();
                ing.setName(ri.getIngredient() != null ? ri.getIngredient().getName() : "");
                ing.setQuantity(ri.getQuantity());
                ing.setUnit(ri.getUnit() != null ? ri.getUnit().name() : "GRAM");
                list.add(ing);
            }
        }

        if (list.isEmpty()) {
            list.add(new CreateRecipeDTO.IngredientDTO());
        }

        dto.setIngredients(list);
        return dto;
    }
    @Override
    @Transactional
    public void updateRecipe(Long id, CreateRecipeDTO dto, String username) {

        Recipe recipe = recipeRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow();

        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipeRepository.save(recipe);


        recipeIngredientRepository.deleteByRecipeId(recipe.getId());
        recipeIngredientRepository.flush();


        saveIngredientsForRecipe(recipe, dto);
    }




    private void saveIngredientsForRecipe(Recipe recipe, CreateRecipeDTO dto) {
        if (dto.getIngredients() == null) return;

        Set<String> seen = new HashSet<>();

        for (CreateRecipeDTO.IngredientDTO ingDto : dto.getIngredients()) {
            if (ingDto == null) continue;

            String name = ingDto.getName() == null ? "" : ingDto.getName().trim().toLowerCase();
            if (name.isBlank()) continue;

            if (!seen.add(name)) continue;

            Ingredient ingredient = ingredientRepository
                    .findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        Ingredient newIng = new Ingredient();
                        newIng.setName(name);
                        return ingredientRepository.save(newIng);
                    });

            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredient);
            ri.setQuantity(ingDto.getQuantity() == null ? 0.0 : ingDto.getQuantity());

            String unitStr = ingDto.getUnit() == null ? "GRAM" : ingDto.getUnit().trim();
            ri.setUnit(Unit.valueOf(unitStr));

            recipeIngredientRepository.save(ri);
        }
    }

}
