package com.ruppyrup.pricer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
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

@Service
@Getter
public class PricerService {

  private static final Logger LOGGER = Logger.getLogger(PricerService.class.getName());

  private final PriceCalculator priceCalculator;
  private final Publisher publisher;
  private final Subscriber subscriber;

  private final ObjectMapper mapper = new ObjectMapper();

  private final Map<ShopItem, Integer> cartItems = new HashMap<>(Map.of(
      new ShopItem(100L, "Test item1", 8.99), 1,
      new ShopItem(101L, "Test item2", 10.99), 3
  ));

  public PricerService(PriceCalculator priceCalculator, Publisher publisher, Subscriber subscriber) {
    this.priceCalculator = priceCalculator;
    this.publisher = publisher;
    this.subscriber = subscriber;
    subscriber.subscribe(this.getClass().getSimpleName(), new Topic("cartupdate"));


    CompletableFuture.runAsync(() ->
    subscriber.createUdpFlux().subscribe(
        this::handleCartMessage,
        err -> System.out.println("Error: " + err.getMessage()),
        () -> {
          System.out.println("Done!");
        })
    );
  }

  private void handleCartMessage(Message message) {
    LOGGER.info("Message received from update cart :: " + message);
    String contents = message.getContents();
    TypeReference<HashMap<ShopItem, Integer>> typeRef = new TypeReference<>() {};
    Cart updatedCart = null;
    try {
      updatedCart = mapper.readValue(contents, Cart.class);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing cart map :: " + e.getMessage());
    }
    if (updatedCart == null) return;
    cartItems.clear();
    cartItems.putAll(updatedCart.getCartItems());
    publishPriceUpdate();
  }

  public void publishPriceUpdate() {
    Topic priceUpdateTopic = new Topic("priceupdate");
    String priceJson = null;
    TotalPrice totalPrice = priceCalculator.createTotalPrice(cartItems);
    try {
      priceJson = mapper.writeValueAsString(totalPrice);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Problem converting shopper to string :: " + e.getMessage());
    }
    Message message = new DataMessage(priceUpdateTopic, priceJson);
    publisher.publish(message);
  }

}
