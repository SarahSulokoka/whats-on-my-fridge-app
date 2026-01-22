package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
