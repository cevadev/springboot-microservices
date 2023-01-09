package ar.admiral.api.core.composite.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface que nos permite acceder a la informacion de un productaggregate
 */
public interface ProductCompositeService {
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = "application/json"
    )
    // al usar @PathVariable significa que en el url path vendra el dato
    ProductAggregate getProduct(@PathVariable int productId);
}
