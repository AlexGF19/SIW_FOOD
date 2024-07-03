package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cook;

public interface CookRepository extends CrudRepository<Cook, Long>{
	
	@Query("SELECT c FROM Cook c WHERE LOWER(c.name) LIKE LOWER(:param)")
	public Iterable<Cook> searchCookQuery(@Param("param") String param);

}
