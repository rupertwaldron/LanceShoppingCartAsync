package com.ruppyrup.cart.service;

import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

  private final Publisher publisher;

  public ShoppingCartService(Publisher publisher) {
    this.publisher = publisher;
  }

  public void shoppingPublish() {
    Topic topic = new Topic("cart");
    Message message = new DataMessage(topic, "Info from topic cart");
    publisher.publish(message);
  }

}
