package com.ruppyrup.models;

public class ShopItem {
  private long id;
  private String name;
  private double price;

  public ShopItem() {
  }

  public ShopItem(long id, String name, double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
