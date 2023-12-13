package com.ibm.icu.impl;

import com.ibm.icu.util.VersionInfo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class ICUBinary {
   private static final byte MAGIC1 = -38;
   private static final byte MAGIC2 = 39;
   private static final byte BIG_ENDIAN_ = 1;
   private static final byte CHAR_SET_ = 0;
   private static final byte CHAR_SIZE_ = 2;
   private static final String MAGIC_NUMBER_AUTHENTICATION_FAILED_ = "ICU data file error: Not an ICU data file";
   private static final String HEADER_AUTHENTICATION_FAILED_ = "ICU data file error: Header authentication failed, please check if you have a valid ICU data file";

   public static final byte[] readHeader(InputStream var0, byte[] var1, ICUBinary.Authenticate var2) throws IOException {
      DataInputStream var3 = new DataInputStream(var0);
      char var4 = var3.readChar();
      byte var5 = 2;
      byte var6 = var3.readByte();
      int var14 = var5 + 1;
      byte var7 = var3.readByte();
      ++var14;
      if (var6 == -38 && var7 == 39) {
         var3.readChar();
         var14 += 2;
         var3.readChar();
         var14 += 2;
         byte var8 = var3.readByte();
         ++var14;
         byte var9 = var3.readByte();
         ++var14;
         byte var10 = var3.readByte();
         ++var14;
         var3.readByte();
         ++var14;
         byte[] var11 = new byte[4];
         var3.readFully(var11);
         var14 += 4;
         byte[] var12 = new byte[4];
         var3.readFully(var12);
         var14 += 4;
         byte[] var13 = new byte[4];
         var3.readFully(var13);
         var14 += 4;
         if (var4 < var14) {
            throw new IOException("Internal Error: Header size error");
         } else {
            var3.skipBytes(var4 - var14);
            if (var8 == 1 && var9 == 0 && var10 == 2 && Arrays.equals(var1, var11) && (var2 == null || var2.isDataVersionAcceptable(var12))) {
               return var13;
            } else {
               throw new IOException("ICU data file error: Header authentication failed, please check if you have a valid ICU data file");
            }
         }
      } else {
         throw new IOException("ICU data file error: Not an ICU data file");
      }
   }

   public static final VersionInfo readHeaderAndDataVersion(InputStream var0, byte[] var1, ICUBinary.Authenticate var2) throws IOException {
      byte[] var3 = readHeader(var0, var1, var2);
      return VersionInfo.getInstance(var3[0], var3[1], var3[2], var3[3]);
   }

   public interface Authenticate {
      boolean isDataVersionAcceptable(byte[] var1);
   }
}
