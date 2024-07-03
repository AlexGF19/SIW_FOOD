package it.uniroma3.siw.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.configuration.AuthConfiguration;
import it.uniroma3.siw.model.Cook;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.CookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CookController {
	
	@Autowired CookService cookService;
	@Autowired CredentialsService credentialsService;
	@Autowired ImageService imageService;

	
	@GetMapping("/cook")
	public String getCooks(Model model) {
		Iterable<Cook> cooks = this.cookService.findAll();
		model.addAttribute("cooks", cooks);
		return "cooks.html";
	}
	
	@GetMapping("/cook/{id}")
	public String getCook(@PathVariable("id") Long id , @ModelAttribute("userDetails") UserDetails userDetails, Model model) {
		Cook cook = this.cookService.findById(id);
		if(userDetails!=null && cook.getCredentials().getUsername().equals(userDetails.getUsername()))
			return "redirect:/profile/" + userDetails.getUsername();
		model.addAttribute("cook", cook);
		if(userDetails!=null && this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())
			return "cookAdmin.html";
		return "cook.html";
	}
	
	@GetMapping("/deleteCook/{id}")
	public String deleteCook(@PathVariable("id") Long id , @ModelAttribute("userDetails") UserDetails userDetails) {
		Cook cook = this.cookService.findById(id);
		if(userDetails!=null && (cook.getCredentials().getUsername().equals(userDetails.getUsername())
				|| this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())) {
			this.cookService.delete(cook);
		}
		return "redirect:/logout";
	}
	
	@GetMapping("/editCook/{id}")
	public String editCook(@PathVariable("id") Long id , @ModelAttribute("userDetails") UserDetails userDetails, Model model) {
		Cook cook = this.cookService.findById(id);
		if(userDetails!=null && (cook.getCredentials().getUsername().equals(userDetails.getUsername())
				|| this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())) {
			model.addAttribute("cook", cook);
			return "editCookForm.html";
		}
		return "redirect:/";
	}
	
	@PostMapping("/editCook/{id}")
	public String editCookForm(@Valid @ModelAttribute("cook") Cook tempCook, @ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id) {
		Cook cook = this.cookService.findById(id);
		cook.setName(tempCook.getName());
		cook.setSurname(tempCook.getSurname());
		LocalDate birthDate = tempCook.getBirthDate();
		if(birthDate!=null && !(cook.getBirthDate().equals(birthDate))) {
			cook.setBirthDate(birthDate);
		}
		this.cookService.save(cook);
		return "redirect:/cook/" + id;
	}
	
	@GetMapping("/addProfilePic/{id}")
	public String addProfilePic(@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model  model) {
		Cook cook = this.cookService.findById(id);
		if(userDetails!=null && (cook.getCredentials().getUsername().equals(userDetails.getUsername())
				|| this.credentialsService.getCredentials(userDetails.getUsername()).isAdmin())) {
			model.addAttribute("cook", cook);
			return "profilePicForm.html";
		}
		return "redirect:/";
	}
	
	@PostMapping("/addProfilePic/{id}")
	public String addProfilePicForm(HttpServletRequest request, @RequestParam("image") MultipartFile file, 
			@ModelAttribute("userDetails") UserDetails userDetails, @PathVariable("id") Long id, Model model)throws IOException, SerialException, SQLException{

	     Image image = this.imageService.save(file);
	     
	     Cook cook = this.cookService.findById(id);
	     Image oldImage = cook.getImage();
	     cook.setImage(image);
	     this.cookService.save(cook);
	     if(oldImage!=null)
			this.imageService.delete(oldImage);
	     return "redirect:/profile/" + userDetails.getUsername();
	}
	
	
}
