package com.ruppyrup.biller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillApplication implements CommandLineRunner {

  public BillApplication() {

  }

  public static void main(String[] args) {
    SpringApplication.run(BillApplication.class, args);
  }

  @Override
  public void run(String... args) {

  }
}
