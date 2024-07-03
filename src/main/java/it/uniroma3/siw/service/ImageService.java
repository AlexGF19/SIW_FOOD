package it.uniroma3.siw.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;



@Service
public class ImageService {
    @Autowired 
    private ImageRepository imageRepository;

    public Image save(MultipartFile file) throws IOException {
    	byte[] bytes = file.getBytes();
	     Image image = new Image();
	     image.setImage(bytes);
	     image.setName(file.getOriginalFilename());
        return this.imageRepository.save(image);
    }

    public void delete(Image image) {
    	this.imageRepository.delete(image);
    }
    
    public Image findById(long id) {
        return imageRepository.findById(id).get();
    }
    
    
    
}

