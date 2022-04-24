package com.ruppyrup.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {

  private final Map<ShopItem, Integer> cartItems = new HashMap<>();

  private final Shopper shopper;

  private double totalPrice;
  private double discout;

  public Cart(Shopper shopper) {
    this.shopper = shopper;
  }

  public void addCartItem(final ShopItem shopItem) {
    if (cartItems.containsKey(shopItem)) {
      Integer quantity = cartItems.get(shopItem);
      cartItems.replace(shopItem, ++quantity);
    } else {
      cartItems.put(shopItem, 1);
    }
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public double getDiscout() {
    return discout;
  }

  public void setDiscout(double discout) {
    this.discout = discout;
  }
}
