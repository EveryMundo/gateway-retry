package cloud.spring.poc.hello.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cloud.spring.poc.hello.dto.GreetingDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
@Slf4j
public class GreetingController {
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@Autowired
	private ObjectMapper mapper;

	@GetMapping
	public ResponseEntity<Mono<ObjectNode>> alwaysSayHello() {
		if (this.counter.getAndIncrement() % 2 == 0) {
			return ResponseEntity.ok(Mono.just(this.mapper.createObjectNode().put("message", "I never fail")));
		}
		
		log.error("It doesn't matter this request failed");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}	

	@PostMapping
	public ResponseEntity<Mono<ObjectNode>> sayHello(@RequestBody @Valid GreetingDTO greetingDTO) {
		if (this.counter.getAndIncrement() % 2 == 0) {
			String message = String.format("Hello, %s", greetingDTO.getTo());
			return ResponseEntity.ok(Mono.just(this.mapper.createObjectNode().put("message", message)));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}	

}
