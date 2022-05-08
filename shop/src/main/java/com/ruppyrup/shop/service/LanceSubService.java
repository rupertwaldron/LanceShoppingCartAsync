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
public class LanceSubService {

  private static final Logger LOGGER = Logger.getLogger(LanceSubService.class.getName());

  private final Subscriber subscriber;
  private final CartService cartService;
  private final ObjectMapper mapper = new ObjectMapper();
  private final Topic cartupdateTopic = new Topic("cartupdate");
  private final Topic priceupdateTopic = new Topic("priceupdate");

  public LanceSubService(Subscriber subscriber, CartService cartService){
    this.subscriber = subscriber;
    this.cartService = cartService;
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
      handlePriceMessage(message, cartService);
      handleCartMessage(message, cartService);
  }

  private void handlePriceMessage(Message message, CartService cartService) {
    LOGGER.info("Message received from update price :: " + message);
    String contents = message.getContents();
    try {
      cartService.setPrice(mapper.readValue(contents, TotalPrice.class));
    } catch (JsonProcessingException e) {
      LOGGER.warning("Error deserializing totalprice object :: " + e.getMessage());
    }
  }

  private void handleCartMessage(Message message, CartService cartService) {
    LOGGER.info("Message received from update cart :: " + message);
    String contents = message.getContents();
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
