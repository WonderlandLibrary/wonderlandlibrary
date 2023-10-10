package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class ObjectTypeAdapter extends TypeAdapter {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public TypeAdapter create(Gson var1, TypeToken var2) {
         return var2.getRawType() == Object.class ? new ObjectTypeAdapter(var1) : null;
      }
   };
   private final Gson gson;

   private ObjectTypeAdapter(Gson var1) {
      this.gson = var1;
   }

   public Object read(JsonReader var1) throws IOException {
      JsonToken var2 = var1.peek();
      switch(var2) {
      case BEGIN_ARRAY:
         ArrayList var3 = new ArrayList();
         var1.beginArray();

         while(var1.hasNext()) {
            var3.add(this.read(var1));
         }

         var1.endArray();
         return var3;
      case BEGIN_OBJECT:
         LinkedTreeMap var4 = new LinkedTreeMap();
         var1.beginObject();

         while(var1.hasNext()) {
            var4.put(var1.nextName(), this.read(var1));
         }

         var1.endObject();
         return var4;
      case STRING:
         return var1.nextString();
      case NUMBER:
         return var1.nextDouble();
      case BOOLEAN:
         return var1.nextBoolean();
      case NULL:
         var1.nextNull();
         return null;
      default:
         throw new IllegalStateException();
      }
   }

   public void write(JsonWriter var1, Object var2) throws IOException {
      if (var2 == null) {
         var1.nullValue();
      } else {
         TypeAdapter var3 = this.gson.getAdapter(var2.getClass());
         if (var3 instanceof ObjectTypeAdapter) {
            var1.beginObject();
            var1.endObject();
         } else {
            var3.write(var1, var2);
         }
      }
   }

   ObjectTypeAdapter(Gson var1, Object var2) {
      this(var1);
   }
}
