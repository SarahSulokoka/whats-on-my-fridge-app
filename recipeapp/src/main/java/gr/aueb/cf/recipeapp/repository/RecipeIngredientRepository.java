package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    @Modifying
    @Transactional
    @Query("delete from RecipeIngredient ri where ri.recipe.id = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Long recipeId);
}



