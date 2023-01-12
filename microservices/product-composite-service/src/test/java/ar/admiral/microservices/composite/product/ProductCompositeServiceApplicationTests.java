package ar.admiral.microservices.composite.product;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.review.Review;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.api.exceptions.NotFoundException;
import ar.admiral.microservices.composite.product.services.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductCompositeServiceApplicationTests {
	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 2;
	private static final int PRODUCT_ID_INVALID = 3;

	@Autowired private WebTestClient client;
	// probamos al ProductCompositeIntegration
	// usamos la integracion de Spring con Mockito
	@MockBean private ProductCompositeIntegration compositeIntegration;

	// configuramos el entorno de prueba antes de iniciar las pruebas
	@BeforeEach
	void setUp(){
		// cuando alguien llame al metodo compositeIntegration.getProduct() con un codigo valido
		Mockito.when(compositeIntegration.getProduct(PRODUCT_ID_OK))
				.thenReturn(new Product(PRODUCT_ID_OK, "name", 1, "mock-address"));

		// // cuando alguien llame al metodo compositeIntegration.getRecommendations() con un codigo valido
		Mockito.when(compositeIntegration.getRecommendations(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Recommendation(PRODUCT_ID_OK, 1,
						"author", 1, "content", "mock-address")));

		// // cuando alguien llame al metodo compositeIntegration.getReviews() con un codigo valido
		Mockito.when(compositeIntegration.getReviews(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Review(PRODUCT_ID_OK, 1,
						"author", "subject", "content", "mock-address")));

		Mockito.when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND))
				.thenThrow(new NotFoundException("Not found: " + PRODUCT_ID_NOT_FOUND));

		Mockito.when(compositeIntegration.getProduct(PRODUCT_ID_INVALID))
				.thenThrow(new InvalidInputException("Invalid: " + PRODUCT_ID_INVALID));
	}

	@Test
	void getProductById(){
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_OK)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
				.jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1);
	}

	@Test
	void getProductNotFound(){
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("Not found: " + PRODUCT_ID_NOT_FOUND);
	}
	@Test
	void getProductInvalidInput(){
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_INVALID)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID)
				.jsonPath("$.message").isEqualTo("Invalid: " + PRODUCT_ID_INVALID);
	}
}
