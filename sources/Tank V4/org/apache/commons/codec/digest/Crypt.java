package org.apache.commons.codec.digest;

import org.apache.commons.codec.Charsets;

public class Crypt {
   public static String crypt(byte[] var0) {
      return crypt((byte[])var0, (String)null);
   }

   public static String crypt(byte[] var0, String var1) {
      if (var1 == null) {
         return Sha2Crypt.sha512Crypt(var0);
      } else if (var1.startsWith("$6$")) {
         return Sha2Crypt.sha512Crypt(var0, var1);
      } else if (var1.startsWith("$5$")) {
         return Sha2Crypt.sha256Crypt(var0, var1);
      } else {
         return var1.startsWith("$1$") ? Md5Crypt.md5Crypt(var0, var1) : UnixCrypt.crypt(var0, var1);
      }
   }

   public static String crypt(String var0) {
      return crypt((String)var0, (String)null);
   }

   public static String crypt(String var0, String var1) {
      return crypt(var0.getBytes(Charsets.UTF_8), var1);
   }
}
