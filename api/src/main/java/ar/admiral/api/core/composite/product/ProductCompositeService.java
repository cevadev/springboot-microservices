package ar.admiral.api.core.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface que nos permite acceder a la informacion de un productaggregate
 */

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
public interface ProductCompositeService {
    @Operation(
            summary = "${api.product-composite.get-composite-product.description}",
            description = "${api.product-composite.get-composite-product.notes}"
    )
    // definimos la respuesta swagger
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}"),
    })
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = "application/json"
    )
    // al usar @PathVariable significa que en el url path vendra el dato
    ProductAggregate getProduct(@PathVariable int productId);
}
