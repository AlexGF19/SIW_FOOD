package it.uniroma3.siw.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Cucine;
import it.uniroma3.siw.service.CucineService;

@Component
public class CucineValidator implements Validator{
	
	@Autowired CucineService cucineService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Cucine cucine = (Cucine)o;
		if(cucine.getName() != null && this.cucineService.existsByName(cucine.getName()))
			errors.reject("cucine.duplicate");
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Cucine.class.equals(aClass);
	}
}
