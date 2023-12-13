package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer {
   Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException;
}
