package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class CustomerDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Customer> findAll() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers",
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        );
    }

        public void save(Customer customer) {
            jdbcTemplate.update(
                    "INSERT INTO customers (first_name, last_name) VALUES (?, ?)",
                    customer.getFirstName(), customer.getLastName()
            );
        }
}