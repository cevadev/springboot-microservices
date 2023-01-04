package ar.admiral.api.core.recommendation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @RequestParam -> cuando se utiliza el @requestParam el uri sera https://www.site:8080/recommendation?productId=1
 */
public interface RecommendationService {
    @GetMapping(
            value = "/recommendation",
            produces = "application/json"
    )
    List<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
