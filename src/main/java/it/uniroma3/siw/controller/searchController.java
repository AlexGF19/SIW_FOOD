package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Cook;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.CookService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;

@Controller
public class searchController {
	
	@Autowired CookService cookService;
	@Autowired RecipeService recipeService;
	@Autowired IngredientService ingredientService;
	
	@GetMapping("/searchCook")
	public String searchCook(@RequestParam("prefix") String prefix, Model model) {
		Iterable<Cook> cooks = this.cookService.searchCookQuery(prefix);
		model.addAttribute("cooks", cooks);
		model.addAttribute("prefix", prefix);
		return "searchResultsCook.html";
	}
	
	@GetMapping("/searchRecipe")
	public String searchRecipe(@RequestParam("prefix") String prefix, Model model) {
		Iterable<Recipe> recipes = this.recipeService.searchRecipeQuery(prefix);
		model.addAttribute("recipes", recipes);
		model.addAttribute("prefix", prefix);
		return "searchResultsRecipe.html";
	}
	
	@GetMapping("/searchIngredient")
	public String searchIngredient(@RequestParam("prefix") String prefix, Model model) {
		Iterable<Ingredient> ingredients = this.ingredientService.searchIngredientQuery(prefix);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("prefix", prefix);
		return "searchResultsIngredient.html";
	}
	
}
