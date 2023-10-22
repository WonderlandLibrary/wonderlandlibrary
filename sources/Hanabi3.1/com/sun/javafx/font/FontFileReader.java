package com.sun.javafx.font;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedActionException;

class FontFileReader implements FontConstants {
   String filename;
   long filesize;
   RandomAccessFile raFile;
   private static final int READBUFFERSIZE = 1024;
   private byte[] readBuffer;
   private int readBufferLen;
   private int readBufferStart;

   public FontFileReader(String var1) {
      this.filename = var1;
   }

   public String getFilename() {
      return this.filename;
   }

   public synchronized boolean openFile() throws PrivilegedActionException {
      if (this.raFile != null) {
         return false;
      } else {
         this.raFile = (RandomAccessFile)AccessController.doPrivileged(() -> {
            try {
               return new RandomAccessFile(this.filename, "r");
            } catch (FileNotFoundException var2) {
               return null;
            }
         });
         if (this.raFile != null) {
            try {
               this.filesize = this.raFile.length();
               return true;
            } catch (IOException var2) {
            }
         }

         return false;
      }
   }

   public synchronized void closeFile() throws IOException {
      if (this.raFile != null) {
         this.raFile.close();
         this.raFile = null;
         this.readBuffer = null;
      }

   }

   public synchronized long getLength() {
      return this.filesize;
   }

   public synchronized void reset() throws IOException {
      if (this.raFile != null) {
         this.raFile.seek(0L);
      }

   }

   private synchronized int readFromFile(byte[] var1, long var2, int var4) {
      try {
         this.raFile.seek(var2);
         int var5 = this.raFile.read(var1, 0, var4);
         return var5;
      } catch (IOException var6) {
         if (PrismFontFactory.debugFonts) {
            var6.printStackTrace();
         }

         return 0;
      }
   }

   public synchronized Buffer readBlock(int var1, int var2) {
      if (this.readBuffer == null) {
         this.readBuffer = new byte[1024];
         this.readBufferLen = 0;
      }

      if (var2 <= 1024) {
         if (this.readBufferStart <= var1 && this.readBufferStart + this.readBufferLen >= var1 + var2) {
            return new Buffer(this.readBuffer, var1 - this.readBufferStart);
         } else {
            this.readBufferStart = var1;
            this.readBufferLen = (long)(var1 + 1024) > this.filesize ? (int)this.filesize - var1 : 1024;
            this.readFromFile(this.readBuffer, (long)this.readBufferStart, this.readBufferLen);
            return new Buffer(this.readBuffer, 0);
         }
      } else {
         byte[] var3 = new byte[var2];
         this.readFromFile(var3, (long)var1, var2);
         return new Buffer(var3, 0);
      }
   }

   static class Buffer {
      byte[] data;
      int pos;
      int orig;

      Buffer(byte[] var1, int var2) {
         this.orig = this.pos = var2;
         this.data = var1;
      }

      int getInt(int var1) {
         var1 += this.orig;
         int var2 = this.data[var1++] & 255;
         var2 <<= 8;
         var2 |= this.data[var1++] & 255;
         var2 <<= 8;
         var2 |= this.data[var1++] & 255;
         var2 <<= 8;
         var2 |= this.data[var1++] & 255;
         return var2;
      }

      int getInt() {
         int var1 = this.data[this.pos++] & 255;
         var1 <<= 8;
         var1 |= this.data[this.pos++] & 255;
         var1 <<= 8;
         var1 |= this.data[this.pos++] & 255;
         var1 <<= 8;
         var1 |= this.data[this.pos++] & 255;
         return var1;
      }

      short getShort(int var1) {
         var1 += this.orig;
         int var2 = this.data[var1++] & 255;
         var2 <<= 8;
         var2 |= this.data[var1++] & 255;
         return (short)var2;
      }

      short getShort() {
         int var1 = this.data[this.pos++] & 255;
         var1 <<= 8;
         var1 |= this.data[this.pos++] & 255;
         return (short)var1;
      }

      char getChar(int var1) {
         var1 += this.orig;
         int var2 = this.data[var1++] & 255;
         var2 <<= 8;
         var2 |= this.data[var1++] & 255;
         return (char)var2;
      }

      char getChar() {
         int var1 = this.data[this.pos++] & 255;
         var1 <<= 8;
         var1 |= this.data[this.pos++] & 255;
         return (char)var1;
      }

      void position(int var1) {
         this.pos = this.orig + var1;
      }

      int capacity() {
         return this.data.length - this.orig;
      }

      byte get() {
         return this.data[this.pos++];
      }

      byte get(int var1) {
         var1 += this.orig;
         return this.data[var1];
      }

      void skip(int var1) {
         this.pos += var1;
      }

      void get(int var1, byte[] var2, int var3, int var4) {
         System.arraycopy(this.data, this.orig + var1, var2, var3, var4);
      }
   }
}
