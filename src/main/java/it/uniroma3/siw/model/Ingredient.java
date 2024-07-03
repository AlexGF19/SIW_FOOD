package it.uniroma3.siw.model;

import java.util.List;
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
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "ingredient", cascade = {CascadeType.REMOVE})
	private List<RecipeIngredient> recipes;
	
	@OneToOne(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Image image;
	
	
	
	//------------------------------------------------------------------------------

	
	
	
	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public long getId() {
		return id;
	}

	
	public List<RecipeIngredient> getRecipes() {
		return recipes;
	}


	public void setRecipes(List<RecipeIngredient> recipes) {
		this.recipes = recipes;
	}


	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		Ingredient other = (Ingredient) obj;
		return id == other.id && Objects.equals(name, other.name);
	}
	
	
}
