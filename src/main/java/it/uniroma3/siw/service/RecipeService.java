package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;

@Service
public class RecipeService {
	@Autowired RecipeRepository recipeRepository;
	
	public Iterable<Recipe> findAll(){
		return this.recipeRepository.findAll();
	}

	public Recipe findById(Long id) {
		return this.recipeRepository.findById(id).get();
	}
	
	public void save(Recipe recipe) {
		this.recipeRepository.save(recipe);
	}
	
	public void delete(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}
	
	public Iterable<Recipe> getRecipeByCucine(Long cucineId){
		return this.recipeRepository.getRecipeByCucine(cucineId);
	}
	
	public Iterable<Recipe> getVeganRecipe(){
		return this.recipeRepository.getVeganRecipe();
	}
	
	public Recipe getRecipeByNameAndUsername(String name, String username) {
		return this.recipeRepository.getRecipeByNameAndUsername(name, username);
	}

	public void update(Recipe recipe) {
		this.recipeRepository.updateRecipe(recipe.getId(), recipe.getName(), recipe.getDescription(), recipe.isVegan());
	}
	
	public Iterable<Recipe> searchRecipeQuery(String param){
		return this.recipeRepository.searchRecipeQuery("%" + param + "%");
	}
	
}
