package com.ruppyrup.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication implements CommandLineRunner {


  public ShopApplication() {

  }

  public static void main(String[] args) {
    SpringApplication.run(ShopApplication.class, args);
  }

  @Override
  public void run(String... args) {

  }
}