package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Cucine;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.CucineRepository;

@Service
public class CucineService {
	@Autowired CucineRepository cucineRepository;
	
	public Iterable<Cucine> findAll(){
		return this.cucineRepository.findAll();
	}
	
	public Cucine findById(Long id) {
		return this.cucineRepository.findById(id).get();
	}
	
	public void save(Cucine cucine) {
		this.cucineRepository.save(cucine);
	}
	
	public void delete(Cucine cucine) {
		this.cucineRepository.delete(cucine);
	}
	
	public boolean existsByName(String name) {
		return this.cucineRepository.existsByName(name);
	}
	
	public Iterable<Cucine> findCucinesNotInRecipe(Long id){
		return this.cucineRepository.findCucinesNotInRecipe(id);
	}
}
