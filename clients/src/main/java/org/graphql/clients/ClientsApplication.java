package org.graphql.clients;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;

@SpringBootApplication
public class ClientsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientsApplication.class, args);
    }

    @Bean
    HttpGraphQlClient httpGraphQlClient() {
        return HttpGraphQlClient.builder().url("http://127.0.0.1:8080/graphql").build();
    }

    @Bean
    RSocketGraphQlClient rSocketGraphQlClient(RSocketGraphQlClient.Builder<?> builder) {
        return builder.tcp("127.0.0.1", 9191).route("graphql").build();
    }

    @Bean
    ApplicationRunner applicationRunner(RSocketGraphQlClient rSocket,
                                        HttpGraphQlClient http) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                var httpRequestDocument = """                     
                        query {
                            customerById(id: 1) {
                                id
                                name
                            }
                        }""";

                http.document(httpRequestDocument).retrieve("customerById").toEntity(Customer.class)
                        .subscribe(System.out::println);

                var rSocketRequestDocument = """
                        subscription {
                            greetings { greeting }
                        }
                        """;

                rSocket.document(rSocketRequestDocument).retrieveSubscription("greetings").toEntity(Greeting.class).subscribe(System.out::println);
            }
        };
    }
}

record Greeting(String greeting) {
}

record Customer(Integer id, String name) {
}
