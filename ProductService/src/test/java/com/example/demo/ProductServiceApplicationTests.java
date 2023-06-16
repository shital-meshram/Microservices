package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ProductServiceApplicationTests {
	
	@Container
	static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:6.0.6");
	
	static void setProperties (DynamicPropertyRegistry dynPropertyRegistry) {
		dynPropertyRegistry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
	}

	@Test
	void contextLoads() {
	}

}
