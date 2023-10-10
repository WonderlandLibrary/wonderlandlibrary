package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter implements Closeable, Flushable {
   private static final String[] REPLACEMENT_CHARS = new String[128];
   private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
   private final Writer out;
   private int[] stack = new int[32];
   private int stackSize = 0;
   private String indent;
   private String separator;
   private boolean lenient;
   private boolean htmlSafe;
   private String deferredName;
   private boolean serializeNulls;

   public JsonWriter(Writer var1) {
      this.push(6);
      this.separator = ":";
      this.serializeNulls = true;
      if (var1 == null) {
         throw new NullPointerException("out == null");
      } else {
         this.out = var1;
      }
   }

   public final void setIndent(String var1) {
      if (var1.length() == 0) {
         this.indent = null;
         this.separator = ":";
      } else {
         this.indent = var1;
         this.separator = ": ";
      }

   }

   public final void setLenient(boolean var1) {
      this.lenient = var1;
   }

   public boolean isLenient() {
      return this.lenient;
   }

   public final void setHtmlSafe(boolean var1) {
      this.htmlSafe = var1;
   }

   public final boolean isHtmlSafe() {
      return this.htmlSafe;
   }

   public final void setSerializeNulls(boolean var1) {
      this.serializeNulls = var1;
   }

   public final boolean getSerializeNulls() {
      return this.serializeNulls;
   }

   public JsonWriter beginArray() throws IOException {
      this.writeDeferredName();
      return this.open(1, "[");
   }

   public JsonWriter endArray() throws IOException {
      return this.close(1, 2, "]");
   }

   public JsonWriter beginObject() throws IOException {
      this.writeDeferredName();
      return this.open(3, "{");
   }

   public JsonWriter endObject() throws IOException {
      return this.close(3, 5, "}");
   }

   private JsonWriter open(int var1, String var2) throws IOException {
      this.beforeValue(true);
      this.push(var1);
      this.out.write(var2);
      return this;
   }

   private JsonWriter close(int var1, int var2, String var3) throws IOException {
      int var4 = this.peek();
      if (var4 != var2 && var4 != var1) {
         throw new IllegalStateException("Nesting problem.");
      } else if (this.deferredName != null) {
         throw new IllegalStateException("Dangling name: " + this.deferredName);
      } else {
         --this.stackSize;
         if (var4 == var2) {
            this.newline();
         }

         this.out.write(var3);
         return this;
      }
   }

   private void push(int var1) {
      if (this.stackSize == this.stack.length) {
         int[] var2 = new int[this.stackSize * 2];
         System.arraycopy(this.stack, 0, var2, 0, this.stackSize);
         this.stack = var2;
      }

      this.stack[this.stackSize++] = var1;
   }

   private int peek() {
      if (this.stackSize == 0) {
         throw new IllegalStateException("JsonWriter is closed.");
      } else {
         return this.stack[this.stackSize - 1];
      }
   }

   private void replaceTop(int var1) {
      this.stack[this.stackSize - 1] = var1;
   }

   public JsonWriter name(String var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("name == null");
      } else if (this.deferredName != null) {
         throw new IllegalStateException();
      } else if (this.stackSize == 0) {
         throw new IllegalStateException("JsonWriter is closed.");
      } else {
         this.deferredName = var1;
         return this;
      }
   }

   private void writeDeferredName() throws IOException {
      if (this.deferredName != null) {
         this.beforeName();
         this.string(this.deferredName);
         this.deferredName = null;
      }

   }

   public JsonWriter value(String var1) throws IOException {
      if (var1 == null) {
         return this.nullValue();
      } else {
         this.writeDeferredName();
         this.beforeValue(false);
         this.string(var1);
         return this;
      }
   }

   public JsonWriter nullValue() throws IOException {
      if (this.deferredName != null) {
         if (!this.serializeNulls) {
            this.deferredName = null;
            return this;
         }

         this.writeDeferredName();
      }

      this.beforeValue(false);
      this.out.write("null");
      return this;
   }

   public JsonWriter value(boolean var1) throws IOException {
      this.writeDeferredName();
      this.beforeValue(false);
      this.out.write(var1 ? "true" : "false");
      return this;
   }

   public JsonWriter value(double var1) throws IOException {
      if (!Double.isNaN(var1) && !Double.isInfinite(var1)) {
         this.writeDeferredName();
         this.beforeValue(false);
         this.out.append(Double.toString(var1));
         return this;
      } else {
         throw new IllegalArgumentException("Numeric values must be finite, but was " + var1);
      }
   }

   public JsonWriter value(long var1) throws IOException {
      this.writeDeferredName();
      this.beforeValue(false);
      this.out.write(Long.toString(var1));
      return this;
   }

   public JsonWriter value(Number var1) throws IOException {
      if (var1 == null) {
         return this.nullValue();
      } else {
         this.writeDeferredName();
         String var2 = var1.toString();
         if (this.lenient || !var2.equals("-Infinity") && !var2.equals("Infinity") && !var2.equals("NaN")) {
            this.beforeValue(false);
            this.out.append(var2);
            return this;
         } else {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + var1);
         }
      }
   }

   public void flush() throws IOException {
      if (this.stackSize == 0) {
         throw new IllegalStateException("JsonWriter is closed.");
      } else {
         this.out.flush();
      }
   }

   public void close() throws IOException {
      this.out.close();
      int var1 = this.stackSize;
      if (var1 <= 1 && (var1 != 1 || this.stack[var1 - 1] == 7)) {
         this.stackSize = 0;
      } else {
         throw new IOException("Incomplete document");
      }
   }

   private void string(String var1) throws IOException {
      String[] var2 = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
      this.out.write("\"");
      int var3 = 0;
      int var4 = var1.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         char var6 = var1.charAt(var5);
         String var7;
         if (var6 < 128) {
            var7 = var2[var6];
            if (var7 == null) {
               continue;
            }
         } else if (var6 == 8232) {
            var7 = "\\u2028";
         } else {
            if (var6 != 8233) {
               continue;
            }

            var7 = "\\u2029";
         }

         if (var3 < var5) {
            this.out.write(var1, var3, var5 - var3);
         }

         this.out.write(var7);
         var3 = var5 + 1;
      }

      if (var3 < var4) {
         this.out.write(var1, var3, var4 - var3);
      }

      this.out.write("\"");
   }

   private void newline() throws IOException {
      if (this.indent != null) {
         this.out.write("\n");
         int var1 = 1;

         for(int var2 = this.stackSize; var1 < var2; ++var1) {
            this.out.write(this.indent);
         }

      }
   }

   private void beforeName() throws IOException {
      int var1 = this.peek();
      if (var1 == 5) {
         this.out.write(44);
      } else if (var1 != 3) {
         throw new IllegalStateException("Nesting problem.");
      }

      this.newline();
      this.replaceTop(4);
   }

   private void beforeValue(boolean var1) throws IOException {
      switch(this.peek()) {
      case 1:
         this.replaceTop(2);
         this.newline();
         break;
      case 2:
         this.out.append(',');
         this.newline();
         break;
      case 3:
      case 5:
      default:
         throw new IllegalStateException("Nesting problem.");
      case 4:
         this.out.append(this.separator);
         this.replaceTop(5);
         break;
      case 7:
         if (!this.lenient) {
            throw new IllegalStateException("JSON must have only one top-level value.");
         }
      case 6:
         if (!this.lenient && !var1) {
            throw new IllegalStateException("JSON must start with an array or an object.");
         }

         this.replaceTop(7);
      }

   }

   static {
      for(int var0 = 0; var0 <= 31; ++var0) {
         REPLACEMENT_CHARS[var0] = String.format("\\u%04x", var0);
      }

      REPLACEMENT_CHARS[34] = "\\\"";
      REPLACEMENT_CHARS[92] = "\\\\";
      REPLACEMENT_CHARS[9] = "\\t";
      REPLACEMENT_CHARS[8] = "\\b";
      REPLACEMENT_CHARS[10] = "\\n";
      REPLACEMENT_CHARS[13] = "\\r";
      REPLACEMENT_CHARS[12] = "\\f";
      HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone();
      HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
      HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
      HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
      HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
      HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
   }
}
