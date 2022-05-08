package com.ruppyrup.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import com.ruppyrup.lance.subscriber.Subscriber;
import com.ruppyrup.models.Cart;
import com.ruppyrup.models.ShopItem;
import com.ruppyrup.models.TotalPrice;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Getter
public class LancePubService {

  private static final Logger LOGGER = Logger.getLogger(LancePubService.class.getName());

  private final Publisher publisher;
  private final ObjectMapper mapper = new ObjectMapper();

  public LancePubService(Publisher publisher){
    this.publisher = publisher;
  }

  public void publish(Message message) {
    publisher.publish(message);
  }
}
