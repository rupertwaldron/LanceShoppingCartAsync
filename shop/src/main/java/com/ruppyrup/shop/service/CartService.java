package com.ruppyrup.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import com.ruppyrup.models.CartInputDto;
import com.ruppyrup.models.ShopItem;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CartService {

  private final Publisher publisher;

  private final ShopperService shopperService;

  public CartService(Publisher publisher, ShopperService shopperService) throws SocketException {
    this.publisher = publisher;
    this.shopperService = shopperService;
    publisher.start();
  }

  private final Map<ShopItem, Integer> cartItems = new HashMap<>(Map.of(
      new ShopItem(100L, "Test item1", 8.99), 1,
      new ShopItem(101L, "Test item2", 10.99), 3
  ));

  public void addCartItem(ShopItem shopItem) {
    if (cartItems.containsKey(shopItem)) {
      Integer quantity = cartItems.get(shopItem);
      cartItems.replace(shopItem, ++quantity);
    } else {
      cartItems.put(shopItem, 1);
    }

    Topic topic = new Topic("cart");
    String itemJson = null;

    CartInputDto cartItem = new CartInputDto(shopItem, shopperService.getCurrentShopper());

    try {
      itemJson = new ObjectMapper().writeValueAsString(cartItem);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Message message = new DataMessage(topic, itemJson);
    publisher.publish(message);
  }

}
