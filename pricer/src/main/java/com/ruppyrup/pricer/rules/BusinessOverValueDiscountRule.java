package com.ruppyrup.pricer.rules;

import org.springframework.stereotype.Component;

@Component
public class BusinessOverValueDiscountRule implements BusinessDiscountRule {

  @Override
  public double applyDiscount(double totalPrice) {
    return (totalPrice > 40.0) ? totalPrice * 0.9 : totalPrice;
  }
}
