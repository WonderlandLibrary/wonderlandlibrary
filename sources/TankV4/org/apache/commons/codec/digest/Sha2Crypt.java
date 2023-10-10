package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.Charsets;

public class Sha2Crypt {
   private static final int ROUNDS_DEFAULT = 5000;
   private static final int ROUNDS_MAX = 999999999;
   private static final int ROUNDS_MIN = 1000;
   private static final String ROUNDS_PREFIX = "rounds=";
   private static final int SHA256_BLOCKSIZE = 32;
   static final String SHA256_PREFIX = "$5$";
   private static final int SHA512_BLOCKSIZE = 64;
   static final String SHA512_PREFIX = "$6$";
   private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");

   public static String sha256Crypt(byte[] var0) {
      return sha256Crypt(var0, (String)null);
   }

   public static String sha256Crypt(byte[] var0, String var1) {
      if (var1 == null) {
         var1 = "$5$" + B64.getRandomSalt(8);
      }

      return sha2Crypt(var0, var1, "$5$", 32, "SHA-256");
   }

   private static String sha2Crypt(byte[] var0, String var1, String var2, int var3, String var4) {
      int var5 = var0.length;
      int var6 = 5000;
      boolean var7 = false;
      if (var1 == null) {
         throw new IllegalArgumentException("Salt must not be null");
      } else {
         Matcher var8 = SALT_PATTERN.matcher(var1);
         if (var8 != null && var8.find()) {
            if (var8.group(3) != null) {
               var6 = Integer.parseInt(var8.group(3));
               var6 = Math.max(1000, Math.min(999999999, var6));
               var7 = true;
            }

            String var9 = var8.group(4);
            byte[] var10 = var9.getBytes(Charsets.UTF_8);
            int var11 = var10.length;
            MessageDigest var12 = DigestUtils.getDigest(var4);
            var12.update(var0);
            var12.update(var10);
            MessageDigest var13 = DigestUtils.getDigest(var4);
            var13.update(var0);
            var13.update(var10);
            var13.update(var0);
            byte[] var14 = var13.digest();

            int var15;
            for(var15 = var0.length; var15 > var3; var15 -= var3) {
               var12.update(var14, 0, var3);
            }

            var12.update(var14, 0, var15);

            for(var15 = var0.length; var15 > 0; var15 >>= 1) {
               if ((var15 & 1) != 0) {
                  var12.update(var14, 0, var3);
               } else {
                  var12.update(var0);
               }
            }

            var14 = var12.digest();
            var13 = DigestUtils.getDigest(var4);

            for(int var16 = 1; var16 <= var5; ++var16) {
               var13.update(var0);
            }

            byte[] var21 = var13.digest();
            byte[] var17 = new byte[var5];

            int var18;
            for(var18 = 0; var18 < var5 - var3; var18 += var3) {
               System.arraycopy(var21, 0, var17, var18, var3);
            }

            System.arraycopy(var21, 0, var17, var18, var5 - var18);
            var13 = DigestUtils.getDigest(var4);

            for(int var19 = 1; var19 <= 16 + (var14[0] & 255); ++var19) {
               var13.update(var10);
            }

            var21 = var13.digest();
            byte[] var22 = new byte[var11];

            for(var18 = 0; var18 < var11 - var3; var18 += var3) {
               System.arraycopy(var21, 0, var22, var18, var3);
            }

            System.arraycopy(var21, 0, var22, var18, var11 - var18);

            for(int var20 = 0; var20 <= var6 - 1; ++var20) {
               var12 = DigestUtils.getDigest(var4);
               if ((var20 & 1) != 0) {
                  var12.update(var17, 0, var5);
               } else {
                  var12.update(var14, 0, var3);
               }

               if (var20 % 3 != 0) {
                  var12.update(var22, 0, var11);
               }

               if (var20 % 7 != 0) {
                  var12.update(var17, 0, var5);
               }

               if ((var20 & 1) != 0) {
                  var12.update(var14, 0, var3);
               } else {
                  var12.update(var17, 0, var5);
               }

               var14 = var12.digest();
            }

            StringBuilder var23 = new StringBuilder(var2);
            if (var7) {
               var23.append("rounds=");
               var23.append(var6);
               var23.append("$");
            }

            var23.append(var9);
            var23.append("$");
            if (var3 == 32) {
               B64.b64from24bit(var14[0], var14[10], var14[20], 4, var23);
               B64.b64from24bit(var14[21], var14[1], var14[11], 4, var23);
               B64.b64from24bit(var14[12], var14[22], var14[2], 4, var23);
               B64.b64from24bit(var14[3], var14[13], var14[23], 4, var23);
               B64.b64from24bit(var14[24], var14[4], var14[14], 4, var23);
               B64.b64from24bit(var14[15], var14[25], var14[5], 4, var23);
               B64.b64from24bit(var14[6], var14[16], var14[26], 4, var23);
               B64.b64from24bit(var14[27], var14[7], var14[17], 4, var23);
               B64.b64from24bit(var14[18], var14[28], var14[8], 4, var23);
               B64.b64from24bit(var14[9], var14[19], var14[29], 4, var23);
               B64.b64from24bit((byte)0, var14[31], var14[30], 3, var23);
            } else {
               B64.b64from24bit(var14[0], var14[21], var14[42], 4, var23);
               B64.b64from24bit(var14[22], var14[43], var14[1], 4, var23);
               B64.b64from24bit(var14[44], var14[2], var14[23], 4, var23);
               B64.b64from24bit(var14[3], var14[24], var14[45], 4, var23);
               B64.b64from24bit(var14[25], var14[46], var14[4], 4, var23);
               B64.b64from24bit(var14[47], var14[5], var14[26], 4, var23);
               B64.b64from24bit(var14[6], var14[27], var14[48], 4, var23);
               B64.b64from24bit(var14[28], var14[49], var14[7], 4, var23);
               B64.b64from24bit(var14[50], var14[8], var14[29], 4, var23);
               B64.b64from24bit(var14[9], var14[30], var14[51], 4, var23);
               B64.b64from24bit(var14[31], var14[52], var14[10], 4, var23);
               B64.b64from24bit(var14[53], var14[11], var14[32], 4, var23);
               B64.b64from24bit(var14[12], var14[33], var14[54], 4, var23);
               B64.b64from24bit(var14[34], var14[55], var14[13], 4, var23);
               B64.b64from24bit(var14[56], var14[14], var14[35], 4, var23);
               B64.b64from24bit(var14[15], var14[36], var14[57], 4, var23);
               B64.b64from24bit(var14[37], var14[58], var14[16], 4, var23);
               B64.b64from24bit(var14[59], var14[17], var14[38], 4, var23);
               B64.b64from24bit(var14[18], var14[39], var14[60], 4, var23);
               B64.b64from24bit(var14[40], var14[61], var14[19], 4, var23);
               B64.b64from24bit(var14[62], var14[20], var14[41], 4, var23);
               B64.b64from24bit((byte)0, (byte)0, var14[63], 2, var23);
            }

            Arrays.fill(var21, (byte)0);
            Arrays.fill(var17, (byte)0);
            Arrays.fill(var22, (byte)0);
            var12.reset();
            var13.reset();
            Arrays.fill(var0, (byte)0);
            Arrays.fill(var10, (byte)0);
            return var23.toString();
         } else {
            throw new IllegalArgumentException("Invalid salt value: " + var1);
         }
      }
   }

   public static String sha512Crypt(byte[] var0) {
      return sha512Crypt(var0, (String)null);
   }

   public static String sha512Crypt(byte[] var0, String var1) {
      if (var1 == null) {
         var1 = "$6$" + B64.getRandomSalt(8);
      }

      return sha2Crypt(var0, var1, "$6$", 64, "SHA-512");
   }
}
