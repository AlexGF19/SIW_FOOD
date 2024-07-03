package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cucine;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;

public interface CucineRepository extends CrudRepository<Cucine, Long>{
	
	public boolean existsByName(String name);
	
	@Query("SELECT c FROM Cucine c WHERE c.id NOT IN " +
	           "(SELECT r.cucine.id FROM Recipe r WHERE r.id = :recipeId)")
	 public Iterable<Cucine> findCucinesNotInRecipe(@Param("recipeId") Long recipeId);
	
}
