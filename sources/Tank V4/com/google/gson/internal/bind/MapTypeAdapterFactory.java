package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class MapTypeAdapterFactory implements TypeAdapterFactory {
   private final ConstructorConstructor constructorConstructor;
   private final boolean complexMapKeySerialization;

   public MapTypeAdapterFactory(ConstructorConstructor var1, boolean var2) {
      this.constructorConstructor = var1;
      this.complexMapKeySerialization = var2;
   }

   public TypeAdapter create(Gson var1, TypeToken var2) {
      Type var3 = var2.getType();
      Class var4 = var2.getRawType();
      if (!Map.class.isAssignableFrom(var4)) {
         return null;
      } else {
         Class var5 = $Gson$Types.getRawType(var3);
         Type[] var6 = $Gson$Types.getMapKeyAndValueTypes(var3, var5);
         TypeAdapter var7 = this.getKeyAdapter(var1, var6[0]);
         TypeAdapter var8 = var1.getAdapter(TypeToken.get(var6[1]));
         ObjectConstructor var9 = this.constructorConstructor.get(var2);
         MapTypeAdapterFactory.Adapter var10 = new MapTypeAdapterFactory.Adapter(this, var1, var6[0], var7, var6[1], var8, var9);
         return var10;
      }
   }

   private TypeAdapter getKeyAdapter(Gson var1, Type var2) {
      return var2 != Boolean.TYPE && var2 != Boolean.class ? var1.getAdapter(TypeToken.get(var2)) : TypeAdapters.BOOLEAN_AS_STRING;
   }

   static boolean access$000(MapTypeAdapterFactory var0) {
      return var0.complexMapKeySerialization;
   }

   private final class Adapter extends TypeAdapter {
      private final TypeAdapter keyTypeAdapter;
      private final TypeAdapter valueTypeAdapter;
      private final ObjectConstructor constructor;
      final MapTypeAdapterFactory this$0;

      public Adapter(MapTypeAdapterFactory var1, Gson var2, Type var3, TypeAdapter var4, Type var5, TypeAdapter var6, ObjectConstructor var7) {
         this.this$0 = var1;
         this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper(var2, var4, var3);
         this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper(var2, var6, var5);
         this.constructor = var7;
      }

      public Map read(JsonReader var1) throws IOException {
         JsonToken var2 = var1.peek();
         if (var2 == JsonToken.NULL) {
            var1.nextNull();
            return null;
         } else {
            Map var3 = (Map)this.constructor.construct();
            Object var4;
            Object var5;
            Object var6;
            if (var2 == JsonToken.BEGIN_ARRAY) {
               var1.beginArray();

               while(var1.hasNext()) {
                  var1.beginArray();
                  var4 = this.keyTypeAdapter.read(var1);
                  var5 = this.valueTypeAdapter.read(var1);
                  var6 = var3.put(var4, var5);
                  if (var6 != null) {
                     throw new JsonSyntaxException("duplicate key: " + var4);
                  }

                  var1.endArray();
               }

               var1.endArray();
            } else {
               var1.beginObject();

               while(var1.hasNext()) {
                  JsonReaderInternalAccess.INSTANCE.promoteNameToValue(var1);
                  var4 = this.keyTypeAdapter.read(var1);
                  var5 = this.valueTypeAdapter.read(var1);
                  var6 = var3.put(var4, var5);
                  if (var6 != null) {
                     throw new JsonSyntaxException("duplicate key: " + var4);
                  }
               }

               var1.endObject();
            }

            return var3;
         }
      }

      public void write(JsonWriter var1, Map var2) throws IOException {
         if (var2 == null) {
            var1.nullValue();
         } else if (!MapTypeAdapterFactory.access$000(this.this$0)) {
            var1.beginObject();
            Iterator var9 = var2.entrySet().iterator();

            while(var9.hasNext()) {
               Entry var10 = (Entry)var9.next();
               var1.name(String.valueOf(var10.getKey()));
               this.valueTypeAdapter.write(var1, var10.getValue());
            }

            var1.endObject();
         } else {
            boolean var3 = false;
            ArrayList var4 = new ArrayList(var2.size());
            ArrayList var5 = new ArrayList(var2.size());

            JsonElement var8;
            for(Iterator var6 = var2.entrySet().iterator(); var6.hasNext(); var3 |= var8.isJsonArray() || var8.isJsonObject()) {
               Entry var7 = (Entry)var6.next();
               var8 = this.keyTypeAdapter.toJsonTree(var7.getKey());
               var4.add(var8);
               var5.add(var7.getValue());
            }

            int var11;
            if (var3) {
               var1.beginArray();

               for(var11 = 0; var11 < var4.size(); ++var11) {
                  var1.beginArray();
                  Streams.write((JsonElement)var4.get(var11), var1);
                  this.valueTypeAdapter.write(var1, var5.get(var11));
                  var1.endArray();
               }

               var1.endArray();
            } else {
               var1.beginObject();

               for(var11 = 0; var11 < var4.size(); ++var11) {
                  JsonElement var12 = (JsonElement)var4.get(var11);
                  var1.name(this.keyToString(var12));
                  this.valueTypeAdapter.write(var1, var5.get(var11));
               }

               var1.endObject();
            }

         }
      }

      private String keyToString(JsonElement var1) {
         if (var1.isJsonPrimitive()) {
            JsonPrimitive var2 = var1.getAsJsonPrimitive();
            if (var2.isNumber()) {
               return String.valueOf(var2.getAsNumber());
            } else if (var2.isBoolean()) {
               return Boolean.toString(var2.getAsBoolean());
            } else if (var2.isString()) {
               return var2.getAsString();
            } else {
               throw new AssertionError();
            }
         } else if (var1.isJsonNull()) {
            return "null";
         } else {
            throw new AssertionError();
         }
      }

      public Object read(JsonReader var1) throws IOException {
         return this.read(var1);
      }

      public void write(JsonWriter var1, Object var2) throws IOException {
         this.write(var1, (Map)var2);
      }
   }
}
