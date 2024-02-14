package com.example.controller;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex,
                                                                          @Nonnull HttpHeaders headers,
                                                                          @Nonnull HttpStatusCode status,
                                                                          @Nonnull ServerWebExchange exchange) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::getError)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return Mono.just(ResponseEntity.badRequest().body(body));
    }

    protected String getError(FieldError error) {
        return messageSource.getMessage(
                Objects.requireNonNull(error.getCode()),
                error.getArguments(),
                error.getDefaultMessage(),
                Locale.getDefault());
    }
}
