package gr.aueb.cf.recipeapp.dto;

import java.util.List;

public class RecipeMatchDTO {

    private Long recipeId;
    private String title;
    private String description;

    private int totalIngredients;
    private int haveCount;
    private int missingCount;

    private List<String> missing;

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getTotalIngredients() { return totalIngredients; }
    public void setTotalIngredients(int totalIngredients) { this.totalIngredients = totalIngredients; }

    public int getHaveCount() { return haveCount; }
    public void setHaveCount(int haveCount) { this.haveCount = haveCount; }

    public int getMissingCount() { return missingCount; }
    public void setMissingCount(int missingCount) { this.missingCount = missingCount; }

    public List<String> getMissing() { return missing; }
    public void setMissing(List<String> missing) { this.missing = missing; }
}
