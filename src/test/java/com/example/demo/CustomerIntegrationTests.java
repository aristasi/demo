package com.example.demo;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerIntegrationTests extends AbstractTest {

	private static final Logger logger = LoggerFactory.getLogger(HomepageController.class);

	@Autowired
	private CustomerDao customerDao;

	@Test
	@Order(1)
	void test_find_and_save_customers() {
		logger.info("Starting test 1");
		List<Customer> customers = customerDao.findAll();
		System.out.println(customers);
		assertThat(customers).hasSize(2);
		saveAnewCustomer();
		customers = customerDao.findAll();
		assertThat(customers).hasSize(3);
		logger.info("Finished test 1");
	}

	@Test
	@Order(2)
	void test_find_customers() {
		logger.info("Starting test 2");
		List<Customer> customers = customerDao.findAll();
		assertThat(customers).hasSize(3);
		logger.info("Finished test 2");
	}

	private void saveAnewCustomer() {
		Customer customer = new Customer();
		customer.setFirstName("3rd");
		customer.setLastName("row");
		customerDao.save(customer);
	}
}
