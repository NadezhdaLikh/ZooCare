package com.spring.zoocare.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandler {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions errorAttributeOptions) {
                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.MESSAGE));
            }
        };
    }

    /*@ExceptionHandler(CustomBackendException.class)
    public void handleCustomBackendException(HttpServletResponse httpServletResponse, CustomBackendException e) throws IOException {
        httpServletResponse.sendError(e.getHttpStatus().value(), e.getMessage());
    }*/

    @ExceptionHandler(CustomBackendException.class)
    public ResponseEntity<ErrorMessage> handleCustomBackendException(CustomBackendException e) {
        log.error("Handling CustomBackendException: {}", e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorMessage);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingServletReqParam(MissingServletRequestParameterException e) {
        String param = e.getParameterName();
        log.error("{} param is missing!", param);

        return ResponseEntity.badRequest().body(new ErrorMessage(String.format("Parameter %s is missing!", param)));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgTypeMismatch(MethodArgumentTypeMismatchException e) {
        String param = e.getParameter().getParameterName();
        log.error("Method argument type mismatch for parameter {}!", param);

        return ResponseEntity.badRequest().body(new ErrorMessage(String.format("Method argument type mismatch for parameter %s", param)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgNotValid(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getField() + " " + fieldError.getDefaultMessage() : e.getMessage();
        log.error(message);

        return ResponseEntity.badRequest().body(new ErrorMessage(message));
    }
}
