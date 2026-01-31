package gr.aueb.cf.recipeapp.dto;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatchDTO {

    private Long recipeId;
    private String title;
    private String description;

    private int haveCount;
    private int totalIngredients;

    private List<String> missing = new ArrayList<>();
    private int missingCount;

    private List<String> matched = new ArrayList<>();

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHaveCount() {
        return haveCount;
    }

    public void setHaveCount(int haveCount) {
        this.haveCount = haveCount;
    }

    public int getTotalIngredients() {
        return totalIngredients;
    }

    public void setTotalIngredients(int totalIngredients) {
        this.totalIngredients = totalIngredients;
    }

    public List<String> getMissing() {
        return missing;
    }

    public void setMissing(List<String> missing) {
        this.missing = missing == null ? new ArrayList<>() : missing;
    }

    public int getMissingCount() {
        return missingCount;
    }

    public void setMissingCount(int missingCount) {
        this.missingCount = missingCount;
    }

    public List<String> getMatched() {
        return matched;
    }

    public void setMatched(List<String> matched) {
        this.matched = matched == null ? new ArrayList<>() : matched;
    }
}
