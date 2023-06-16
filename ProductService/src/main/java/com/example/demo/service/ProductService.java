package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PorductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor // will create constructor for each required properties/ /same as @autowired 
@Slf4j // for logging from lombok
public class ProductService {
	
	
	private final ProductRepository productRepo;  
	
	public void createPorduct(PorductRequest porductRequest) {
		Product product = Product.builder()
				.name(porductRequest.getName())
				.description(porductRequest.getDescription())
				.price(porductRequest.getPrice()).build();
		
		productRepo.save(product);
		
		log.info("product {} saved successfully !!", product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		
		List<Product> product = productRepo.findAll();	
		
		return product.stream().map(this::mapToProductResponse).toList();
		
	}

	
	//this method used to map the product with the product response dto class
	//it is bad practice to expose the entity class to the world thats why we need to use the dto class
	//fetching the details from the database it returns the entity class so need to map it to the response class.
	private ProductResponse mapToProductResponse( Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}

}
