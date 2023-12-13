package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class JsonTreeReader extends JsonReader {
   private static final Reader UNREADABLE_READER = new Reader() {
      public int read(char[] var1, int var2, int var3) throws IOException {
         throw new AssertionError();
      }

      public void close() throws IOException {
         throw new AssertionError();
      }
   };
   private static final Object SENTINEL_CLOSED = new Object();
   private final List stack = new ArrayList();

   public JsonTreeReader(JsonElement var1) {
      super(UNREADABLE_READER);
      this.stack.add(var1);
   }

   public void beginArray() throws IOException {
      this.expect(JsonToken.BEGIN_ARRAY);
      JsonArray var1 = (JsonArray)this.peekStack();
      this.stack.add(var1.iterator());
   }

   public void endArray() throws IOException {
      this.expect(JsonToken.END_ARRAY);
      this.popStack();
      this.popStack();
   }

   public void beginObject() throws IOException {
      this.expect(JsonToken.BEGIN_OBJECT);
      JsonObject var1 = (JsonObject)this.peekStack();
      this.stack.add(var1.entrySet().iterator());
   }

   public void endObject() throws IOException {
      this.expect(JsonToken.END_OBJECT);
      this.popStack();
      this.popStack();
   }

   public boolean hasNext() throws IOException {
      JsonToken var1 = this.peek();
      return var1 != JsonToken.END_OBJECT && var1 != JsonToken.END_ARRAY;
   }

   public JsonToken peek() throws IOException {
      if (this.stack.isEmpty()) {
         return JsonToken.END_DOCUMENT;
      } else {
         Object var1 = this.peekStack();
         if (var1 instanceof Iterator) {
            boolean var4 = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            Iterator var3 = (Iterator)var1;
            if (var3.hasNext()) {
               if (var4) {
                  return JsonToken.NAME;
               } else {
                  this.stack.add(var3.next());
                  return this.peek();
               }
            } else {
               return var4 ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            }
         } else if (var1 instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
         } else if (var1 instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
         } else if (var1 instanceof JsonPrimitive) {
            JsonPrimitive var2 = (JsonPrimitive)var1;
            if (var2.isString()) {
               return JsonToken.STRING;
            } else if (var2.isBoolean()) {
               return JsonToken.BOOLEAN;
            } else if (var2.isNumber()) {
               return JsonToken.NUMBER;
            } else {
               throw new AssertionError();
            }
         } else if (var1 instanceof JsonNull) {
            return JsonToken.NULL;
         } else if (var1 == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
         } else {
            throw new AssertionError();
         }
      }
   }

   private Object peekStack() {
      return this.stack.get(this.stack.size() - 1);
   }

   private Object popStack() {
      return this.stack.remove(this.stack.size() - 1);
   }

   private void expect(JsonToken var1) throws IOException {
      if (this.peek() != var1) {
         throw new IllegalStateException("Expected " + var1 + " but was " + this.peek());
      }
   }

   public String nextName() throws IOException {
      this.expect(JsonToken.NAME);
      Iterator var1 = (Iterator)this.peekStack();
      Entry var2 = (Entry)var1.next();
      this.stack.add(var2.getValue());
      return (String)var2.getKey();
   }

   public String nextString() throws IOException {
      JsonToken var1 = this.peek();
      if (var1 != JsonToken.STRING && var1 != JsonToken.NUMBER) {
         throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + var1);
      } else {
         return ((JsonPrimitive)this.popStack()).getAsString();
      }
   }

   public boolean nextBoolean() throws IOException {
      this.expect(JsonToken.BOOLEAN);
      return ((JsonPrimitive)this.popStack()).getAsBoolean();
   }

   public void nextNull() throws IOException {
      this.expect(JsonToken.NULL);
      this.popStack();
   }

   public double nextDouble() throws IOException {
      JsonToken var1 = this.peek();
      if (var1 != JsonToken.NUMBER && var1 != JsonToken.STRING) {
         throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + var1);
      } else {
         double var2 = ((JsonPrimitive)this.peekStack()).getAsDouble();
         if (this.isLenient() || !Double.isNaN(var2) && !Double.isInfinite(var2)) {
            this.popStack();
            return var2;
         } else {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + var2);
         }
      }
   }

   public long nextLong() throws IOException {
      JsonToken var1 = this.peek();
      if (var1 != JsonToken.NUMBER && var1 != JsonToken.STRING) {
         throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + var1);
      } else {
         long var2 = ((JsonPrimitive)this.peekStack()).getAsLong();
         this.popStack();
         return var2;
      }
   }

   public int nextInt() throws IOException {
      JsonToken var1 = this.peek();
      if (var1 != JsonToken.NUMBER && var1 != JsonToken.STRING) {
         throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + var1);
      } else {
         int var2 = ((JsonPrimitive)this.peekStack()).getAsInt();
         this.popStack();
         return var2;
      }
   }

   public void close() throws IOException {
      this.stack.clear();
      this.stack.add(SENTINEL_CLOSED);
   }

   public void skipValue() throws IOException {
      if (this.peek() == JsonToken.NAME) {
         this.nextName();
      } else {
         this.popStack();
      }

   }

   public String toString() {
      return this.getClass().getSimpleName();
   }

   public void promoteNameToValue() throws IOException {
      this.expect(JsonToken.NAME);
      Iterator var1 = (Iterator)this.peekStack();
      Entry var2 = (Entry)var1.next();
      this.stack.add(var2.getValue());
      this.stack.add(new JsonPrimitive((String)var2.getKey()));
   }
}
