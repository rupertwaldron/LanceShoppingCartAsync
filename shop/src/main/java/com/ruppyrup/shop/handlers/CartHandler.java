package com.ruppyrup.shop.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.models.Cart;
import com.ruppyrup.models.ShopItem;
import com.ruppyrup.shop.service.CartService;
import com.ruppyrup.shop.service.LanceSubService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class CartHandler implements MessageHandler {

  private static final Logger LOGGER = Logger.getLogger(LanceSubService.class.getName());
  private final ObjectMapper mapper = new ObjectMapper();

  public void handleMessage(Message message, CartService cartService) {
    LOGGER.info("Message received from update cart :: " + message);
    String contents = message.getContents();
    if (!contents.contains("cartitems")) return;
    TypeReference<HashMap<ShopItem, Integer>> typeRef = new TypeReference<>() {};
    Map<ShopItem, Integer> cartMap = null;
    Cart updatedCart;
    try {
      updatedCart = mapper.readValue(contents, Cart.class);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing cart map :: " + e.getMessage());
      return;
    }
    if (updatedCart == null) return;
    cartService.processMessage(updatedCart.getCartItems());
  }

}
