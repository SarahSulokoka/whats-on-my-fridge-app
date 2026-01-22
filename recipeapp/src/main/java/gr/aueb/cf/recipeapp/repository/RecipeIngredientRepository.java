package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
