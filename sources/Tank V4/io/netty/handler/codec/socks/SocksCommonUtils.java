package io.netty.handler.codec.socks;

final class SocksCommonUtils {
   public static final SocksRequest UNKNOWN_SOCKS_REQUEST = new UnknownSocksRequest();
   public static final SocksResponse UNKNOWN_SOCKS_RESPONSE = new UnknownSocksResponse();
   private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
   private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
   private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
   private static final int XOR_DEFAULT_VALUE = 255;
   private static final char[] ipv6conseqZeroFiller = new char[]{':', ':'};
   private static final char ipv6hextetSeparator = ':';
   static final boolean $assertionsDisabled = !SocksCommonUtils.class.desiredAssertionStatus();

   private SocksCommonUtils() {
   }

   public static String intToIp(int var0) {
      return String.valueOf(var0 >> 24 & 255) + '.' + (var0 >> 16 & 255) + '.' + (var0 >> 8 & 255) + '.' + (var0 & 255);
   }

   public static String ipv6toCompressedForm(byte[] var0) {
      if (!$assertionsDisabled && var0.length != 16) {
         throw new AssertionError();
      } else {
         int var1 = -1;
         int var2 = 0;

         int var4;
         for(int var3 = 0; var3 < 8; var3 = var4 / 2 + 1) {
            var4 = var3 * 2;

            int var5;
            for(var5 = 0; var4 < var0.length && var0[var4] == 0 && var0[var4 + 1] == 0; ++var5) {
               var4 += 2;
            }

            if (var5 > var2) {
               var1 = var3;
               var2 = var5;
            }
         }

         if (var1 != -1 && var2 >= 2) {
            StringBuilder var6 = new StringBuilder(39);
            ipv6toStr(var6, var0, 0, var1);
            var6.append(ipv6conseqZeroFiller);
            ipv6toStr(var6, var0, var1 + var2, 8);
            return var6.toString();
         } else {
            return ipv6toStr(var0);
         }
      }
   }

   public static String ipv6toStr(byte[] var0) {
      if (!$assertionsDisabled && var0.length != 16) {
         throw new AssertionError();
      } else {
         StringBuilder var1 = new StringBuilder(39);
         ipv6toStr(var1, var0, 0, 8);
         return var1.toString();
      }
   }

   private static void ipv6toStr(StringBuilder var0, byte[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         var0.append(Integer.toHexString(var1[var4 << 1] << 8 & '\uff00' | var1[(var4 << 1) + 1] & 255));
         if (var4 < var3 - 1) {
            var0.append(':');
         }
      }

   }
}
