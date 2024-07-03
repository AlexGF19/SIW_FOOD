package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Cook;
import it.uniroma3.siw.repository.CookRepository;

@Service
public class CookService {
	@Autowired CookRepository cookRepository;

	public Iterable<Cook> findAll() {
		return this.cookRepository.findAll();
	}
	
	public Cook findById(Long id) {
		return this.cookRepository.findById(id).get();
	}
	
	public void save(Cook cook) {
		this.cookRepository.save(cook);
	}
	
	public void delete(Cook cook) {
		this.cookRepository.delete(cook);
	}
	
	public Iterable<Cook> searchCookQuery(String param){
		return this.cookRepository.searchCookQuery("%" + param + "%");
	}
	
}
