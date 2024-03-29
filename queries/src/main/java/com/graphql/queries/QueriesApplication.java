package com.graphql.queries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class QueriesApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueriesApplication.class, args);
    }
}

record Customer(Integer id, String name) {
}

record Account(Integer id) {
}

@Controller
class GreetingsController {

    Collection<Customer> customers = List.of(new Customer(1, "A"),
            new Customer(2, "B"));

    @QueryMapping
    Customer customerById(@Argument Integer id) {
        return new Customer(id, Math.random() > .5 ? "A" : "B");
    }

    @QueryMapping
    String helloWithName(@Argument String name) {
        return String.format("Hello, %s!", name);
    }

    //@QueryMapping
    @SchemaMapping(typeName = "Query", field = "hello")
// First and second annotation equal
    String hello() {
        return "Hello, world!";
    }

//    @QueryMapping
//    Collection<Customer> customers() {
//        return this.customers;
//    }

    @QueryMapping
    Flux<Customer> customers() {
        return Flux.fromIterable(this.customers);
    }

    @SchemaMapping(typeName = "Customer")
    Mono<Account> account(Customer customer) {
        return Mono.just(new Account(customer.id()));
    }
}