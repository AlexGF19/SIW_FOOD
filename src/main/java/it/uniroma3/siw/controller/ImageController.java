package it.uniroma3.siw.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.ImageService;

@Controller
public class ImageController {
	@Autowired ImageService imageService;
	
	@GetMapping("/image/{id}")
	 public ResponseEntity<byte[]> displayImage(@PathVariable("id") long id) throws IOException, SQLException {
        Image image = imageService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getImage());
    }
	
	
}
