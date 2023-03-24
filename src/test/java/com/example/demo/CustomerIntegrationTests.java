package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// port number is generated randomly

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class CustomerIntegrationTests {

	@Autowired
	private CustomerDao customerDao;

	@Container
	private static MySQLContainer container = new MySQLContainer("mysql:latest")
			.withDatabaseName("somedatabase")
			.withUsername("root")
			.withPassword("root");

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() {
		List<Customer> customers = customerDao.findAll();
		assertThat(customers).hasSize(2);
	}
}
