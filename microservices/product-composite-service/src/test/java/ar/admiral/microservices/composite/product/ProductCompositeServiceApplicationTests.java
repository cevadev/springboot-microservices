package ar.admiral.microservices.composite.product;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.review.Review;
import ar.admiral.microservices.composite.product.services.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductCompositeServiceApplicationTests {
	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_NOT_FOUND = 2;
	private static final int PRODUCT_INVALID = 3;

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

		
	}
}
