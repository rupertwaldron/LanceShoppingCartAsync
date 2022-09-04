package com.ruppyrup.shop.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.models.TotalPrice;
import com.ruppyrup.shop.service.CartService;
import com.ruppyrup.shop.service.LanceSubService;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class PriceHandler implements MessageHandler {
  private static final Logger LOGGER = Logger.getLogger(LanceSubService.class.getName());
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handleMessage(Message message, CartService cartService) {
    LOGGER.info("Message received from update price :: " + message);
    String contents = message.getContents();
    if (!contents.contains("total")) return;
    try {
      cartService.setPrice(mapper.readValue(contents, TotalPrice.class));
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing totalprice object :: " + e.getMessage());
    }
  }

}
