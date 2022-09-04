package com.ruppyrup.shop.handlers;

import com.ruppyrup.lance.models.Message;
import com.ruppyrup.shop.service.CartService;

public interface MessageHandler {

  void handleMessage(Message message, CartService cartService);
}
