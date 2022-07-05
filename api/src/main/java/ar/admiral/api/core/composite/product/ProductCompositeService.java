package ar.admiral.api.core.composite.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface que nos permite acceder a la informacion de un producto agregado
 */
public interface ProductCompositeService {
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ProductAggregate getProduct(@PathVariable int productId);
}
