package com.google.gson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public final class Streams {
   public static JsonElement parse(JsonReader var0) throws JsonParseException {
      boolean var1 = true;

      try {
         var0.peek();
         var1 = false;
         return (JsonElement)TypeAdapters.JSON_ELEMENT.read(var0);
      } catch (EOFException var3) {
         if (var1) {
            return JsonNull.INSTANCE;
         } else {
            throw new JsonSyntaxException(var3);
         }
      } catch (MalformedJsonException var4) {
         throw new JsonSyntaxException(var4);
      } catch (IOException var5) {
         throw new JsonIOException(var5);
      } catch (NumberFormatException var6) {
         throw new JsonSyntaxException(var6);
      }
   }

   public static void write(JsonElement var0, JsonWriter var1) throws IOException {
      TypeAdapters.JSON_ELEMENT.write(var1, var0);
   }

   public static Writer writerForAppendable(Appendable var0) {
      return (Writer)(var0 instanceof Writer ? (Writer)var0 : new Streams.AppendableWriter(var0));
   }

   private static final class AppendableWriter extends Writer {
      private final Appendable appendable;
      private final Streams.AppendableWriter.CurrentWrite currentWrite;

      private AppendableWriter(Appendable var1) {
         this.currentWrite = new Streams.AppendableWriter.CurrentWrite();
         this.appendable = var1;
      }

      public void write(char[] var1, int var2, int var3) throws IOException {
         this.currentWrite.chars = var1;
         this.appendable.append(this.currentWrite, var2, var2 + var3);
      }

      public void write(int var1) throws IOException {
         this.appendable.append((char)var1);
      }

      public void flush() {
      }

      public void close() {
      }

      AppendableWriter(Appendable var1, Object var2) {
         this(var1);
      }

      static class CurrentWrite implements CharSequence {
         char[] chars;

         public int length() {
            return this.chars.length;
         }

         public char charAt(int var1) {
            return this.chars[var1];
         }

         public CharSequence subSequence(int var1, int var2) {
            return new String(this.chars, var1, var2 - var1);
         }
      }
   }
}
