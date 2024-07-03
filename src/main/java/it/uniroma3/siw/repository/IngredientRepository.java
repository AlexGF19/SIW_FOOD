package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long>{
	
	public boolean existsByName(String name);
	
	 @Query("SELECT i FROM Ingredient i WHERE i.id NOT IN " +
	           "(SELECT ri.ingredient.id FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId)")
	 public Iterable<Ingredient> findIngredientsNotInRecipe(@Param("recipeId") Long recipeId);
	
	 @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(:param)")
		public Iterable<Ingredient> searchIngredientQuery(@Param("param") String param);
	 
}
