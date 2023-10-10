package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Simple8BitZipEncoding implements ZipEncoding {
   private final char[] highChars;
   private final List reverseMapping;

   public Simple8BitZipEncoding(char[] var1) {
      this.highChars = (char[])var1.clone();
      ArrayList var2 = new ArrayList(this.highChars.length);
      byte var3 = 127;
      char[] var4 = this.highChars;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         char var7 = var4[var6];
         ++var3;
         var2.add(new Simple8BitZipEncoding.Simple8BitChar(var3, var7));
      }

      Collections.sort(var2);
      this.reverseMapping = Collections.unmodifiableList(var2);
   }

   public char decodeByte(byte var1) {
      return var1 >= 0 ? (char)var1 : this.highChars[128 + var1];
   }

   private Simple8BitZipEncoding.Simple8BitChar encodeHighChar(char var1) {
      int var2 = 0;
      int var3 = this.reverseMapping.size();

      while(var3 > var2) {
         int var4 = var2 + (var3 - var2) / 2;
         Simple8BitZipEncoding.Simple8BitChar var5 = (Simple8BitZipEncoding.Simple8BitChar)this.reverseMapping.get(var4);
         if (var5.unicode == var1) {
            return var5;
         }

         if (var5.unicode < var1) {
            var2 = var4 + 1;
         } else {
            var3 = var4;
         }
      }

      if (var2 >= this.reverseMapping.size()) {
         return null;
      } else {
         Simple8BitZipEncoding.Simple8BitChar var6 = (Simple8BitZipEncoding.Simple8BitChar)this.reverseMapping.get(var2);
         if (var6.unicode != var1) {
            return null;
         } else {
            return var6;
         }
      }
   }

   public boolean canEncode(String var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         char var3 = var1.charAt(var2);
         if (var3 >= 0) {
            return false;
         }
      }

      return true;
   }

   public ByteBuffer encode(String var1) {
      ByteBuffer var2 = ByteBuffer.allocate(var1.length() + 6 + (var1.length() + 1) / 2);

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var2.remaining() < 6) {
            var2 = ZipEncodingHelper.growBuffer(var2, var2.position() + 6);
         }

         if (var4 >= 0) {
            ZipEncodingHelper.appendSurrogate(var2, var4);
         }
      }

      var2.limit(var2.position());
      var2.rewind();
      return var2;
   }

   public String decode(byte[] var1) throws IOException {
      char[] var2 = new char[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.decodeByte(var1[var3]);
      }

      return new String(var2);
   }

   private static final class Simple8BitChar implements Comparable {
      public final char unicode;
      public final byte code;

      Simple8BitChar(byte var1, char var2) {
         this.code = var1;
         this.unicode = var2;
      }

      public int compareTo(Simple8BitZipEncoding.Simple8BitChar var1) {
         return this.unicode - var1.unicode;
      }

      public String toString() {
         return "0x" + Integer.toHexString('\uffff' & this.unicode) + "->0x" + Integer.toHexString(255 & this.code);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Simple8BitZipEncoding.Simple8BitChar)) {
            return false;
         } else {
            Simple8BitZipEncoding.Simple8BitChar var2 = (Simple8BitZipEncoding.Simple8BitChar)var1;
            return this.unicode == var2.unicode && this.code == var2.code;
         }
      }

      public int hashCode() {
         return this.unicode;
      }

      public int compareTo(Object var1) {
         return this.compareTo((Simple8BitZipEncoding.Simple8BitChar)var1);
      }
   }
}
