package com.ruppyrup.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalPrice {
  private double total;
  private double discountPrice;
  private double discount = 1.0;

}
