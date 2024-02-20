package com.graphql.batching_requests;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootApplication
public class BatchingRequestsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchingRequestsApplication.class, args);
    }
}

record Customer(Integer id, String name) {
}

record Account(Integer id) {
}

@Slf4j
@Controller
class BatchController {

    @QueryMapping
    Collection<Customer> customers() {
        return List.of(new Customer(1, "A"), new Customer(2, "B"));
    }

    @BatchMapping
    Map<Customer, Account> account(List<Customer> customers) {
        log.info("Calling account for " + customers.size() + " customer");
        return customers
                .stream()
                .collect(Collectors.toMap(customer -> customer,
                        customer -> new Account(customer.id())));
    }

    /*@SchemaMapping(typeName = "Customer")
    Account account(Customer customer) {
        log.info("Getting account for customer # " + customer.id());
        return new Account(customer.id());
    }*/
}