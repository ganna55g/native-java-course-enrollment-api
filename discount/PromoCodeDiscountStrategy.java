package com.coursemanagement.discount;

import java.math.BigDecimal;

public class PromoCodeDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal originalPrice) {

        return originalPrice.multiply(
                new BigDecimal("0.15")
        );
    }
}