package com.ruppyrup.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.models.CartInputDto;
import com.ruppyrup.models.ShopItem;
import com.ruppyrup.models.TotalPrice;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class CartService {

  private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
  private final ShopperService shopperService;
  private final LancePubService lancePubService;
  private final ObjectMapper mapper = new ObjectMapper();

  private final Map<ShopItem, Integer> cartItems = new HashMap<>(Map.of(
      new ShopItem(100L, "Test item1", 8.99), 1,
      new ShopItem(101L, "Test item2", 10.99), 3
  ));
  private TotalPrice price = new TotalPrice(10.0, 9.0, 11);

  public CartService(ShopperService shopperService, LancePubService lancePubService) {
    this.shopperService = shopperService;
    this.lancePubService = lancePubService;
  }

  public void addCartItem(ShopItem shopItem) {

    Topic topic = new Topic("cart");
    String itemJson = null;

    CartInputDto cartItem = new CartInputDto(shopItem, shopperService.getCurrentShopper());

    try {
      itemJson = new ObjectMapper().writeValueAsString(cartItem);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error converting cartInputDto to string :: " + e.getMessage());
    }
    Message message = new DataMessage(topic, itemJson);
    lancePubService.publish(message);
  }

  public void processMessage(Map<ShopItem, Integer> newcartItemps) {
    cartItems.clear();
    cartItems.putAll(newcartItemps);
  }
}
