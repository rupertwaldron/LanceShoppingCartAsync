package com.ruppyrup.shop.service;

import com.ruppyrup.models.Shopper;
import org.springframework.stereotype.Service;

@Service
public class ShopperService {

  private final Shopper currentShopper;

  public ShopperService() {
    currentShopper = new Shopper("Rup", 2L);
  }

  public Shopper getCurrentShopper() {
    return currentShopper;
  }

}
