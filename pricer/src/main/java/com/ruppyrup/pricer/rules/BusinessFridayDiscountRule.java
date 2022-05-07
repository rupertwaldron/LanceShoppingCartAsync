package com.ruppyrup.pricer.rules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class BusinessFridayDiscountRule implements BusinessDiscountRule {

  @Override
  public double applyDiscount(double totalPrice) {
    LocalDate localDate = LocalDate.now();
    return localDate.getDayOfWeek().equals(DayOfWeek.FRIDAY) ? totalPrice * 0.05 : totalPrice;
  }
}
