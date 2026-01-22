package gr.aueb.cf.recipeapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FridgeSearchDTO {

    @NotEmpty
    private List<String> ingredients;
}
