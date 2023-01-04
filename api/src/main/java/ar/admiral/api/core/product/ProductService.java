package ar.admiral.api.core.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @PathVariale -> significa que en el url se va a incluir el valor de un productId ejemplo:
 * https://www.site:8080/product/1
 */
public interface ProductService {
    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json"
    )
    Product getProduct(@PathVariable int productId);
}