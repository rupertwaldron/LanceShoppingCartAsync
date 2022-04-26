package com.ruppyrup.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ruppyrup.models.ShopItem;
import java.io.IOException;
import java.io.StringWriter;

public class ShopItemDeSerializer extends KeyDeserializer {

  private ObjectMapper mapper = new ObjectMapper();
  @Override
  public ShopItem deserializeKey(String key, DeserializationContext ctxt) throws IOException {
    ShopItem shopItem = mapper.readValue(key, ShopItem.class);
    return shopItem;
  }
}
