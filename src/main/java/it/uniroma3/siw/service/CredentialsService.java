package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {
	@Autowired CredentialsRepository credentialsRepository;

	public boolean existsByUsername(String username) {
		return this.credentialsRepository.existsByUsername(username);
	}

	public void save(Credentials credentials) {
		this.credentialsRepository.save(credentials);
	}

	public Credentials getCredentials(String username) {
		return this.credentialsRepository.findByUsername(username);
	}
}
