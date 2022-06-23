package ar.admiral.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// clase componente: significa que esta clase se puede inyectar en lo proyecto que importan util
@Component
public class ServiceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtil.class);
    private final String port;
    private String serviceAddress = null;

    // leemos el port del archivo properties. nombre de la key server.port
    public ServiceUtil(@Value("${server.port}") String port){
        this.port = port;
    }
}
