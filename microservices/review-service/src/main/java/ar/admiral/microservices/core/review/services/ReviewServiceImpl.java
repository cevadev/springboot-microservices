package ar.admiral.microservices.core.review.services;

import ar.admiral.api.core.review.Review;
import ar.admiral.api.core.review.ReviewServices;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.api.exceptions.NotFoundException;
import ar.admiral.util.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewServiceImpl implements ReviewServices {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if(productId < 1){
            throw new InvalidInputException("Invalid productId " + productId);
        }

        if(productId == 123){
            LOG.debug("No reviews for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Review> list = new ArrayList<Review>();
        list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));
        LOG.debug("/review response size {}", list.size());

        return list;
    }
}
