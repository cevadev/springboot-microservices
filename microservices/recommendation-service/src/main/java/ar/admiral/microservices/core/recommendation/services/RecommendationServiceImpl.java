package ar.admiral.microservices.core.recommendation.services;

import ar.admiral.api.core.recommendation.Recommendation;
import ar.admiral.api.core.recommendation.RecommendationService;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.util.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if(productId < 1){
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if(productId == 113){
            LOG.debug("No recommendations found for productId " + productId);
            return new ArrayList<>();
        }

        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 3", 3, "Content 1", serviceUtil.getServiceAddress()));

        LOG.debug("/recommendation response size: {}", list.size());
        return list;
    }
}
