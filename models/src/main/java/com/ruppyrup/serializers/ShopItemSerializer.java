package com.ruppyrup.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ruppyrup.models.ShopItem;
import java.io.IOException;
import java.io.StringWriter;

public class ShopItemSerializer extends JsonSerializer<ShopItem> {
  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(ShopItem value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {

    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
