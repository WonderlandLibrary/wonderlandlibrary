package net.minecraft.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {
   private String func_151232_a(Object var1) {
      return var1 instanceof Enum ? ((Enum)var1).name().toLowerCase(Locale.US) : var1.toString().toLowerCase(Locale.US);
   }

   static String access$0(EnumTypeAdapterFactory var0, Object var1) {
      return var0.func_151232_a(var1);
   }

   public TypeAdapter create(Gson var1, TypeToken var2) {
      Class var3 = var2.getRawType();
      if (!var3.isEnum()) {
         return null;
      } else {
         HashMap var4 = Maps.newHashMap();
         Object[] var8;
         int var7 = (var8 = var3.getEnumConstants()).length;

         for(int var6 = 0; var6 < var7; ++var6) {
            Object var5 = var8[var6];
            var4.put(this.func_151232_a(var5), var5);
         }

         return new TypeAdapter(this, var4) {
            final EnumTypeAdapterFactory this$0;
            private final Map val$map;

            public Object read(JsonReader var1) throws IOException {
               if (var1.peek() == JsonToken.NULL) {
                  var1.nextNull();
                  return null;
               } else {
                  return this.val$map.get(var1.nextString());
               }
            }

            public void write(JsonWriter var1, Object var2) throws IOException {
               if (var2 == null) {
                  var1.nullValue();
               } else {
                  var1.value(EnumTypeAdapterFactory.access$0(this.this$0, var2));
               }

            }

            {
               this.this$0 = var1;
               this.val$map = var2;
            }
         };
      }
   }
}
