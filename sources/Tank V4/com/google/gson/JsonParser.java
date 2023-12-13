package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
   public JsonElement parse(String var1) throws JsonSyntaxException {
      return this.parse((Reader)(new StringReader(var1)));
   }

   public JsonElement parse(Reader var1) throws JsonIOException, JsonSyntaxException {
      try {
         JsonReader var2 = new JsonReader(var1);
         JsonElement var3 = this.parse(var2);
         if (!var3.isJsonNull() && var2.peek() != JsonToken.END_DOCUMENT) {
            throw new JsonSyntaxException("Did not consume the entire document.");
         } else {
            return var3;
         }
      } catch (MalformedJsonException var4) {
         throw new JsonSyntaxException(var4);
      } catch (IOException var5) {
         throw new JsonIOException(var5);
      } catch (NumberFormatException var6) {
         throw new JsonSyntaxException(var6);
      }
   }

   public JsonElement parse(JsonReader var1) throws JsonIOException, JsonSyntaxException {
      boolean var2 = var1.isLenient();
      var1.setLenient(true);

      JsonElement var3;
      try {
         var3 = Streams.parse(var1);
      } catch (StackOverflowError var5) {
         throw new JsonParseException("Failed parsing JSON source: " + var1 + " to Json", var5);
      } catch (OutOfMemoryError var6) {
         throw new JsonParseException("Failed parsing JSON source: " + var1 + " to Json", var6);
      }

      var1.setLenient(var2);
      return var3;
   }
}
