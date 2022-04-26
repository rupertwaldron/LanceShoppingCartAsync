package com.ruppyrup.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ruppyrup.serializers.ShopItemDeSerializer;
import com.ruppyrup.serializers.ShopItemSerializer;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Cart {

  @JsonSerialize(keyUsing = ShopItemSerializer.class)
  @JsonDeserialize(keyUsing = ShopItemDeSerializer.class)
  private final Map<ShopItem, Integer> cartItems = new HashMap<>();

  public Cart() {
  }

  public void addCartItem(final ShopItem shopItem) {
    if (cartItems.containsKey(shopItem)) {
      Integer quantity = cartItems.get(shopItem);
      cartItems.replace(shopItem, ++quantity);
    } else {
      cartItems.put(shopItem, 1);
    }
  }
}
