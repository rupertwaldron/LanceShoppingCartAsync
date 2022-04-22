package com.ruppyrup.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import com.ruppyrup.models.Cart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

  private final Publisher publisher;

  public ShoppingCartService(Publisher publisher) {
    this.publisher = publisher;
  }

  public void shoppingPublish() {
    Topic topic = new Topic("cart");
    Cart myCart = new Cart();
    String myCartJson = null;
    try {
      myCartJson = new ObjectMapper().writeValueAsString(myCart);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Message message = new DataMessage(topic, myCartJson);
    publisher.publish(message);
  }

}
