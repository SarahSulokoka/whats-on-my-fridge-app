package gr.aueb.cf.recipeapp.util;

import gr.aueb.cf.recipeapp.model.entity.*;
import gr.aueb.cf.recipeapp.model.enums.Unit;
import gr.aueb.cf.recipeapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository
    ) {
        return args -> {

            if (userRepository.count() > 0) return;

            User user = new User();
            user.setUsername("demo");
            user.setPassword("demo");
            userRepository.save(user);

            Ingredient egg = new Ingredient();
            egg.setName("egg");
            ingredientRepository.save(egg);

            Ingredient milk = new Ingredient();
            milk.setName("milk");
            ingredientRepository.save(milk);

            Ingredient flour = new Ingredient();
            flour.setName("flour");
            ingredientRepository.save(flour);

            Ingredient sugar = new Ingredient();
            sugar.setName("sugar");
            ingredientRepository.save(sugar);

            Ingredient butter = new Ingredient();
            butter.setName("butter");
            ingredientRepository.save(butter);

            Ingredient cheese = new Ingredient();
            cheese.setName("cheese");
            ingredientRepository.save(cheese);

            Ingredient tomato = new Ingredient();
            tomato.setName("tomato");
            ingredientRepository.save(tomato);

            Ingredient pasta = new Ingredient();
            pasta.setName("pasta");
            ingredientRepository.save(pasta);

            Ingredient oliveOil = new Ingredient();
            oliveOil.setName("olive oil");
            ingredientRepository.save(oliveOil);

            Ingredient salt = new Ingredient();
            salt.setName("salt");
            ingredientRepository.save(salt);

            Recipe pancakes = new Recipe();
            pancakes.setTitle("Pancakes");
            pancakes.setDescription("Mix ingredients and cook on pan");
            pancakes.setOwner(user);
            recipeRepository.save(pancakes);

            RecipeIngredient p1 = new RecipeIngredient();
            p1.setRecipe(pancakes);
            p1.setIngredient(egg);
            p1.setQuantity(2.0);
            p1.setUnit(Unit.PIECE);
            recipeIngredientRepository.save(p1);

            RecipeIngredient p2 = new RecipeIngredient();
            p2.setRecipe(pancakes);
            p2.setIngredient(milk);
            p2.setQuantity(200.0);
            p2.setUnit(Unit.ML);
            recipeIngredientRepository.save(p2);

            RecipeIngredient p3 = new RecipeIngredient();
            p3.setRecipe(pancakes);
            p3.setIngredient(flour);
            p3.setQuantity(150.0);
            p3.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(p3);

            RecipeIngredient p4 = new RecipeIngredient();
            p4.setRecipe(pancakes);
            p4.setIngredient(sugar);
            p4.setQuantity(30.0);
            p4.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(p4);

            Recipe omelette = new Recipe();
            omelette.setTitle("Omelette");
            omelette.setDescription("Egg omelette with cheese");
            omelette.setOwner(user);
            recipeRepository.save(omelette);

            RecipeIngredient o1 = new RecipeIngredient();
            o1.setRecipe(omelette);
            o1.setIngredient(egg);
            o1.setQuantity(3.0);
            o1.setUnit(Unit.PIECE);
            recipeIngredientRepository.save(o1);

            RecipeIngredient o2 = new RecipeIngredient();
            o2.setRecipe(omelette);
            o2.setIngredient(cheese);
            o2.setQuantity(50.0);
            o2.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(o2);

            RecipeIngredient o3 = new RecipeIngredient();
            o3.setRecipe(omelette);
            o3.setIngredient(butter);
            o3.setQuantity(10.0);
            o3.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(o3);

            Recipe pastaTomato = new Recipe();
            pastaTomato.setTitle("Pasta with Tomato");
            pastaTomato.setDescription("Boil pasta and add tomato sauce");
            pastaTomato.setOwner(user);
            recipeRepository.save(pastaTomato);

            RecipeIngredient t1 = new RecipeIngredient();
            t1.setRecipe(pastaTomato);
            t1.setIngredient(pasta);
            t1.setQuantity(200.0);
            t1.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(t1);

            RecipeIngredient t2 = new RecipeIngredient();
            t2.setRecipe(pastaTomato);
            t2.setIngredient(tomato);
            t2.setQuantity(3.0);
            t2.setUnit(Unit.PIECE);
            recipeIngredientRepository.save(t2);

            RecipeIngredient t3 = new RecipeIngredient();
            t3.setRecipe(pastaTomato);
            t3.setIngredient(oliveOil);
            t3.setQuantity(2.0);
            t3.setUnit(Unit.TABLESPOON);
            recipeIngredientRepository.save(t3);

            RecipeIngredient t4 = new RecipeIngredient();
            t4.setRecipe(pastaTomato);
            t4.setIngredient(salt);
            t4.setQuantity(1.0);
            t4.setUnit(Unit.TEASPOON);
            recipeIngredientRepository.save(t4);

            Recipe grilledCheese = new Recipe();
            grilledCheese.setTitle("Grilled Cheese");
            grilledCheese.setDescription("Toasted bread with melted cheese");
            grilledCheese.setOwner(user);
            recipeRepository.save(grilledCheese);

            RecipeIngredient g1 = new RecipeIngredient();
            g1.setRecipe(grilledCheese);
            g1.setIngredient(cheese);
            g1.setQuantity(80.0);
            g1.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(g1);

            RecipeIngredient g2 = new RecipeIngredient();
            g2.setRecipe(grilledCheese);
            g2.setIngredient(butter);
            g2.setQuantity(15.0);
            g2.setUnit(Unit.GRAM);
            recipeIngredientRepository.save(g2);
        };
    }
}
