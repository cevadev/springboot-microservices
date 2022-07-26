package ar.admiral.microservices.composite.product.services;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.product.ProductService;
import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.recommendation.RecommendationService;
import ar.admiral.api.core.review.Review;
import ar.admiral.api.core.review.ReviewServices;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.api.exceptions.NotFoundException;
import ar.admiral.util.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * ProductCompositeIntegration es un componente de Spring
 * Implementacion de ProductService, RecommendationServices y ReviewServices
 */
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewServices {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;

    // para trabjar con json
    private final ObjectMapper objectMapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    // @Value() -> le pasamos el valor de la respectiva variable del archivo application.yml
    public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}") String productServiceHost,
                                       @Value("${app.product-service.port}") String productServicePort,
                                       @Value("${app.recommendation-service.host}") String recommendationServiceHost,
                                       @Value("${app.recommendation-service.port}") String recommendationServicePort,
                                       @Value("${app.review-service.host}") String reviewServiceHost,
                                       @Value("${app.review-service.port}") String reviewServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId";
    }

    @Override
    public Product getProduct(int productId) {
        // manejamos el procesamiento en un trycatch por si algo sale mal
        try {
            String url = productServiceUrl + productId;
            LOG.debug("Llamamos al API getProduct en la URL {}", url);

            // que el restTemplate retorne un objeto Product
            Product product = restTemplate.getForObject(url, Product.class);
            LOG.debug("Encontramos un producto con id: {}", product.getProductId());

            return product;
        }
        catch(HttpClientErrorException ex){
            switch (ex.getStatusCode()){
                case NOT_FOUND:
                    throw new NotFoundException();
                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException();
                default:
                    LOG.warn("Ocurrió un error inesperado: {}, se lanzara", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }
    }

    private String getErrorMessage(HttpClientErrorException ex){
        try {
            // el objeto objetMapper lee texto de un objeto y la pasa a otro objeto custom
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch(IOException error){
            return error.getMessage();
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return null;
    }

    @Override
    public List<Review> getReviews(int productId) {
        return null;
    }
}
