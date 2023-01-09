package ar.admiral.microservices.composite.product.services;

import ar.admiral.api.core.composite.product.*;
import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.review.Review;
import ar.admiral.api.exceptions.NotFoundException;
import ar.admiral.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que implementa a la interface ProductCompositeService del proyecto API
 */
@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    // Service util y clase de integracion
    @Autowired
    private ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration){
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {
        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // La lista recommendations debemos convertirlo en un objeto RecommendationSummary
        List<RecommendationSummary> recommendationSummaryList = recommendations
                .stream()
                .map(recommendation -> new RecommendationSummary(recommendation.getRecommendationId(),
                        recommendation.getAuthor(), recommendation.getRate()))
                .collect(Collectors.toList());

        List<ReviewSummary> reviewSummaryList = reviews
                .stream()
                .map(review -> new ReviewSummary(review.getReviewId(), review.getAuthor(), review.getSubject()))
                .collect(Collectors.toList());

        // obtenemos los services address de cada servicio
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, recommendationAddress, reviewAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaryList, reviewSummaryList, serviceAddresses);
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        Product product = integration.getProduct(productId);
        if(product == null){
            throw new NotFoundException("No product found for ProductId: " + productId);
        }
        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);
        // construimos el objeto ProductAggregate
        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }
}
