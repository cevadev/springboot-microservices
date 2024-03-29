package ar.admiral.microservices.core.product.services;

import ar.admiral.api.core.product.Product;
import ar.admiral.api.core.product.ProductService;
import ar.admiral.api.exceptions.BadRequestException;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.api.exceptions.NotFoundException;
import ar.admiral.util.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/***
 * Servicio que responde cuando ProductCompositeIntegration hace una llamada
 */
@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ServiceUtil serviceUtil;

    // Inyectamos el ServiceUtil
    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        LOG.debug("/product return the found product for productId {}", productId);
        // validaiones
        if(productId < 1){
            throw new InvalidInputException("Invalid productId " + productId);
        }

        if(productId == 13){
            throw new NotFoundException("No product found for productId " + productId);
        }

        return new Product(productId, "name - " + productId, 123, serviceUtil.getServiceAddress());
    }
}
