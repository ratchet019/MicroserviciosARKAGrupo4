package com.arka.ecommerce.mssales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(Long productId) {
        super("Producto sin stock suficiente: " + productId);
    }
}
