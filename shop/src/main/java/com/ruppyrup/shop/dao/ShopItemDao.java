package com.ruppyrup.shop.dao;

import com.ruppyrup.models.ShopItem;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ShopItemDao {

  private final Map<Long, ShopItem> shopItems = new HashMap<>(Map.of(
      1L, new ShopItem(1L, "Hair Dryer", 120.00),
      2L, new ShopItem(2L, "C++ Programming for beginners", 34.99),
      3L, new ShopItem(3L, "Kitchen Bin", 47.89),
      4L, new ShopItem(4L, "Shin Pads", 12.50),
      5L, new ShopItem(5L, "Mac Book Pro", 1759.00),
      6L, new ShopItem(6L, "Wrangler Jeans", 89.10)
  ));

  public Collection<ShopItem> findAllItems() {
    return shopItems.values();
  }

  public Optional<ShopItem> findShopItemById(Long id) {
    return Optional.ofNullable(shopItems.get(id));
  }

  public void addShopItem(ShopItem shopItem) {
    int nextId = shopItems.size() + 1;
    shopItems.put((long) nextId, shopItem);
  }

  public void updateShopItem(Long id, ShopItem shopItem) {
    shopItems.put(id, shopItem);
  }

  public void removeShopItem(Long id) {
    shopItems.remove(id);
  }

}
