package ar.admiral.microservices.core.product.services;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.product.ProductService;
import ar.admiral.util.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {
    private final ServiceUtil serviceUtil;

    // Inyectamos el ServiceUtil
    public ProductServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        return new Product(productId, "name - " + productId, 123, serviceUtil.getServiceAddress());
    }
}
