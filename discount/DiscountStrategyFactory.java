package com.coursemanagement.discount;

import com.coursemanagement.model.DiscountType;

public class DiscountStrategyFactory {

    public static DiscountStrategy getStrategy(
            DiscountType discountType) {

        if (discountType == null) {
            throw new IllegalArgumentException(
                    "Discount type is required"
            );
        }

        switch (discountType) {

            case NONE:
                return new NoDiscountStrategy();

            case STUDENT:
                return new StudentDiscountStrategy();

            case VIP:
                return new VipDiscountStrategy();

            case PROMO:
                return new PromoCodeDiscountStrategy();

            default:
                throw new IllegalArgumentException(
                        "Unsupported discount type"
                );
        }
    }
}