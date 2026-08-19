package com.coursemanagement.discount;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal originalPrice) {
        return BigDecimal.ZERO;
    }
}