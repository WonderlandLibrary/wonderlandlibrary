package com.google.gson;

public enum LongSerializationPolicy {
   DEFAULT {
      public JsonElement serialize(Long var1) {
         return new JsonPrimitive(var1);
      }
   },
   STRING {
      public JsonElement serialize(Long var1) {
         return new JsonPrimitive(String.valueOf(var1));
      }
   };

   private static final LongSerializationPolicy[] $VALUES = new LongSerializationPolicy[]{DEFAULT, STRING};

   private LongSerializationPolicy() {
   }

   public abstract JsonElement serialize(Long var1);

   LongSerializationPolicy(Object var3) {
      this();
   }
}
