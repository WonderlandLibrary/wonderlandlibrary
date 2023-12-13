package org.apache.commons.compress.compressors.z._internal_;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public abstract class InternalLZWInputStream extends CompressorInputStream {
   private final byte[] oneByte = new byte[1];
   protected final InputStream in;
   protected int clearCode = -1;
   protected int codeSize = 9;
   protected int bitsCached = 0;
   protected int bitsCachedSize = 0;
   protected int previousCode = -1;
   protected int tableSize = 0;
   protected int[] prefixes;
   protected byte[] characters;
   private byte[] outputStack;
   private int outputStackLocation;

   protected InternalLZWInputStream(InputStream var1) {
      this.in = var1;
   }

   public void close() throws IOException {
      this.in.close();
   }

   public int read() throws IOException {
      int var1 = this.read(this.oneByte);
      return var1 < 0 ? var1 : 255 & this.oneByte[0];
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4;
      for(var4 = this.readFromStack(var1, var2, var3); var3 - var4 > 0; var4 += this.readFromStack(var1, var2 + var4, var3 - var4)) {
         int var5 = this.decompressNextSymbol();
         if (var5 < 0) {
            if (var4 > 0) {
               this.count(var4);
               return var4;
            }

            return var5;
         }
      }

      this.count(var4);
      return var4;
   }

   protected abstract int decompressNextSymbol() throws IOException;

   protected abstract int addEntry(int var1, byte var2) throws IOException;

   protected void setClearCode(int var1) {
      this.clearCode = 1 << var1 - 1;
   }

   protected void initializeTables(int var1) {
      int var2 = 1 << var1;
      this.prefixes = new int[var2];
      this.characters = new byte[var2];
      this.outputStack = new byte[var2];
      this.outputStackLocation = var2;
      boolean var3 = true;

      for(int var4 = 0; var4 < 256; ++var4) {
         this.prefixes[var4] = -1;
         this.characters[var4] = (byte)var4;
      }

   }

   protected int readNextCode() throws IOException {
      int var1;
      while(this.bitsCachedSize < this.codeSize) {
         var1 = this.in.read();
         if (var1 < 0) {
            return var1;
         }

         this.bitsCached |= var1 << this.bitsCachedSize;
         this.bitsCachedSize += 8;
      }

      var1 = (1 << this.codeSize) - 1;
      int var2 = this.bitsCached & var1;
      this.bitsCached >>>= this.codeSize;
      this.bitsCachedSize -= this.codeSize;
      return var2;
   }

   protected int addEntry(int var1, byte var2, int var3) {
      if (this.tableSize < var3) {
         int var4 = this.tableSize;
         this.prefixes[this.tableSize] = var1;
         this.characters[this.tableSize] = var2;
         ++this.tableSize;
         return var4;
      } else {
         return -1;
      }
   }

   protected int addRepeatOfPreviousCode() throws IOException {
      if (this.previousCode == -1) {
         throw new IOException("The first code can't be a reference to its preceding code");
      } else {
         byte var1 = 0;

         for(int var2 = this.previousCode; var2 >= 0; var2 = this.prefixes[var2]) {
            var1 = this.characters[var2];
         }

         return this.addEntry(this.previousCode, var1);
      }
   }

   protected int expandCodeToOutputStack(int var1, boolean var2) throws IOException {
      for(int var3 = var1; var3 >= 0; var3 = this.prefixes[var3]) {
         this.outputStack[--this.outputStackLocation] = this.characters[var3];
      }

      if (this.previousCode != -1 && !var2) {
         this.addEntry(this.previousCode, this.outputStack[this.outputStackLocation]);
      }

      this.previousCode = var1;
      return this.outputStackLocation;
   }

   private int readFromStack(byte[] var1, int var2, int var3) {
      int var4 = this.outputStack.length - this.outputStackLocation;
      if (var4 > 0) {
         int var5 = Math.min(var4, var3);
         System.arraycopy(this.outputStack, this.outputStackLocation, var1, var2, var5);
         this.outputStackLocation += var5;
         return var5;
      } else {
         return 0;
      }
   }
}
