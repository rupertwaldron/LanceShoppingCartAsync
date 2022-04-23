package com.ruppyrup.shop.controller;

import com.ruppyrup.models.ShopItem;
import com.ruppyrup.shop.dao.ShopItemDao;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShopitemController {

  @Autowired
  private ShopItemDao shopItemDao;

  // http://localhost:8080/shopitems
  @GetMapping("/shopitems")
  public String listArticles(Model model) {
    ShopItem pickedItem = new ShopItem();
    model.addAttribute("pickeditem", pickedItem);
    model.addAttribute("shopitems", shopItemDao.findAllItems());
    return "index";
  }

  @PostMapping("/selectitem")
  public String selectItem(Model model, @RequestParam(name = "item") int id) {
    System.out.println("Id chosen :: " + id);
    return "redirect:/shopitems";
  }

  @PostMapping("/shopitems")
  public String submitForm(@ModelAttribute("pickeditem") ShopItem shopItem) {
    Long nextId = shopItemDao.findAllItems().size() + 1L;
    shopItem.setId(nextId);
    shopItemDao.addShopItem(shopItem);
    System.out.println(shopItem);
    return "redirect:/shopitems";
  }

}
