package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    List<UserIngredient> findByUserId(Long userId);
}
