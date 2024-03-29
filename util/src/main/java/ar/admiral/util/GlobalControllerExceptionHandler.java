package ar.admiral.util;

import ar.admiral.api.exceptions.BadRequestException;
import ar.admiral.api.exceptions.InvalidInputException;
import ar.admiral.api.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Cuando se produzca una de las excepcion que definimos GlobalControllerExceptionHandler
 * actua como interceptor
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Capturamos el error Not_Found y retornamos un objeto HttpErrorInfo
     * request -> tenemos acceso al request
     * ex -> importamos la excepcion del proyecto api
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(ServerHttpRequest request, NotFoundException ex){
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestException(ServerHttpRequest request, BadRequestException ex){
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, InvalidInputException ex){
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        // obtenemos el path que lanzo el error
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();
        LOG.debug("Returning HTTP Status: {} for path: {}, message {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
