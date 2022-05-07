package com.ruppyrup.pricer.service;

import com.ruppyrup.models.ShopItem;
import com.ruppyrup.models.TotalPrice;
import com.ruppyrup.pricer.rules.BusinessDiscountRule;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

  private final List<BusinessDiscountRule> businessDiscountRules;

  public PriceCalculator(List<BusinessDiscountRule> businessDiscountRules) {
    this.businessDiscountRules = businessDiscountRules;
  }

  public TotalPrice createTotalPrice(Map<ShopItem, Integer> cartItems) {
    double totalPrice = getTotalPrice(cartItems);
    double discountedPrice = applyDiscount(totalPrice);
    double discount = (totalPrice - discountedPrice) / totalPrice;
    return new TotalPrice(totalPrice, discountedPrice, discount);
  }


  private double getTotalPrice(Map<ShopItem, Integer> cartItems) {
    return cartItems.entrySet().stream()
        .mapToDouble(x -> x.getKey().getPrice() * x.getValue())
        .reduce(0, Double::sum);
  }

  private double applyDiscount(double price) {
    double newPrice = price;
    for (BusinessDiscountRule br : businessDiscountRules) {
      newPrice = br.applyDiscount(newPrice);
    }
    return newPrice;
  }

}
