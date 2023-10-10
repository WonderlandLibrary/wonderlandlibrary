package com.google.gson;

public final class JsonNull extends JsonElement {
   public static final JsonNull INSTANCE = new JsonNull();

   JsonNull deepCopy() {
      return INSTANCE;
   }

   public int hashCode() {
      return JsonNull.class.hashCode();
   }

   public boolean equals(Object var1) {
      return this == var1 || var1 instanceof JsonNull;
   }

   JsonElement deepCopy() {
      return this.deepCopy();
   }
}
