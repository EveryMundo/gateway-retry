package cloud.spring.poc.client;

import static java.lang.System.exit;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        	System.out.println();
        	
        	final WebClient webClient = WebClient.create("http://localhost:8080/greeting-service");

        	// GET requests 
        	log.info("***** 20 GET requests to endpoint /hello *****");
        	IntStream.range(0, 20).forEach(count -> webClient.get()
        												.uri("/hello")
        												.retrieve()
        												.bodyToMono(JsonNode.class)
        												.doOnNext(node -> log.info(node.toString()))
        												.block());
        	System.out.println();

        	// POST requests
        	log.info("***** 20 POST requests to endpoint /hello *****");
        	final ObjectNode body = new ObjectMapper().createObjectNode().put("to", "Mickey Mouse");
        	IntStream.range(0, 20).forEach(count -> webClient.post()
        												.uri("/hello")
        												.body(Mono.just(body), ObjectNode.class).retrieve()
        												.bodyToMono(JsonNode.class)
        												.doOnNext(node -> log.info(node.toString()))
        												.doOnError(e -> log.error("Iteration {} - {}", count, e.getMessage()))
        												.onErrorResume(e -> Mono.empty())
        												.block());
        	
            exit(0);
        };
    }

}
