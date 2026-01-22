package gr.aueb.cf.recipeapp.model.entity;

import gr.aueb.cf.recipeapp.model.enums.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_ingredients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ingredient_id"}))
@Getter
@Setter
@NoArgsConstructor
public class UserIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;
}
