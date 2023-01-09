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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductCompositeIntegration es un componente de Spring
 * que llama a la implementacion de ProductService, RecommendationServices y ReviewServices
 * que se encuentra dentro de la carpeta microservicios
 */
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewServices {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    // Bean RestTemplate para llamar a otros servicios
    private final RestTemplate restTemplate;

    // para trabjar con json
    private final ObjectMapper objectMapper;

    // url de los distintos proyectos
    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    // @Value() -> le pasamos el valor de la respectiva variable del archivo application.yml
    // utilizando spring expression laguage ${}
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
        // esta url varia porque recommendatio y review utilizan request param
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
    }

    @Override
    public List<Review> getReviews(int productId) {
        try{
            String url = reviewServiceUrl + productId;
            LOG.debug("Llamamos al API getReviews en la URL {}", url);
            List<Review> reviewList = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
            }).getBody();
            return reviewList;
        }
        catch(Exception ex){
            LOG.warn("Se produjo una excepcion mientras solicitamos la lista de revisiones," +
                    "retornamos cero revisiones: {}", ex.getMessage());
            // retornamos una lista vacia
            return new ArrayList<>();
        }
    }
    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try{
            String url = recommendationServiceUrl + productId;
            LOG.debug("Llamamos al API getRecommendations en la URL {}", url);
            // obtenemos una lista de recomendaciones haciendo un get al url
            List<Recommendation> recommendationList = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
            }).getBody();
            LOG.debug("Encontramos {} recomendaciones para el producto {}", recommendationList.size(), productId);
            return recommendationList;
        }
        catch(Exception ex){
            LOG.warn("Se produjo una excepcion mientras solicitamos la lista de recomendaciones," +
                    "retornamos cero recomendaciones: {}", ex.getMessage());
            // retornamos una lista vacia
            return new ArrayList<>();
        }
    }

    @Override
    public Product getProduct(int productId) {
        try {
            // formamos el url concatenando el productId
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
            // el objeto objetMapper lee el cuerpo de la respuesta de un objeto y
            // le pasamos la informacion a nuestro objeto HttpErrorInfo y con getMeesage() obtenemos mensaje
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch(IOException error){
            return error.getMessage();
        }
    }
}
