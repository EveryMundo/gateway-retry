package cloud.spring.poc.hello.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class GreetingDTO {

	@NotBlank
	private String to;
	
}
