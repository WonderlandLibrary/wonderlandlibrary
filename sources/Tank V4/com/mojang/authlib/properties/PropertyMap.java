package com.mojang.authlib.properties;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;

public class PropertyMap extends ForwardingMultimap {
   private final Multimap properties = LinkedHashMultimap.create();

   protected Multimap delegate() {
      return this.properties;
   }

   protected Object delegate() {
      return this.delegate();
   }

   public static class Serializer implements JsonSerializer, JsonDeserializer {
      public PropertyMap deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         PropertyMap var4 = new PropertyMap();
         if (var1 instanceof JsonObject) {
            JsonObject var5 = (JsonObject)var1;
            Iterator var6 = var5.entrySet().iterator();

            while(true) {
               Entry var7;
               do {
                  if (!var6.hasNext()) {
                     return var4;
                  }

                  var7 = (Entry)var6.next();
               } while(!(var7.getValue() instanceof JsonArray));

               Iterator var8 = ((JsonArray)var7.getValue()).iterator();

               while(var8.hasNext()) {
                  JsonElement var9 = (JsonElement)var8.next();
                  var4.put(var7.getKey(), new Property((String)var7.getKey(), var9.getAsString()));
               }
            }
         } else if (var1 instanceof JsonArray) {
            Iterator var10 = ((JsonArray)var1).iterator();

            while(var10.hasNext()) {
               JsonElement var11 = (JsonElement)var10.next();
               if (var11 instanceof JsonObject) {
                  JsonObject var12 = (JsonObject)var11;
                  String var13 = var12.getAsJsonPrimitive("name").getAsString();
                  String var14 = var12.getAsJsonPrimitive("value").getAsString();
                  if (var12.has("signature")) {
                     var4.put(var13, new Property(var13, var14, var12.getAsJsonPrimitive("signature").getAsString()));
                  } else {
                     var4.put(var13, new Property(var13, var14));
                  }
               }
            }
         }

         return var4;
      }

      public JsonElement serialize(PropertyMap var1, Type var2, JsonSerializationContext var3) {
         JsonArray var4 = new JsonArray();

         JsonObject var7;
         for(Iterator var5 = var1.values().iterator(); var5.hasNext(); var4.add(var7)) {
            Property var6 = (Property)var5.next();
            var7 = new JsonObject();
            var7.addProperty("name", var6.getName());
            var7.addProperty("value", var6.getValue());
            if (var6.hasSignature()) {
               var7.addProperty("signature", var6.getSignature());
            }
         }

         return var4;
      }

      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.serialize((PropertyMap)var1, var2, var3);
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }
   }
}
