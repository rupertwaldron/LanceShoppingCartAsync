package com.ruppyrup.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopItem {
  private Long id;
  private String name;
  private Double price;

  public ShopItem() {
  }

}
