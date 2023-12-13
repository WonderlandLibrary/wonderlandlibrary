package com.google.gson;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter {
   public abstract void write(JsonWriter var1, Object var2) throws IOException;

   public final void toJson(Writer var1, Object var2) throws IOException {
      JsonWriter var3 = new JsonWriter(var1);
      this.write(var3, var2);
   }

   public final TypeAdapter nullSafe() {
      return new TypeAdapter(this) {
         final TypeAdapter this$0;

         {
            this.this$0 = var1;
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            if (var2 == null) {
               var1.nullValue();
            } else {
               this.this$0.write(var1, var2);
            }

         }

         public Object read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return this.this$0.read(var1);
            }
         }
      };
   }

   public final String toJson(Object var1) throws IOException {
      StringWriter var2 = new StringWriter();
      this.toJson(var2, var1);
      return var2.toString();
   }

   public final JsonElement toJsonTree(Object var1) {
      try {
         JsonTreeWriter var2 = new JsonTreeWriter();
         this.write(var2, var1);
         return var2.get();
      } catch (IOException var3) {
         throw new JsonIOException(var3);
      }
   }

   public abstract Object read(JsonReader var1) throws IOException;

   public final Object fromJson(Reader var1) throws IOException {
      JsonReader var2 = new JsonReader(var1);
      return this.read(var2);
   }

   public final Object fromJson(String var1) throws IOException {
      return this.fromJson((Reader)(new StringReader(var1)));
   }

   public final Object fromJsonTree(JsonElement var1) {
      try {
         JsonTreeReader var2 = new JsonTreeReader(var1);
         return this.read(var2);
      } catch (IOException var3) {
         throw new JsonIOException(var3);
      }
   }
}
