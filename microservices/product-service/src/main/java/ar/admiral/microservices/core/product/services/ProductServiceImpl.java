package ar.admiral.microservices.core.product.services;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.product.ProductService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProduct(int productId) {
        return null;
    }
}
