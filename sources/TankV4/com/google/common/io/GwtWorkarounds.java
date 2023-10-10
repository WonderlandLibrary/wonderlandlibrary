package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

@GwtCompatible(
   emulated = true
)
final class GwtWorkarounds {
   private GwtWorkarounds() {
   }

   @GwtIncompatible("Reader")
   static GwtWorkarounds.CharInput asCharInput(Reader var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharInput(var0) {
         final Reader val$reader;

         {
            this.val$reader = var1;
         }

         public int read() throws IOException {
            return this.val$reader.read();
         }

         public void close() throws IOException {
            this.val$reader.close();
         }
      };
   }

   static GwtWorkarounds.CharInput asCharInput(CharSequence var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharInput(var0) {
         int index;
         final CharSequence val$chars;

         {
            this.val$chars = var1;
            this.index = 0;
         }

         public int read() {
            return this.index < this.val$chars.length() ? this.val$chars.charAt(this.index++) : -1;
         }

         public void close() {
            this.index = this.val$chars.length();
         }
      };
   }

   @GwtIncompatible("InputStream")
   static InputStream asInputStream(GwtWorkarounds.ByteInput var0) {
      Preconditions.checkNotNull(var0);
      return new InputStream(var0) {
         final GwtWorkarounds.ByteInput val$input;

         {
            this.val$input = var1;
         }

         public int read() throws IOException {
            return this.val$input.read();
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            Preconditions.checkNotNull(var1);
            Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
            if (var3 == 0) {
               return 0;
            } else {
               int var4 = this.read();
               if (var4 == -1) {
                  return -1;
               } else {
                  var1[var2] = (byte)var4;

                  for(int var5 = 1; var5 < var3; ++var5) {
                     int var6 = this.read();
                     if (var6 == -1) {
                        return var5;
                     }

                     var1[var2 + var5] = (byte)var6;
                  }

                  return var3;
               }
            }
         }

         public void close() throws IOException {
            this.val$input.close();
         }
      };
   }

   @GwtIncompatible("OutputStream")
   static OutputStream asOutputStream(GwtWorkarounds.ByteOutput var0) {
      Preconditions.checkNotNull(var0);
      return new OutputStream(var0) {
         final GwtWorkarounds.ByteOutput val$output;

         {
            this.val$output = var1;
         }

         public void write(int var1) throws IOException {
            this.val$output.write((byte)var1);
         }

         public void flush() throws IOException {
            this.val$output.flush();
         }

         public void close() throws IOException {
            this.val$output.close();
         }
      };
   }

   @GwtIncompatible("Writer")
   static GwtWorkarounds.CharOutput asCharOutput(Writer var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharOutput(var0) {
         final Writer val$writer;

         {
            this.val$writer = var1;
         }

         public void write(char var1) throws IOException {
            this.val$writer.append(var1);
         }

         public void flush() throws IOException {
            this.val$writer.flush();
         }

         public void close() throws IOException {
            this.val$writer.close();
         }
      };
   }

   static GwtWorkarounds.CharOutput stringBuilderOutput(int var0) {
      StringBuilder var1 = new StringBuilder(var0);
      return new GwtWorkarounds.CharOutput(var1) {
         final StringBuilder val$builder;

         {
            this.val$builder = var1;
         }

         public void write(char var1) {
            this.val$builder.append(var1);
         }

         public void flush() {
         }

         public void close() {
         }

         public String toString() {
            return this.val$builder.toString();
         }
      };
   }

   interface CharOutput {
      void write(char var1) throws IOException;

      void flush() throws IOException;

      void close() throws IOException;
   }

   interface ByteOutput {
      void write(byte var1) throws IOException;

      void flush() throws IOException;

      void close() throws IOException;
   }

   interface ByteInput {
      int read() throws IOException;

      void close() throws IOException;
   }

   interface CharInput {
      int read() throws IOException;

      void close() throws IOException;
   }
}
