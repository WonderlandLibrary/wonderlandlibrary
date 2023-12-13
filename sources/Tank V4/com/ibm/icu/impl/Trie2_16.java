package com.ibm.icu.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Trie2_16 extends Trie2 {
   Trie2_16() {
   }

   public static Trie2_16 createFromSerialized(InputStream var0) throws IOException {
      return (Trie2_16)Trie2.createFromSerialized(var0);
   }

   public final int get(int var1) {
      if (var1 >= 0) {
         char var2;
         int var3;
         char var4;
         if (var1 < 55296 || var1 > 56319 && var1 <= 65535) {
            var4 = this.index[var1 >> 5];
            var3 = (var4 << 2) + (var1 & 31);
            var2 = this.index[var3];
            return var2;
         }

         if (var1 <= 65535) {
            var4 = this.index[2048 + (var1 - '\ud800' >> 5)];
            var3 = (var4 << 2) + (var1 & 31);
            var2 = this.index[var3];
            return var2;
         }

         if (var1 < this.highStart) {
            var3 = 2080 + (var1 >> 11);
            var4 = this.index[var3];
            var3 = var4 + (var1 >> 5 & 63);
            var4 = this.index[var3];
            var3 = (var4 << 2) + (var1 & 31);
            var2 = this.index[var3];
            return var2;
         }

         if (var1 <= 1114111) {
            var2 = this.index[this.highValueIndex];
            return var2;
         }
      }

      return this.errorValue;
   }

   public int getFromU16SingleLead(char var1) {
      char var3 = this.index[var1 >> 5];
      int var4 = (var3 << 2) + (var1 & 31);
      char var2 = this.index[var4];
      return var2;
   }

   public int serialize(OutputStream var1) throws IOException {
      DataOutputStream var2 = new DataOutputStream(var1);
      byte var3 = 0;
      int var5 = var3 + this.serializeHeader(var2);

      for(int var4 = 0; var4 < this.dataLength; ++var4) {
         var2.writeChar(this.index[this.data16 + var4]);
      }

      var5 += this.dataLength * 2;
      return var5;
   }

   public int getSerializedLength() {
      return 16 + (this.header.indexLength + this.dataLength) * 2;
   }

   int rangeEnd(int var1, int var2, int var3) {
      int var4 = var1;
      boolean var5 = false;
      boolean var6 = false;

      label66:
      while(var4 < var2) {
         int var7;
         int var10;
         char var11;
         if (var4 < 55296 || var4 > 56319 && var4 <= 65535) {
            var11 = 0;
            var10 = this.index[var4 >> 5] << 2;
         } else if (var4 < 65535) {
            var11 = 2048;
            var10 = this.index[var11 + (var4 - '\ud800' >> 5)] << 2;
         } else {
            if (var4 >= this.highStart) {
               if (var3 == this.index[this.highValueIndex]) {
                  var4 = var2;
               }
               break;
            }

            var7 = 2080 + (var4 >> 11);
            var11 = this.index[var7];
            var10 = this.index[var11 + (var4 >> 5 & 63)] << 2;
         }

         if (var11 == this.index2NullOffset) {
            if (var3 != this.initialValue) {
               break;
            }

            var4 += 2048;
         } else if (var10 == this.dataNullOffset) {
            if (var3 != this.initialValue) {
               break;
            }

            var4 += 32;
         } else {
            var7 = var10 + (var4 & 31);
            int var8 = var10 + 32;

            for(int var9 = var7; var9 < var8; ++var9) {
               if (this.index[var9] != var3) {
                  var4 += var9 - var7;
                  break label66;
               }
            }

            var4 += var8 - var7;
         }
      }

      if (var4 > var2) {
         var4 = var2;
      }

      return var4 - 1;
   }
}
