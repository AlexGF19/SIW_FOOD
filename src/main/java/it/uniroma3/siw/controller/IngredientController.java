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

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.validation.IngredientValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class IngredientController {
	@Autowired IngredientService ingredientService;
	@Autowired CredentialsService credentialsService;
	@Autowired ImageService imageService;
	
	@Autowired IngredientValidator ingredientValidator;
	
	@GetMapping("/ingredient")
	public String getIngredients(Model model) {
		Iterable<Ingredient> ingredients = this.ingredientService.findAll();
		model.addAttribute("ingredients", ingredients);
		return "ingredients.html";
	}
	
	@GetMapping("/ingredient/{id}")
	public String getIngredient(@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model model) {
		Ingredient ingredient = this.ingredientService.findById(id);
		model.addAttribute("ingredient", ingredient);
		if(userDetails!=null) {
			Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
			if(credentials.isAdmin())
				return "ingredientAdmin.html";
		}
		return "ingredient.html";

	}
	
	@GetMapping("/newIngredient")
	public String newIngredient(Model model) {
		Ingredient ingreddient = new Ingredient();
		model.addAttribute("ingredient", ingreddient);
		return "newIngredientForm.html";
	}
	
	@PostMapping("/newIngredient")
	public String newIngredientPost(@Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult, Model model) {
		this.ingredientValidator.validate(ingredient, bindingResult);
		if(bindingResult.hasErrors())
			return "newIngredientForm.html";
		else {
			this.ingredientService.save(ingredient);
			return "redirect:/addIngredientImage/" + ingredient.getId();
		}
	}
	
	@GetMapping("/admin/deleteIngredient/{id}")
	public String deleteIngredient(@PathVariable("id") Long id) {
	    Ingredient ingredient =	this.ingredientService.findById(id);
	    this.ingredientService.delete(ingredient);
	    return "redirect:/";
	}
	
	@GetMapping("/admin/editIngredient/{id}")
	public String editIngredient(@PathVariable("id") Long id, Model model) {
		Ingredient ingredient =	this.ingredientService.findById(id);
		model.addAttribute("ingredient", ingredient);
		return "editIngredientForm.html";
	}
	
	@PostMapping("/admin/editIngredient/{id}")
	public String editIngredient(@ModelAttribute("ingredient") Ingredient tempIngredient, BindingResult bindingResult, @PathVariable("id") Long id) {
		Ingredient ingredient =	this.ingredientService.findById(id);
		ingredient.setName(tempIngredient.getName());
		this.ingredientValidator.validate(ingredient, bindingResult);
		if(bindingResult.hasErrors())
			return "editIngredientForm.html";
		this.ingredientService.save(ingredient);
		return "redirect:/ingredient/" + id;
	}
	
	@GetMapping("/addIngredientImage/{id}")
	public String addIngredientImage(@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model  model) {
		model.addAttribute("ingredient", this.ingredientService.findById(id));
		return "ingredientImageForm.html";
	}
	
	@PostMapping("/addIngredientImage/{id}")
	public String addIngredientImage(HttpServletRequest request, @RequestParam("image") MultipartFile file, 
		@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model model)throws IOException, SerialException, SQLException{
		Image image = this.imageService.save(file);
		Ingredient ingredient =	this.ingredientService.findById(id);
		if(ingredient.getImage()!=null) 
			this.imageService.delete(ingredient.getImage());
		ingredient.setImage(image);
		this.ingredientService.save(ingredient);
		return "redirect:/ingredient/" + id;
	}
}
