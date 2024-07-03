package it.uniroma3.siw.controller;


import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.configuration.AuthConfiguration;
import it.uniroma3.siw.model.Cook;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cucine;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.RecipeIngredient;
import it.uniroma3.siw.service.CookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CucineService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeIngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.validation.QuantityValidator;
import it.uniroma3.siw.validation.RecipeValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class RecipeController {
	
	@Autowired RecipeService recipeService;
	@Autowired CookService cookService;
	@Autowired CucineService cucineService;
	@Autowired IngredientService ingredientService;
	@Autowired RecipeIngredientService recipeIngredientService;
	@Autowired CredentialsService credentialsService;
	@Autowired ImageService imageService;
	
	@Autowired RecipeValidator recipeValidator;
	@Autowired QuantityValidator quantityValidator;
	
	
	
	
	
	
	@GetMapping("/recipe")
	public String getRecipes(Model model) {
		Iterable<Recipe> recipes = this.recipeService.findAll();
		model.addAttribute("recipes", recipes);
		return "recipes.html";
	}
	
	@GetMapping("/recipe/{id}")
	public String getRecipe(@ModelAttribute("userDetails") UserDetails userDetails,@PathVariable("id") Long id ,Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		if(userDetails!=null) {
			String username = userDetails.getUsername();
			if(recipe.getCook().getCredentials().getUsername().equals(username) || this.credentialsService.getCredentials(username).isAdmin())
				return "myRecipe.html";
		} 
		return "recipe.html";
	}
	
	/*--------------------------images----------------------------------------*/
	@GetMapping("/addRecipeImage/{id}")
	public String addRecipeImage(@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model  model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		return "recipeImageForm.html";
	}
	
	@PostMapping("/addRecipeImage/{id}")
	public String addRecipeImage(HttpServletRequest request, @RequestParam("image") MultipartFile file, 
		@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model model)throws IOException, SerialException, SQLException{
		Image image = this.imageService.save(file);
		Recipe recipe = this.recipeService.findById(id);
		recipe.getImages().add(image);
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);
		return "recipeImageForm.html";
	}

	@GetMapping("/deleteRecipeImage/{id}")
	public String deleteRecipeImages(@PathVariable("id") Long id, Model  model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "recipeImages.html";
	}
	
	@GetMapping("/deleteRecipeImage/{recipeId}/{imageId}")
	public String deleteRecipeImage(@PathVariable("recipeId") Long recipeId, @PathVariable("imageId") Long imageId) {
		Image image = this.imageService.findById(imageId);
		Recipe recipe = this.recipeService.findById(recipeId);
		recipe.getImages().remove(image);
		this.recipeService.save(recipe);
		this.imageService.delete(image);
		return "redirect:/deleteRecipeImage/" + recipeId;
	}
	
	/*------------------------------------------------------------------------*/
	
	
	
	
	
	/*--------------adding a new recipe---------------------------------------*/
	@GetMapping("/newRecipe")
	public String newRecipe(Model model) {
		Recipe recipe = new Recipe();
		model.addAttribute("recipe", recipe);
		return "newRecipeForm.html";
	}
	
	@PostMapping("/newRecipe")
	public String newRecipePost(@Valid @ModelAttribute("recipe")Recipe recipe, BindingResult bindingResult, 
			@ModelAttribute("userDetails") UserDetails userDetails, Model model) {
		Cook cook = this.credentialsService.getCredentials(userDetails.getUsername()).getCook();
		recipe.setCook(cook);
		this.recipeValidator.validate(recipe, bindingResult);
		if(bindingResult.hasErrors())
			return "newRecipeForm.html";
		this.recipeService.save(recipe);
		cook.getRecipes().add(recipe);
		this.cookService.save(cook);
		model.addAttribute("recipe", recipe);
		return "redirect:/newRecipe/setCucine/" + recipe.getId();
	}
	
	@GetMapping("/newRecipe/setCucine/{recipeId}")
	public String getCucineNewRecipe(@PathVariable("recipeId") Long recipeId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		model.addAttribute("recipe", recipe);
		Iterable<Cucine> cucines = this.cucineService.findAll();
		model.addAttribute("cucines", cucines);
		return "setCucineNewRecipe.html";
	}
	
	@GetMapping("/newRecipe/setCucine/{recipeId}/{cucineId}")
	public String setCucineNewRecipe(@PathVariable("cucineId") Long cucineId, @PathVariable("recipeId") Long recipeId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		if(cucineId != -1) {
			Cucine cucine = this.cucineService.findById(cucineId);
			recipe.setCucine(cucine);
			this.recipeService.save(recipe);
		}
		return "redirect:/newRecipe/setIngredient/" + recipe.getId();
	}
	
	@GetMapping("/newRecipe/setIngredient/{recipeId}")
	public String getIngredientNewRecipe(@PathVariable("recipeId") Long recipeId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		Iterable<Ingredient> ingredients = this.ingredientService.findIngredientsNotInRecipe(recipeId);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("recipe", recipe);
		return "setIngredientNewRecipe.html";
	}
	
	@GetMapping("/removeIngredientNewRecipe/{recipeId}/{recipeIngredientId}")
	public String removeIngredientNewRecipe(@PathVariable("recipeId") Long recipeId, @PathVariable("recipeIngredientId") Long recipeIngredientId, Model model) {
		RecipeIngredient recipeIngredient = this.recipeIngredientService.findById(recipeIngredientId);
		this.recipeIngredientService.delete(recipeIngredient);
		return "redirect:/newRecipe/setIngredient/" + recipeId;
	}
	
	@GetMapping("/newRecipe/setIngredient/{recipeId}/{ingredientId}")
	public String setIngredientNewRecipe(@PathVariable("recipeId") Long recipeId, @PathVariable("ingredientId") Long ingredientId, @ModelAttribute("userDetails") UserDetails userDetails, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		if(ingredientId!=-1) {
			Ingredient ingredient = this.ingredientService.findById(ingredientId);
			
			RecipeIngredient recipeIngredient = new RecipeIngredient();
			recipeIngredient.setIngredient(ingredient);
			recipeIngredient.setRecipe(recipe);
			this.recipeIngredientService.save(recipeIngredient);
			
			ingredient.getRecipes().add(recipeIngredient);
			this.ingredientService.save(ingredient);
			
			recipe.getIngredients().add(recipeIngredient);
			this.recipeService.save(recipe);
			
			model.addAttribute("recipeIngredient", recipeIngredient);
			model.addAttribute("recipe", recipe);
			
			return "setQuantityIngredientNewRecipeForm.html";
		}
		if(this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())
			return "redirect:/admin/setCookNewRecipe/" + recipeId;
		return "redirect:/addRecipeImage/" + recipeId;
	}
	
	@PostMapping("/newRecipe/setIngredient/setQuantity/{recipeId}/{recipeIngredientId}")
	public String setQuantitySetIngredientNewRecipe(@Valid @ModelAttribute("recipeIngredient")RecipeIngredient tempRecipeIngredient, BindingResult bindingResult, 
			@PathVariable("recipeIngredientId") Long recipeIngredientId, @PathVariable("recipeId") Long recipeId, Model model) {
		
		RecipeIngredient recipeIngredient = this.recipeIngredientService.findById(recipeIngredientId);
		recipeIngredient.setQuantity(tempRecipeIngredient.getQuantity());
		
		this.quantityValidator.validate(recipeIngredient, bindingResult);
		if(bindingResult.hasErrors()) {
			Recipe recipe = this.recipeService.findById(recipeId);
			model.addAttribute("recipe", recipe);
			model.addAttribute("recipeIngredient", recipeIngredient);
			return "setQuantityIngredientNewRecipeForm.html";
		}
		
		this.recipeIngredientService.save(recipeIngredient);
		
		return"redirect:/newRecipe/setIngredient/" + recipeId;
	}
	
	@GetMapping("/admin/setCookNewRecipe/{id}")
	public String getCookNewRecipe(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		model.addAttribute("cooks", this.cookService.findAll());
		return "setCookNewRecipe.html";
	}
	
	@GetMapping("/admin/setCookNewRecipe/{recipeId}/{cookId}")
	public String setCookNewRecipe(@PathVariable("recipeId") Long recipeId, @PathVariable("cookId") Long cookId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		Cook cook = this.cookService.findById(cookId);
		recipe.setCook(cook);
		cook.getRecipes().add(recipe);
		this.recipeService.save(recipe);
		return "redirect:/addRecipeImage/" + recipeId;
	}
	/*------------------------------------------------------------------------*/
	
	
	
	
	@GetMapping("/deleteRecipe/{id}")
	public String deleteRecipe(@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		this.recipeService.delete(recipe);
		return "redirect:/profile/" + userDetails.getUsername();
	}
	
	
	
	
	/*--------------editing a recipe---------------------------------------*/

	@GetMapping("/editRecipe/{id}")
	public String editRecipes(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "editRecipe.html";
	}
	
	@GetMapping("/editRecipeForm/{id}")
	public String editRecipe(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "editRecipeForm.html";
	}
	
	@PostMapping("/editRecipeForm/{id}")
	public String editRecipe(@Valid @ModelAttribute("recipe")Recipe tRecipe, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		if(!(recipe.getName().equals(tRecipe.getName()))) {
			recipe.setName(tRecipe.getName());
			this.recipeValidator.validate(recipe, bindingResult);
			if(bindingResult.hasErrors()) {
				model.addAttribute("recipe", recipe);
				return "editRecipeForm.html";
			}
		}
		recipe.setDescription(tRecipe.getDescription());
		recipe.setVegan(tRecipe.isVegan());
		this.recipeService.save(recipe);
		return "redirect:/editRecipe/" + recipe.getId();
	}
	
	@GetMapping("/editRecipeCucine/{id}")
	public String editRecipeCucine(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		if(recipe.getCucine()==null)
			model.addAttribute("cucines", this.cucineService.findAll());
		else
			model.addAttribute("cucines", this.cucineService.findCucinesNotInRecipe(id));
		return "editRecipeCucine.html";
	}
	
	@GetMapping("/editRecipeCucine/{recipeId}/{cucineId}")
	public String setNewRecipeCucine(@PathVariable("recipeId") Long recipeId, @PathVariable("cucineId") Long cucineId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		Cucine cucine = this.cucineService.findById(cucineId);
		recipe.setCucine(cucine);
		this.recipeService.save(recipe);
		return "redirect:/editRecipe/" + recipeId;
	}
	
	@GetMapping("/editRecipeIngredient/{id}")
	public String editRecipeIngredient(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findIngredientsNotInRecipe(id));
		return "editRecipeIngredient.html";
	}
	
	@GetMapping("/removeIngredientRecipe/{recipeId}/{recipeIngredientId}")
	public String removeIngredientRecipe(@PathVariable("recipeId") Long recipeId, @PathVariable("recipeIngredientId") Long recipeIngredientId, Model model) {
		RecipeIngredient recipeIngredient = this.recipeIngredientService.findById(recipeIngredientId);
		this.recipeIngredientService.delete(recipeIngredient);
		return "redirect:/editRecipeIngredient/" + recipeId;
	}
	
	@GetMapping("/addIngredientRecipe/{recipeId}/{ingredientId}")
	public String addIngredientRecipe(@PathVariable("recipeId") Long recipeId, @PathVariable("ingredientId") Long ingredientId, Model model) {
		Recipe recipe = this.recipeService.findById(recipeId);
		Ingredient ingredient = this.ingredientService.findById(ingredientId);
		RecipeIngredient recipeIngredient = new RecipeIngredient();
		recipeIngredient.setRecipe(recipe);
		recipeIngredient.setIngredient(ingredient);
		recipe.getIngredients().add(recipeIngredient);
		ingredient.getRecipes().add(recipeIngredient);
		this.recipeIngredientService.save(recipeIngredient);
		model.addAttribute("recipe", recipe);
		model.addAttribute("recipeIngredient", recipeIngredient);
		return "setQuantityIngredientEditRecipeForm.html";
	}
	
	@PostMapping("/setQuantityIngredientEditRecipe/{recipeId}/{recipeIngredientId}")
	public String setQuantityIngredientEditRecipe(@Valid @ModelAttribute("recipeIngredient")RecipeIngredient tempRecipeIngredient, BindingResult bindingResult, 
			@PathVariable("recipeId") Long recipeId, @PathVariable("recipeIngredientId") Long recipeIngredientId, Model model) {
		
		RecipeIngredient recipeIngredient = this.recipeIngredientService.findById(recipeIngredientId);
		recipeIngredient.setQuantity(tempRecipeIngredient.getQuantity());
		
		this.quantityValidator.validate(recipeIngredient, bindingResult);
		if(bindingResult.hasErrors()) {
			Recipe recipe = this.recipeService.findById(recipeId);
			model.addAttribute("recipe", recipe);
			model.addAttribute("recipeIngredient", recipeIngredient);
			return "setQuantityIngredientEditRecipeForm.html";
		}
		
		this.recipeIngredientService.save(recipeIngredient);
		return "redirect:/editRecipeIngredient/" + recipeId;
	}
	/*------------------------------------------------------------------------*/

	
	@GetMapping("/vegan")
	public String getVeganRecipe(Model model) {
		Iterable<Recipe> recipes = this.recipeService.getVeganRecipe();
		model.addAttribute("recipes", recipes);
		return "veganRecipes.html";
	}
	
}
