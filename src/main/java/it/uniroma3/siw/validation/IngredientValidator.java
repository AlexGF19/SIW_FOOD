package it.uniroma3.siw.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;


@Component
public class IngredientValidator implements Validator{
	
	@Autowired IngredientService ingredientService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Ingredient ingredient = (Ingredient) o;
		if(ingredient.getName() != null && this.ingredientService.existsByName(ingredient.getName()))
				errors.reject("ingredient.duplicate");
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Ingredient.class.equals(aClass);
	}
}
