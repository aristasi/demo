package com.example.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// port number is generated randomly

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomerIntegrationTests {

	@Autowired
	private CustomerDao customerDao;

	// docker run -e MYSQL_USERNAME=... -e MYSQL_PASSWORD=....  mysql:latest


	//this annotation will shut down the container at the end of the test thats why we remove test containers annotations
	private static MySQLContainer container = (MySQLContainer) new MySQLContainer("mysql:latest");

	@BeforeAll
	public static void setup() {
		container.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() throws IOException, InterruptedException {
		container.withClasspathResourceMapping("application.properties", "/tp/application.properties", BindMode.READ_ONLY);
		container.execInContainer("la", "-la");
		Integer containerHostPort = container.getMappedPort(3306);
		String stdout = container.getLogs(OutputFrame.OutputType.STDOUT);
		List<Customer> customers = customerDao.findAll();
		assertThat(customers).hasSize(2);
	}
}
