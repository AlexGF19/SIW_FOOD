package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long>{

	@Query("SELECT r FROM Recipe r WHERE r.cucine.id = :cucineId")
	public Iterable<Recipe> getRecipeByCucine(Long cucineId);
	
	@Query("SELECT r FROM Recipe r WHERE r.vegan = true")
	public Iterable<Recipe> getVeganRecipe();
	
	@Query("SELECT r FROM Recipe r WHERE r.name=:name AND r.cook.credentials.username=:username")
	public Recipe getRecipeByNameAndUsername(String name, String username);
	
	@Query("UPDATE Recipe r SET r.name=:rName, r.description=:rDescription, r.vegan=:rVegan WHERE r.id=:rId")
	public void updateRecipe(Long rId, String rName, String rDescription, boolean rVegan);
	
	@Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(:param)")
	public Iterable<Recipe> searchRecipeQuery(@Param("param") String param);
}
