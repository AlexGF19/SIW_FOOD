package it.uniroma3.siw.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.RecipeService;

@Component
public class RecipeValidator implements Validator{
	@Autowired RecipeService recipeService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Recipe recipe = (Recipe) o;
		String username = recipe.getCook().getCredentials().getUsername();
		if(recipe.getName() != null && username != null && this.recipeService.getRecipeByNameAndUsername(recipe.getName(), username) != null )
			errors.reject("recipe.duplicate");
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Recipe.class.equals(aClass);
	}
}
