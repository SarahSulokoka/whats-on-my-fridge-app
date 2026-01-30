package gr.aueb.cf.recipeapp.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.aueb.cf.recipeapp.model.entity.Ingredient;
import gr.aueb.cf.recipeapp.model.entity.Recipe;
import gr.aueb.cf.recipeapp.model.entity.RecipeIngredient;
import gr.aueb.cf.recipeapp.model.entity.User;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.repository.IngredientRepository;
import gr.aueb.cf.recipeapp.repository.RecipeIngredientRepository;
import gr.aueb.cf.recipeapp.repository.RecipeRepository;
import gr.aueb.cf.recipeapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    private static final String THEMEALDB_BASE = "https://www.themealdb.com/api/json/v1/1";
    private static final int TARGET_RECIPES = 100;
    private static final int CHUNK = 50;

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> seed(userRepository, ingredientRepository, recipeRepository, recipeIngredientRepository, passwordEncoder);
    }

    @Transactional
    void seed(
            UserRepository userRepository,
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            PasswordEncoder passwordEncoder
    ) {
        User demo = userRepository.findByUsername("demo").orElseGet(() -> {
            User u = new User();
            u.setUsername("demo");
            u.setPassword(passwordEncoder.encode("demo"));
            return userRepository.save(u);
        });

        long existing = recipeRepository.count();
        if (existing >= TARGET_RECIPES) return;

        int toCreate = (int) (TARGET_RECIPES - existing);

        RestClient client = RestClient.create();

        List<Meal> meals = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();

        int safety = 0;
        while (meals.size() < toCreate && safety < toCreate * 10) {
            safety++;

            MealDbResponse randomResp = client.get()
                    .uri(THEMEALDB_BASE + "/random.php")
                    .retrieve()
                    .body(MealDbResponse.class);

            if (randomResp == null || randomResp.meals == null || randomResp.meals.isEmpty()) continue;

            Meal meal = randomResp.meals.get(0);
            if (meal == null || meal.idMeal == null) continue;

            if (!usedIds.add(meal.idMeal)) continue;

            String title = safe(meal.strMeal);
            if (title.isBlank()) continue;

            List<String> ingNames = meal.extractIngredientNames();
            if (ingNames.isEmpty()) continue;

            if (safe(meal.strInstructions).isBlank()) meal.strInstructions = "No instructions available.";

            meals.add(meal);
        }

        if (meals.isEmpty()) return;

        Map<String, Ingredient> ingredientCache = new HashMap<>();
        List<Ingredient> allExistingIngredients = ingredientRepository.findAll();
        for (Ingredient i : allExistingIngredients) {
            if (i.getName() != null) ingredientCache.put(i.getName().trim().toLowerCase(Locale.ROOT), i);
        }

        List<Recipe> recipesToSave = new ArrayList<>();
        List<List<String>> recipeIngredientsNames = new ArrayList<>();

        for (Meal m : meals) {
            Recipe r = new Recipe();
            r.setTitle(safe(m.strMeal));

            String desc = safe(m.strInstructions);
            if (desc.length() > 600) desc = desc.substring(0, 600) + "...";
            r.setDescription(desc);

            r.setOwner(demo);

            recipesToSave.add(r);
            recipeIngredientsNames.add(m.extractIngredientNames());
        }

        for (int start = 0; start < recipesToSave.size(); start += CHUNK) {
            int end = Math.min(start + CHUNK, recipesToSave.size());
            List<Recipe> chunk = recipesToSave.subList(start, end);
            recipeRepository.saveAll(chunk);
            recipeRepository.flush();
        }

        List<RecipeIngredient> links = new ArrayList<>();

        for (int idx = 0; idx < recipesToSave.size(); idx++) {
            Recipe recipe = recipesToSave.get(idx);
            List<String> ingNames = recipeIngredientsNames.get(idx);

            for (String raw : ingNames) {
                String name = raw.trim().toLowerCase(Locale.ROOT);
                if (name.isBlank()) continue;

                Ingredient ing = ingredientCache.get(name);
                if (ing == null) {
                    Ingredient ni = new Ingredient();
                    ni.setName(name);
                    ing = ingredientRepository.save(ni);
                    ingredientCache.put(name, ing);
                }

                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipe(recipe);
                ri.setIngredient(ing);
                ri.setQuantity(1.0);
                ri.setUnit(Unit.PIECE);

                links.add(ri);
            }
        }

        for (int start = 0; start < links.size(); start += 200) {
            int end = Math.min(start + 200, links.size());
            List<RecipeIngredient> chunk = links.subList(start, end);
            recipeIngredientRepository.saveAll(chunk);
            recipeIngredientRepository.flush();
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MealDbResponse {
        public List<Meal> meals;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meal {
        public String idMeal;
        public String strMeal;

        @JsonProperty("strInstructions")
        public String strInstructions;

        public String strIngredient1;
        public String strIngredient2;
        public String strIngredient3;
        public String strIngredient4;
        public String strIngredient5;
        public String strIngredient6;
        public String strIngredient7;
        public String strIngredient8;
        public String strIngredient9;
        public String strIngredient10;
        public String strIngredient11;
        public String strIngredient12;
        public String strIngredient13;
        public String strIngredient14;
        public String strIngredient15;
        public String strIngredient16;
        public String strIngredient17;
        public String strIngredient18;
        public String strIngredient19;
        public String strIngredient20;

        public List<String> extractIngredientNames() {
            List<String> raw = Arrays.asList(
                    strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                    strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                    strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
                    strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
            );

            return raw.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .distinct()
                    .collect(Collectors.toList());
        }
    }
}
