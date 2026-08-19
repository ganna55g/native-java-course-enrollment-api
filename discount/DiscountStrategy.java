package com.coursemanagement.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal calculateDiscount(BigDecimal originalPrice);
}