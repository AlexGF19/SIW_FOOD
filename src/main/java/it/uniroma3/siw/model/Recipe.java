 package it.uniroma3.siw.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "cook_id"}))
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	@Column(length = 10000)
	private String description;
	
	@Column(nullable=true)
	private boolean vegan; 
	
	//type of cucine (italian, japanese etc...)
	@ManyToOne
	private Cucine cucine;
	
	@OneToMany(cascade = {CascadeType.REMOVE})
	private List<Image> images; 
	

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	private Cook cook;
	
	@OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE})
	private List<RecipeIngredient> Ingredients;
	
	

	
	//--------------------------------------------------------------------------
	
	
	public boolean isVegan() {
		return vegan;
	}

	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}

	public Cucine getCucine() {
		return cucine;
	}

	public void setCucine(Cucine cucine) {
		this.cucine = cucine;
	}

	public Long getId() {
		return id;
	}
	
	public List<RecipeIngredient> getIngredients() {
		return Ingredients;
	}

	public void setIngredients(List<RecipeIngredient> ingredients) {
		Ingredients = ingredients;
	}

	public Cook getCook() {
		return cook;
	}

	public void setCook(Cook cook) {
		this.cook = cook;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

}
