package com.ruppyrup.cart;

import com.ruppyrup.cart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CartApplication implements CommandLineRunner {

  private final ShoppingCartService service;

  public CartApplication(ShoppingCartService service) {
    this.service = service;
  }

  public static void main(String[] args) {
    SpringApplication.run(CartApplication.class, args);
  }

  @Override
  public void run(String... args) {
    service.shoppingPublish();
  }
}
