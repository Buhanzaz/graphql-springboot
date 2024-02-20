package com.graphql.graphql_java_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class GraphqlJavaEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlJavaEngineApplication.class, args);
    }


    /*@Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer(CrmService crm) {
        return new RuntimeWiringConfigurer() {
            @Override
            public void configure(RuntimeWiring.Builder builder) {
                builder.type("Query", new UnaryOperator<TypeRuntimeWiring.Builder>() {
                    @Override
                    public TypeRuntimeWiring.Builder apply(TypeRuntimeWiring.Builder wiring) {
                        return wiring.dataFetcher("customers", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment environment) throws Exception {
                                return crm.getCustomers();
                            }
                        });
                    }
                });
            }
        };
    }*/

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer(CrmService crm) {
        return builder -> {
            builder.type("Customer", wiring -> wiring
                    .dataFetcher("profile", evn -> crm.getProfile(evn.getSource())));
            builder.type("Query", wiring -> wiring
                    .dataFetcher("customerById",
                            env -> crm.getCustomerById(Integer.parseInt(env.getArgument("id"))))
                    .dataFetcher("customers", env -> crm.getCustomers()));
        };
    }
}


record Customer(Integer id, String name) {
}

record Profile(Integer id, Integer customerId) {
}

@Service
class CrmService {

    Collection<Customer> customers = List.of(new Customer(1, "A"),
            new Customer(2, "B"));

    Collection<Profile> profiles = List.of(new Profile(1, 1),
            new Profile(2, 2));

    Collection<Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Profile getProfile(Customer customer) {
        return profiles.stream()
                .filter(profile -> profile.customerId().equals(customer.id()))
                .findFirst()
                .orElse(null);
    }
}
