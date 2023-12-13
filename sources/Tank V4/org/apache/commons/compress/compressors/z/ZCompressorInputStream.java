package org.apache.commons.compress.compressors.z;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.z._internal_.InternalLZWInputStream;

public class ZCompressorInputStream extends InternalLZWInputStream {
   private static final int MAGIC_1 = 31;
   private static final int MAGIC_2 = 157;
   private static final int BLOCK_MODE_MASK = 128;
   private static final int MAX_CODE_SIZE_MASK = 31;
   private final boolean blockMode;
   private final int maxCodeSize;
   private long totalCodesRead = 0L;

   public ZCompressorInputStream(InputStream var1) throws IOException {
      super(var1);
      int var2 = this.in.read();
      int var3 = this.in.read();
      int var4 = this.in.read();
      if (var2 == 31 && var3 == 157 && var4 >= 0) {
         this.blockMode = (var4 & 128) != 0;
         this.maxCodeSize = var4 & 31;
         if (this.blockMode) {
            this.setClearCode(this.codeSize);
         }

         this.initializeTables(this.maxCodeSize);
         this.clearEntries();
      } else {
         throw new IOException("Input is not in .Z format");
      }
   }

   private void clearEntries() {
      this.tableSize = 256;
      if (this.blockMode) {
         ++this.tableSize;
      }

   }

   protected int readNextCode() throws IOException {
      int var1 = super.readNextCode();
      if (var1 >= 0) {
         ++this.totalCodesRead;
      }

      return var1;
   }

   private void reAlignReading() throws IOException {
      long var1 = 8L - this.totalCodesRead % 8L;
      if (var1 == 8L) {
         var1 = 0L;
      }

      for(long var3 = 0L; var3 < var1; ++var3) {
         this.readNextCode();
      }

      this.bitsCached = 0;
      this.bitsCachedSize = 0;
   }

   protected int addEntry(int var1, byte var2) throws IOException {
      int var3 = 1 << this.codeSize;
      int var4 = this.addEntry(var1, var2, var3);
      if (this.tableSize == var3 && this.codeSize < this.maxCodeSize) {
         this.reAlignReading();
         ++this.codeSize;
      }

      return var4;
   }

   protected int decompressNextSymbol() throws IOException {
      int var1 = this.readNextCode();
      if (var1 < 0) {
         return -1;
      } else if (this.blockMode && var1 == this.clearCode) {
         this.clearEntries();
         this.reAlignReading();
         this.codeSize = 9;
         this.previousCode = -1;
         return 0;
      } else {
         boolean var2 = false;
         if (var1 == this.tableSize) {
            this.addRepeatOfPreviousCode();
            var2 = true;
         } else if (var1 > this.tableSize) {
            throw new IOException(String.format("Invalid %d bit code 0x%x", this.codeSize, var1));
         }

         return this.expandCodeToOutputStack(var1, var2);
      }
   }

   public static boolean matches(byte[] var0, int var1) {
      return var1 > 3 && var0[0] == 31 && var0[1] == -99;
   }
}
