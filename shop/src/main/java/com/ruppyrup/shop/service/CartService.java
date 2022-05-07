package com.ruppyrup.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.DataMessage;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.publisher.Publisher;
import com.ruppyrup.lance.subscriber.Subscriber;
import com.ruppyrup.models.Cart;
import com.ruppyrup.models.CartInputDto;
import com.ruppyrup.models.ShopItem;
import com.ruppyrup.models.TotalPrice;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Getter
public class CartService {

  private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

  private final Publisher publisher;
  private final Subscriber subscriber;

  private final ShopperService shopperService;

  private final ObjectMapper mapper = new ObjectMapper();

  private final Map<ShopItem, Integer> cartItems = new HashMap<>(Map.of(
      new ShopItem(100L, "Test item1", 8.99), 1,
      new ShopItem(101L, "Test item2", 10.99), 3
  ));
  private final Topic cartupdateTopic = new Topic("cartupdate");
  private final Topic priceupdateTopic = new Topic("priceupdate");
  private TotalPrice price = new TotalPrice(10.0, 9.0, 11);

  public CartService(Publisher publisher, Subscriber subscriber,
      ShopperService shopperService) throws SocketException {
    this.publisher = publisher;
    this.subscriber = subscriber;
    this.shopperService = shopperService;
    subscriber.subscribe(this.getClass().getSimpleName(), cartupdateTopic);
    subscriber.subscribe(this.getClass().getSimpleName(), priceupdateTopic);


    CompletableFuture.runAsync(() -> {
          Flux<Message> udpFlux = subscriber.createUdpFlux();

          udpFlux
              .subscribe(
                  this::handleMessage,
                  err -> System.out.println("Error: " + err.getMessage()),
                  () -> {
                    System.out.println("Done!");
                  });
        }
    );
  }

  private void handleMessage(Message message) {
    if (message.getTopic().equals(priceupdateTopic)) {
      handlePriceMessage(message);
    } else if (message.getTopic().equals(cartupdateTopic)) {
      handleCartMessage(message);
    }
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
    publisher.publish(message);
  }

  private void handlePriceMessage(Message message) {
    LOGGER.info("Message received from update price :: " + message);
    String contents = message.getContents();
    try {
      price = mapper.readValue(contents, TotalPrice.class);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing totalprice object :: " + e.getMessage());
    }
  }

  private void handleCartMessage(Message message) {
    LOGGER.info("Message received from update cart :: " + message);
    String contents = message.getContents();
    TypeReference<HashMap<ShopItem, Integer>> typeRef = new TypeReference<>() {};
    Map<ShopItem, Integer> cartMap = null;
    Cart updatedCart = null;
    try {
      updatedCart = mapper.readValue(contents, Cart.class);
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing cart map :: " + e.getMessage());
    }
    if (updatedCart == null) return;
    cartItems.clear();
    cartItems.putAll(updatedCart.getCartItems());
  }

//  @PreDestroy
//  public void close() {
//    subscriber.close();
//    publisher.close();
//  }
}
