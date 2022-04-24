package com.ruppyrup.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import com.ruppyrup.lance.subscriber.Subscriber;
import com.ruppyrup.lance.transceivers.MsgTransceiver;
import com.ruppyrup.models.Cart;
import com.ruppyrup.models.CartInputDto;
import com.ruppyrup.models.ShopItem;
import com.ruppyrup.models.Shopper;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
  private static final Logger LOGGER = Logger.getLogger(ShoppingCartService.class.getName());
  private final Subscriber subscriber;
  private final Map<Shopper, Cart> shoppingCarts = new HashMap<>();
  private boolean receivingMessages;
  private final ObjectMapper mapper = new ObjectMapper();

  public ShoppingCartService(Subscriber subscriber) {
    this.subscriber = subscriber;
  }

  public void start() throws SocketException {
    subscriber.start();
    receivingMessages = true;
  }

  public void close() {
    receivingMessages = false;
    subscriber.close();
  }

  public void subscribeToCart() {
    subscriber.subscribe(this.getClass().getSimpleName(), new Topic("cart"));
  }

  public void receiveMessagesFromCart() {
    while (receivingMessages) {
      Message message = subscriber.receive();
      System.out.println(message.getContents());
      CartInputDto newCartItem = null;
      try {
        newCartItem = mapper.readValue(message.getContents(), CartInputDto.class);
      } catch (JsonProcessingException e) {
        LOGGER.warning("Error converting to CartInputDto :: " + e.getMessage());
      }
      if (newCartItem == null) continue;
      Shopper shopper = newCartItem.getShopper();
      ShopItem shopItem = newCartItem.getShopItem();
      if (shoppingCarts.containsKey(shopper)) {
        shoppingCarts.get(shopper).addCartItem(shopItem);
      } else {
        Cart newCart = new Cart(shopper);
        newCart.addCartItem(shopItem);
        shoppingCarts.put(shopper, newCart);
      }
    }
  }
}
