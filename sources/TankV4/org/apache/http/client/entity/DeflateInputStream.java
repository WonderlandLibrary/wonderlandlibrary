package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class DeflateInputStream extends InputStream {
   private InputStream sourceStream;

   public DeflateInputStream(InputStream var1) throws IOException {
      byte[] var2 = new byte[6];
      PushbackInputStream var3 = new PushbackInputStream(var1, var2.length);
      int var4 = var3.read(var2);
      if (var4 == -1) {
         throw new IOException("Unable to read the response");
      } else {
         byte[] var5 = new byte[1];
         Inflater var6 = new Inflater();

         try {
            int var7;
            while((var7 = var6.inflate(var5)) == 0) {
               if (var6.finished()) {
                  throw new IOException("Unable to read the response");
               }

               if (var6.needsDictionary()) {
                  break;
               }

               if (var6.needsInput()) {
                  var6.setInput(var2);
               }
            }

            if (var7 == -1) {
               throw new IOException("Unable to read the response");
            }

            var3.unread(var2, 0, var4);
            this.sourceStream = new DeflateInputStream.DeflateStream(var3, new Inflater());
         } catch (DataFormatException var9) {
            var3.unread(var2, 0, var4);
            this.sourceStream = new DeflateInputStream.DeflateStream(var3, new Inflater(true));
            var6.end();
            return;
         }

         var6.end();
      }
   }

   public int read() throws IOException {
      return this.sourceStream.read();
   }

   public int read(byte[] var1) throws IOException {
      return this.sourceStream.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.sourceStream.read(var1, var2, var3);
   }

   public long skip(long var1) throws IOException {
      return this.sourceStream.skip(var1);
   }

   public int available() throws IOException {
      return this.sourceStream.available();
   }

   public void mark(int var1) {
      this.sourceStream.mark(var1);
   }

   public void reset() throws IOException {
      this.sourceStream.reset();
   }

   public boolean markSupported() {
      return this.sourceStream.markSupported();
   }

   public void close() throws IOException {
      this.sourceStream.close();
   }

   static class DeflateStream extends InflaterInputStream {
      private boolean closed = false;

      public DeflateStream(InputStream var1, Inflater var2) {
         super(var1, var2);
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            this.inf.end();
            super.close();
         }
      }
   }
}
