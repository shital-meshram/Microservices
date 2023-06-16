package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.demo.dto.PorductRequest;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	
	@Container
	static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:6.0.6");
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductRepository productRepo;
	
	
	@DynamicPropertySource
	static void setProperties (DynamicPropertyRegistry dynPropertyRegistry) {
		dynPropertyRegistry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
	}

	@Test
	void souldCreateProduct() throws Exception {
		PorductRequest productreq = getProductRquest();
		
		String productReqString = objectMapper.writeValueAsString(productreq); 
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productReqString)
				)
		.andExpect(status().isCreated());
		
		Assertions.assertTrue(productRepo.findAll().size()==1);
		
	}
	
	public PorductRequest getProductRquest() {
		return PorductRequest.builder()
				.name("Samsung A30")
				.description("wide angle camera feature")
				.price("150000")
				.build();
	}

}
