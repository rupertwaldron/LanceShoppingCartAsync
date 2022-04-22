package com.ruppyrup.stocker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockApplication implements CommandLineRunner {


  public StockApplication() {

  }

  public static void main(String[] args) {
    SpringApplication.run(StockApplication.class, args);
  }

  @Override
  public void run(String... args) {

  }
}
