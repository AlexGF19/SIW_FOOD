package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.RecipeIngredient;
import it.uniroma3.siw.repository.RecipeIngredientRepository;

@Service
public class RecipeIngredientService {
	@Autowired RecipeIngredientRepository recipeIngredientRepository;
	
	public void save(RecipeIngredient recipeIngredient) {
		this.recipeIngredientRepository.save(recipeIngredient);
	}
	
	public void delete(RecipeIngredient recipeIngredient) {
		this.recipeIngredientRepository.delete(recipeIngredient);
	}
	
	public RecipeIngredient findById(long id) {
		return this.recipeIngredientRepository.findById(id).get();
	}
	
	public RecipeIngredient findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient) {
		return this.recipeIngredientRepository.findByRecipeAndIngredient(recipe, ingredient);
	}
}
