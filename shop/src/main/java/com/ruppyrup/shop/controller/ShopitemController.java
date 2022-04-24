package com.ruppyrup.shop.controller;

import com.ruppyrup.models.ShopItem;
import com.ruppyrup.shop.dao.ShopItemDao;
import com.ruppyrup.shop.service.CartService;
import com.ruppyrup.shop.service.ShopperService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopitemController {

  private final ShopItemDao shopItemDao;

  private final CartService cartService;

  private final ShopperService shopperService;

  public ShopitemController(ShopItemDao shopItemDao, CartService cartService,
      ShopperService shopperService) {
    this.shopItemDao = shopItemDao;
    this.cartService = cartService;
    this.shopperService = shopperService;
  }

  // http://localhost:8080/shopitems
  @GetMapping("/shopitems")
  public String listShopItems(final Model model) {
    ShopItem pickedItem = new ShopItem();
    model.addAttribute("pickeditem", pickedItem);
    model.addAttribute("shopitems", shopItemDao.findAllItems());
    model.addAttribute("cartitems", cartService.getCartItems());
    return "index";
  }

  @PostMapping("/selectitem")
  public String selectItem(final @RequestParam(name = "item") int id) {
    System.out.println("Id chosen :: " + id);
    Optional<ShopItem> shopItemById = shopItemDao.findShopItemById((long) id);
    shopItemById.ifPresent(cartService::addCartItem);
    return "redirect:/shopitems";
  }

  @PostMapping("/shopitems")
  public String submitForm(final @ModelAttribute("pickeditem") ShopItem shopItem) {
    Long nextId = shopItemDao.findAllItems().size() + 1L;
    ShopItem newShopItem = new ShopItem(nextId, shopItem.getName(), shopItem.getPrice());
    shopItemDao.addShopItem(newShopItem);
    System.out.println(newShopItem);
    return "redirect:/shopitems";
  }
}
