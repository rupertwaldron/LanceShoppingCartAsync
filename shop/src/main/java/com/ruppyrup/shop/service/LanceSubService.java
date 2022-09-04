package com.ruppyrup.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.lance.models.Message;
import com.ruppyrup.lance.models.Topic;
import com.ruppyrup.lance.subscriber.Subscriber;
import com.ruppyrup.shop.handlers.MessageHandler;
import java.util.List;
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
  private final List<MessageHandler> handlers;
  private final ObjectMapper mapper = new ObjectMapper();
  private final Topic cartupdateTopic = new Topic("cartupdate");
  private final Topic priceupdateTopic = new Topic("priceupdate");

  public LanceSubService(Subscriber subscriber, CartService cartService,
      List<MessageHandler> handlers){
    this.subscriber = subscriber;
    this.cartService = cartService;
    this.handlers = handlers;
    subscriber.subscribe(this.getClass().getSimpleName(), cartupdateTopic);
    subscriber.subscribe(this.getClass().getSimpleName(), priceupdateTopic);

    CompletableFuture<Void> subcf = CompletableFuture.runAsync(() -> {
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
//    subcf.join();
  }

  private void handleMessage(Message message) {
      handlers.forEach(handler -> handler.handleMessage(message, cartService));
  }
}
