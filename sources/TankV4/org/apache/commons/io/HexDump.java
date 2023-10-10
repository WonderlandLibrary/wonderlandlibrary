package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;

public class HexDump {
   public static final String EOL = System.getProperty("line.separator");
   private static final char[] _hexcodes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static final int[] _shifts = new int[]{28, 24, 20, 16, 12, 8, 4, 0};

   public static void dump(byte[] var0, long var1, OutputStream var3, int var4) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
      if (var4 >= 0 && var4 < var0.length) {
         if (var3 == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
         } else {
            long var5 = var1 + (long)var4;
            StringBuilder var7 = new StringBuilder(74);

            for(int var8 = var4; var8 < var0.length; var8 += 16) {
               int var9 = var0.length - var8;
               if (var9 > 16) {
                  var9 = 16;
               }

               dump(var7, var5).append(' ');

               int var10;
               for(var10 = 0; var10 < 16; ++var10) {
                  if (var10 < var9) {
                     dump(var7, var0[var10 + var8]);
                  } else {
                     var7.append("  ");
                  }

                  var7.append(' ');
               }

               for(var10 = 0; var10 < var9; ++var10) {
                  if (var0[var10 + var8] >= 32 && var0[var10 + var8] < 127) {
                     var7.append((char)var0[var10 + var8]);
                  } else {
                     var7.append('.');
                  }
               }

               var7.append(EOL);
               var3.write(var7.toString().getBytes());
               var3.flush();
               var7.setLength(0);
               var5 += (long)var9;
            }

         }
      } else {
         throw new ArrayIndexOutOfBoundsException("illegal index: " + var4 + " into array of length " + var0.length);
      }
   }

   private static StringBuilder dump(StringBuilder var0, long var1) {
      for(int var3 = 0; var3 < 8; ++var3) {
         var0.append(_hexcodes[(int)(var1 >> _shifts[var3]) & 15]);
      }

      return var0;
   }

   private static StringBuilder dump(StringBuilder var0, byte var1) {
      for(int var2 = 0; var2 < 2; ++var2) {
         var0.append(_hexcodes[var1 >> _shifts[var2 + 6] & 15]);
      }

      return var0;
   }
}
