package com.ruppyrup.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {

  private Map<ShopItem, Integer> items = new HashMap<>();

  private double totalPrice;
  private double discout;

  public Cart() {
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
