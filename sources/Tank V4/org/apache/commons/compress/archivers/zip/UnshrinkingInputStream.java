package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.z._internal_.InternalLZWInputStream;

class UnshrinkingInputStream extends InternalLZWInputStream {
   private static final int MAX_CODE_SIZE = 13;
   private static final int MAX_TABLE_SIZE = 8192;
   private final boolean[] isUsed;

   public UnshrinkingInputStream(InputStream var1) throws IOException {
      super(var1);
      this.setClearCode(this.codeSize);
      this.initializeTables(13);
      this.isUsed = new boolean[this.prefixes.length];

      for(int var2 = 0; var2 < 256; ++var2) {
         this.isUsed[var2] = true;
      }

      this.tableSize = this.clearCode + 1;
   }

   protected int addEntry(int var1, byte var2) throws IOException {
      while(this.tableSize < 8192 && this.isUsed[this.tableSize]) {
         ++this.tableSize;
      }

      int var3 = this.addEntry(var1, var2, 8192);
      if (var3 >= 0) {
         this.isUsed[var3] = true;
      }

      return var3;
   }

   private void partialClear() {
      boolean[] var1 = new boolean[8192];

      int var2;
      for(var2 = 0; var2 < this.isUsed.length; ++var2) {
         if (this.isUsed[var2] && this.prefixes[var2] != -1) {
            var1[this.prefixes[var2]] = true;
         }
      }

      for(var2 = this.clearCode + 1; var2 < var1.length; ++var2) {
         if (!var1[var2]) {
            this.isUsed[var2] = false;
            this.prefixes[var2] = -1;
         }
      }

   }

   protected int decompressNextSymbol() throws IOException {
      int var1 = this.readNextCode();
      if (var1 < 0) {
         return -1;
      } else if (var1 == this.clearCode) {
         int var4 = this.readNextCode();
         if (var4 < 0) {
            throw new IOException("Unexpected EOF;");
         } else {
            if (var4 == 1) {
               if (this.codeSize >= 13) {
                  throw new IOException("Attempt to increase code size beyond maximum");
               }

               ++this.codeSize;
            } else {
               if (var4 != 2) {
                  throw new IOException("Invalid clear code subcode " + var4);
               }

               this.partialClear();
               this.tableSize = this.clearCode + 1;
            }

            return 0;
         }
      } else {
         boolean var2 = false;
         int var3 = var1;
         if (!this.isUsed[var1]) {
            var3 = this.addRepeatOfPreviousCode();
            var2 = true;
         }

         return this.expandCodeToOutputStack(var3, var2);
      }
   }
}
