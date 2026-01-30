package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.Recipe;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
    List<Recipe> findAll(Sort sort);

    @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
    List<Recipe> findByOwnerUsernameOrderByIdDesc(String username);

    @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
    Optional<Recipe> findByIdAndOwnerUsername(Long id, String username);
}
