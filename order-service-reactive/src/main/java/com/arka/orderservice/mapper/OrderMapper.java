package com.arka.orderservice.mapper;

import com.arka.orderservice.dto.*;
import com.arka.orderservice.model.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static BigDecimal calculateTotal(List<OrderItemDto> items) {
        return items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
