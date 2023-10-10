package com.mojang.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

public class UUIDTypeAdapter extends TypeAdapter {
   public void write(JsonWriter var1, UUID var2) throws IOException {
      var1.value(fromUUID(var2));
   }

   public UUID read(JsonReader var1) throws IOException {
      return fromString(var1.nextString());
   }

   public static String fromUUID(UUID var0) {
      return var0.toString().replace("-", "");
   }

   public static UUID fromString(String var0) {
      return UUID.fromString(var0.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
   }

   public Object read(JsonReader var1) throws IOException {
      return this.read(var1);
   }

   public void write(JsonWriter var1, Object var2) throws IOException {
      this.write(var1, (UUID)var2);
   }
}
