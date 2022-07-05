package ar.admiral.microservices.composite.product.services;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.product.ProductService;
import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.recommendation.RecommendationService;
import ar.admiral.api.core.review.Review;
import ar.admiral.api.core.review.ReviewServices;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementacion de ProductService, RecommendationServices y ReviewServices
 */
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewServices {
    @Override
    public Product getProduct(int productId) {
        return null;
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
