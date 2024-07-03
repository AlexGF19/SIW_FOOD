package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Service
public class IngredientService {
	@Autowired IngredientRepository ingredientRepository;
	
	public Iterable<Ingredient> findAll(){
		return this.ingredientRepository.findAll();
	}
	
	public Ingredient findById(Long id) {
		return this.ingredientRepository.findById(id).get();
	}
	
	public void save(Ingredient ingredient) {
		this.ingredientRepository.save(ingredient);
	}
	
	public void delete(Ingredient ingredient) {
		this.ingredientRepository.delete(ingredient);
	}
	
	public boolean existsByName(String name) {
		return this.ingredientRepository.existsByName(name);
	}
	
	public Iterable<Ingredient> findIngredientsNotInRecipe(Long recipeId){
		return this.ingredientRepository.findIngredientsNotInRecipe(recipeId);
	}
	
	public Iterable<Ingredient> searchIngredientQuery(String param){
		return this.ingredientRepository.searchIngredientQuery("%" + param + "%");
	}
}
