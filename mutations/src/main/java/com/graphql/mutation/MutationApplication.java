package com.graphql.mutation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class MutationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MutationApplication.class, args);
    }
}

record Customer(Integer id, String name) {
}

@Controller
class MutationController {
    private final Map<Integer, Customer> db = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    @QueryMapping
    Customer customerById(@Argument Integer id) {
        return this.db.get(id);
    }

    @MutationMapping
        //@SchemaMapping(typeName = "Mutation", field = "addCustomer")
    Customer addCustomer(@Argument String name) {
        var localId = this.id.incrementAndGet();
        var value = new Customer(localId, name);
        this.db.put(localId, value);
        return value;
    }
}