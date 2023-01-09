package ar.admiral.microservices.composite.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// indicamos donde debe buscar otros componentes de los proyectos importamos (api y util)
@ComponentScan("ar.admiral")
public class ProductCompositeServiceApplication {

	// le decimos a Spring que se cree un bean RestTemplate y lo hacemos disponible para todo el proyecto
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

}
