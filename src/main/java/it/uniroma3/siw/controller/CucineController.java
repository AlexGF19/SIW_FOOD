package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Cucine;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CucineService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.validation.CucineValidator;
import jakarta.validation.Valid;

@Controller
public class CucineController {

	@Autowired CucineService cucineService;
	@Autowired RecipeService recipeService;
	@Autowired CredentialsService credentialsService;
	
	@Autowired CucineValidator cucineValidator;
	
	
	
	
	@GetMapping("/cucine")
	public String getCucines(Model model) {
		Iterable<Cucine> cucines = this.cucineService.findAll();
		model.addAttribute("cucines", cucines);
		return "cucines.html";
	}
	
	@GetMapping("/cucine/{id}")
	public String getCucine(@PathVariable("id") Long id, UserDetails userDetails, Model model) {
		Cucine cucine = this.cucineService.findById(id);
		model.addAttribute("cucine", cucine);
		Iterable<Recipe> recipes = this.recipeService.getRecipeByCucine(id);
		model.addAttribute("recipes", recipes);
		if(userDetails!=null && this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())
			return "cucineAdmin.html";
		return "cucine.html";
	}

	
	

	@GetMapping("/admin/newCucine")
	public String newCucine(Model model) {
		Cucine cucine = new Cucine();
		model.addAttribute("cucine", cucine);
		return "newCucineForm.html";
	}
	
	@PostMapping("/newCucine")
	public String newCucinePost(@Valid @ModelAttribute("cucine") Cucine cucine , BindingResult bindingResult, Model model) {
		this.cucineValidator.validate(cucine, bindingResult);
		if(bindingResult.hasErrors())
			return "newCucineForm.html";
		else {
			this.cucineService.save(cucine);
			return "redirect:/cucine/" + cucine.getId();
		}
	}
	
	
	@GetMapping("/admin/deleteCucine/{id}")
	public String deleteCucine(@PathVariable("id") Long id) {
		Cucine cucine = this.cucineService.findById(id);
		ArrayList<Recipe> recipes = (ArrayList<Recipe>) this.recipeService.getRecipeByCucine(id);
		Iterator<Recipe> it = recipes.iterator();
		while(it.hasNext()) {
			Recipe r = it.next();
			r.setCucine(null);
			this.recipeService.save(r);
		}
		this.cucineService.delete(cucine);
		return "redirect:/";
	}
	
	@GetMapping("/admin/editCucine/{id}")
	public String deleteCucine(@PathVariable("id") Long id, Model model) {
		Cucine cucine = this.cucineService.findById(id);
		model.addAttribute("cucine", cucine);
		return "editCucineForm.html";
	}
	
	@PostMapping("/admin/editCucine/{id}")
	public String deleteCucine(@PathVariable("id") Long id, @Valid @ModelAttribute("cucine") Cucine tempCucine, BindingResult bindingResult) {
		Cucine cucine = this.cucineService.findById(id);
		cucine.setName(tempCucine.getName());
		this.cucineValidator.validate(cucine, bindingResult);
		if(bindingResult.hasErrors())
			return "editCucineForm.html";
		this.cucineService.save(cucine);
		return "redirect:/cucine/" + id;
	}
}
