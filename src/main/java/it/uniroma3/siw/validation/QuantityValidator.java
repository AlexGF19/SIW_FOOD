package it.uniroma3.siw.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.RecipeIngredient;

@Component
public class QuantityValidator implements Validator{
	private int maxQuantity = 10000;
	
	@Override
	public void validate(Object o, Errors errors) {
		RecipeIngredient recipeIngredient = (RecipeIngredient) o;
		int quantity = recipeIngredient.getQuantity();
		if(quantity >= maxQuantity)
			errors.reject("max.quantity");
		if(quantity < 0)
			errors.reject("min.quantity");
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return RecipeIngredient.class.equals(aClass);
	}
	
	//soluzione un po brutta da vedere, non riesco atrovare ilmotivo per cui non funzioni l'altra
	public String validateQuantity(RecipeIngredient recipeIngredient) {
		int quantity = recipeIngredient.getQuantity();
		if(quantity >= maxQuantity)
			return "too much, the limit is 10000grams";
		if(quantity < 0)
			return "quantity must be a positive number";
		return null;
	}
}
