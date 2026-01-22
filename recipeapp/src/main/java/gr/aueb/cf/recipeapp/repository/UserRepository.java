package gr.aueb.cf.recipeapp.repository;

import gr.aueb.cf.recipeapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
