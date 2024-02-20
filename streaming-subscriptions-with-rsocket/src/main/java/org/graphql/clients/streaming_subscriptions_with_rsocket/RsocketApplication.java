package org.graphql.clients.streaming_subscriptions_with_rsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/* Request for connection verification RSocket request type*/
/* rsc --request --route=graphql --dataMimeType="application/graphql+json" --data '{\"query\": \"{greeting { greeting } } \" }' --debug tcp://localhost:9191 */

/* Request for connection verification RSocket stream type*/
/* rsc --stream --route=graphql --dataMimeType="application/graphql+json" --data '{\"query\": \" subscription { greetings { greeting } } \" }' --debug tcp://localhost:9191 */


@SpringBootApplication
public class RsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(RsocketApplication.class, args);
    }
}

record Greeting(String greeting) {
}

@Controller
class GreetingController {

    @SubscriptionMapping
    Flux<Greeting> greetings() {
        return Flux.fromStream(Stream.generate(() -> new Greeting("Hello, world @ " + Instant.now() + "!")))
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }

    @QueryMapping
    Greeting greeting() {
        return new Greeting("Hello, world!");
    }
}

