package com.ruppyrup.pricer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PriceApplication implements CommandLineRunner {

  public PriceApplication() {

  }

  public static void main(String[] args) {
    SpringApplication.run(PriceApplication.class, args);
  }

  @Override
  public void run(String... args) {

  }
}
