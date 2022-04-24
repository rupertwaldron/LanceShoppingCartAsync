package com.ruppyrup.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInputDto {
  private ShopItem shopItem;
  private Shopper shopper;
}
