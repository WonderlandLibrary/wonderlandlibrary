package org.apache.http.impl.auth;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
final class NTLMEngineImpl implements NTLMEngine {
   protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
   protected static final int FLAG_REQUEST_TARGET = 4;
   protected static final int FLAG_REQUEST_SIGN = 16;
   protected static final int FLAG_REQUEST_SEAL = 32;
   protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
   protected static final int FLAG_REQUEST_NTLMv1 = 512;
   protected static final int FLAG_DOMAIN_PRESENT = 4096;
   protected static final int FLAG_WORKSTATION_PRESENT = 8192;
   protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
   protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
   protected static final int FLAG_REQUEST_VERSION = 33554432;
   protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
   protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
   protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
   protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = Integer.MIN_VALUE;
   private static final SecureRandom RND_GEN;
   static final String DEFAULT_CHARSET = "ASCII";
   private String credentialCharset = "ASCII";
   private static final byte[] SIGNATURE;

   final String getResponseFor(String var1, String var2, String var3, String var4, String var5) throws NTLMEngineException {
      String var6;
      if (var1 != null && !var1.trim().equals("")) {
         NTLMEngineImpl.Type2Message var7 = new NTLMEngineImpl.Type2Message(var1);
         var6 = this.getType3Message(var2, var3, var4, var5, var7.getChallenge(), var7.getFlags(), var7.getTarget(), var7.getTargetInfo());
      } else {
         var6 = this.getType1Message(var4, var5);
      }

      return var6;
   }

   String getType1Message(String var1, String var2) throws NTLMEngineException {
      return (new NTLMEngineImpl.Type1Message(var2, var1)).getResponse();
   }

   String getType3Message(String var1, String var2, String var3, String var4, byte[] var5, int var6, String var7, byte[] var8) throws NTLMEngineException {
      return (new NTLMEngineImpl.Type3Message(var4, var3, var1, var2, var5, var6, var7, var8)).getResponse();
   }

   String getCredentialCharset() {
      return this.credentialCharset;
   }

   void setCredentialCharset(String var1) {
      this.credentialCharset = var1;
   }

   private static String stripDotSuffix(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(".");
         return var1 != -1 ? var0.substring(0, var1) : var0;
      }
   }

   private static String convertHost(String var0) {
      return stripDotSuffix(var0);
   }

   private static String convertDomain(String var0) {
      return stripDotSuffix(var0);
   }

   private static int readULong(byte[] var0, int var1) throws NTLMEngineException {
      if (var0.length < var1 + 4) {
         throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
      } else {
         return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
      }
   }

   private static int readUShort(byte[] var0, int var1) throws NTLMEngineException {
      if (var0.length < var1 + 2) {
         throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
      } else {
         return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8;
      }
   }

   private static byte[] readSecurityBuffer(byte[] var0, int var1) throws NTLMEngineException {
      int var2 = readUShort(var0, var1);
      int var3 = readULong(var0, var1 + 4);
      if (var0.length < var3 + var2) {
         throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
      } else {
         byte[] var4 = new byte[var2];
         System.arraycopy(var0, var3, var4, 0, var2);
         return var4;
      }
   }

   private static byte[] makeRandomChallenge() throws NTLMEngineException {
      if (RND_GEN == null) {
         throw new NTLMEngineException("Random generator not available");
      } else {
         byte[] var0 = new byte[8];
         SecureRandom var1;
         synchronized(var1 = RND_GEN){}
         RND_GEN.nextBytes(var0);
         return var0;
      }
   }

   private static byte[] makeSecondaryKey() throws NTLMEngineException {
      if (RND_GEN == null) {
         throw new NTLMEngineException("Random generator not available");
      } else {
         byte[] var0 = new byte[16];
         SecureRandom var1;
         synchronized(var1 = RND_GEN){}
         RND_GEN.nextBytes(var0);
         return var0;
      }
   }

   static byte[] hmacMD5(byte[] var0, byte[] var1) throws NTLMEngineException {
      NTLMEngineImpl.HMACMD5 var2 = new NTLMEngineImpl.HMACMD5(var1);
      var2.update(var0);
      return var2.getOutput();
   }

   static byte[] RC4(byte[] var0, byte[] var1) throws NTLMEngineException {
      try {
         Cipher var2 = Cipher.getInstance("RC4");
         var2.init(1, new SecretKeySpec(var1, "RC4"));
         return var2.doFinal(var0);
      } catch (Exception var3) {
         throw new NTLMEngineException(var3.getMessage(), var3);
      }
   }

   static byte[] ntlm2SessionResponse(byte[] var0, byte[] var1, byte[] var2) throws NTLMEngineException {
      try {
         MessageDigest var3 = MessageDigest.getInstance("MD5");
         var3.update(var1);
         var3.update(var2);
         byte[] var4 = var3.digest();
         byte[] var5 = new byte[8];
         System.arraycopy(var4, 0, var5, 0, 8);
         return lmResponse(var0, var5);
      } catch (Exception var6) {
         if (var6 instanceof NTLMEngineException) {
            throw (NTLMEngineException)var6;
         } else {
            throw new NTLMEngineException(var6.getMessage(), var6);
         }
      }
   }

   private static byte[] lmHash(String var0) throws NTLMEngineException {
      try {
         byte[] var1 = var0.toUpperCase(Locale.US).getBytes("US-ASCII");
         int var2 = Math.min(var1.length, 14);
         byte[] var3 = new byte[14];
         System.arraycopy(var1, 0, var3, 0, var2);
         Key var4 = createDESKey(var3, 0);
         Key var5 = createDESKey(var3, 7);
         byte[] var6 = "KGS!@#$%".getBytes("US-ASCII");
         Cipher var7 = Cipher.getInstance("DES/ECB/NoPadding");
         var7.init(1, var4);
         byte[] var8 = var7.doFinal(var6);
         var7.init(1, var5);
         byte[] var9 = var7.doFinal(var6);
         byte[] var10 = new byte[16];
         System.arraycopy(var8, 0, var10, 0, 8);
         System.arraycopy(var9, 0, var10, 8, 8);
         return var10;
      } catch (Exception var11) {
         throw new NTLMEngineException(var11.getMessage(), var11);
      }
   }

   private static byte[] ntlmHash(String var0) throws NTLMEngineException {
      try {
         byte[] var1 = var0.getBytes("UnicodeLittleUnmarked");
         NTLMEngineImpl.MD4 var2 = new NTLMEngineImpl.MD4();
         var2.update(var1);
         return var2.getOutput();
      } catch (UnsupportedEncodingException var3) {
         throw new NTLMEngineException("Unicode not supported: " + var3.getMessage(), var3);
      }
   }

   private static byte[] lmv2Hash(String var0, String var1, byte[] var2) throws NTLMEngineException {
      try {
         NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var2);
         var3.update(var1.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
         if (var0 != null) {
            var3.update(var0.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
         }

         return var3.getOutput();
      } catch (UnsupportedEncodingException var4) {
         throw new NTLMEngineException("Unicode not supported! " + var4.getMessage(), var4);
      }
   }

   private static byte[] ntlmv2Hash(String var0, String var1, byte[] var2) throws NTLMEngineException {
      try {
         NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var2);
         var3.update(var1.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
         if (var0 != null) {
            var3.update(var0.getBytes("UnicodeLittleUnmarked"));
         }

         return var3.getOutput();
      } catch (UnsupportedEncodingException var4) {
         throw new NTLMEngineException("Unicode not supported! " + var4.getMessage(), var4);
      }
   }

   private static byte[] lmResponse(byte[] var0, byte[] var1) throws NTLMEngineException {
      try {
         byte[] var2 = new byte[21];
         System.arraycopy(var0, 0, var2, 0, 16);
         Key var3 = createDESKey(var2, 0);
         Key var4 = createDESKey(var2, 7);
         Key var5 = createDESKey(var2, 14);
         Cipher var6 = Cipher.getInstance("DES/ECB/NoPadding");
         var6.init(1, var3);
         byte[] var7 = var6.doFinal(var1);
         var6.init(1, var4);
         byte[] var8 = var6.doFinal(var1);
         var6.init(1, var5);
         byte[] var9 = var6.doFinal(var1);
         byte[] var10 = new byte[24];
         System.arraycopy(var7, 0, var10, 0, 8);
         System.arraycopy(var8, 0, var10, 8, 8);
         System.arraycopy(var9, 0, var10, 16, 8);
         return var10;
      } catch (Exception var11) {
         throw new NTLMEngineException(var11.getMessage(), var11);
      }
   }

   private static byte[] lmv2Response(byte[] var0, byte[] var1, byte[] var2) throws NTLMEngineException {
      NTLMEngineImpl.HMACMD5 var3 = new NTLMEngineImpl.HMACMD5(var0);
      var3.update(var1);
      var3.update(var2);
      byte[] var4 = var3.getOutput();
      byte[] var5 = new byte[var4.length + var2.length];
      System.arraycopy(var4, 0, var5, 0, var4.length);
      System.arraycopy(var2, 0, var5, var4.length, var2.length);
      return var5;
   }

   private static byte[] createBlob(byte[] var0, byte[] var1, byte[] var2) {
      byte[] var3 = new byte[]{1, 1, 0, 0};
      byte[] var4 = new byte[]{0, 0, 0, 0};
      byte[] var5 = new byte[]{0, 0, 0, 0};
      byte[] var6 = new byte[]{0, 0, 0, 0};
      byte[] var7 = new byte[var3.length + var4.length + var2.length + 8 + var5.length + var1.length + var6.length];
      byte var8 = 0;
      System.arraycopy(var3, 0, var7, var8, var3.length);
      int var9 = var8 + var3.length;
      System.arraycopy(var4, 0, var7, var9, var4.length);
      var9 += var4.length;
      System.arraycopy(var2, 0, var7, var9, var2.length);
      var9 += var2.length;
      System.arraycopy(var0, 0, var7, var9, 8);
      var9 += 8;
      System.arraycopy(var5, 0, var7, var9, var5.length);
      var9 += var5.length;
      System.arraycopy(var1, 0, var7, var9, var1.length);
      var9 += var1.length;
      System.arraycopy(var6, 0, var7, var9, var6.length);
      int var10000 = var9 + var6.length;
      return var7;
   }

   private static Key createDESKey(byte[] var0, int var1) {
      byte[] var2 = new byte[7];
      System.arraycopy(var0, var1, var2, 0, 7);
      byte[] var3 = new byte[]{var2[0], (byte)(var2[0] << 7 | (var2[1] & 255) >>> 1), (byte)(var2[1] << 6 | (var2[2] & 255) >>> 2), (byte)(var2[2] << 5 | (var2[3] & 255) >>> 3), (byte)(var2[3] << 4 | (var2[4] & 255) >>> 4), (byte)(var2[4] << 3 | (var2[5] & 255) >>> 5), (byte)(var2[5] << 2 | (var2[6] & 255) >>> 6), (byte)(var2[6] << 1)};
      oddParity(var3);
      return new SecretKeySpec(var3, "DES");
   }

   private static void oddParity(byte[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         byte var2 = var0[var1];
         boolean var3 = ((var2 >>> 7 ^ var2 >>> 6 ^ var2 >>> 5 ^ var2 >>> 4 ^ var2 >>> 3 ^ var2 >>> 2 ^ var2 >>> 1) & 1) == 0;
         if (var3) {
            var0[var1] = (byte)(var0[var1] | 1);
         } else {
            var0[var1] &= -2;
         }
      }

   }

   static void writeULong(byte[] var0, int var1, int var2) {
      var0[var2] = (byte)(var1 & 255);
      var0[var2 + 1] = (byte)(var1 >> 8 & 255);
      var0[var2 + 2] = (byte)(var1 >> 16 & 255);
      var0[var2 + 3] = (byte)(var1 >> 24 & 255);
   }

   static int F(int var0, int var1, int var2) {
      return var0 & var1 | ~var0 & var2;
   }

   static int G(int var0, int var1, int var2) {
      return var0 & var1 | var0 & var2 | var1 & var2;
   }

   static int H(int var0, int var1, int var2) {
      return var0 ^ var1 ^ var2;
   }

   static int rotintlft(int var0, int var1) {
      return var0 << var1 | var0 >>> 32 - var1;
   }

   public String generateType1Msg(String var1, String var2) throws NTLMEngineException {
      return this.getType1Message(var2, var1);
   }

   public String generateType3Msg(String var1, String var2, String var3, String var4, String var5) throws NTLMEngineException {
      NTLMEngineImpl.Type2Message var6 = new NTLMEngineImpl.Type2Message(var5);
      return this.getType3Message(var1, var2, var4, var3, var6.getChallenge(), var6.getFlags(), var6.getTarget(), var6.getTargetInfo());
   }

   static byte[] access$000() throws NTLMEngineException {
      return makeRandomChallenge();
   }

   static byte[] access$100() throws NTLMEngineException {
      return makeSecondaryKey();
   }

   static byte[] access$200(String var0) throws NTLMEngineException {
      return lmHash(var0);
   }

   static byte[] access$300(byte[] var0, byte[] var1) throws NTLMEngineException {
      return lmResponse(var0, var1);
   }

   static byte[] access$400(String var0) throws NTLMEngineException {
      return ntlmHash(var0);
   }

   static byte[] access$500(String var0, String var1, byte[] var2) throws NTLMEngineException {
      return lmv2Hash(var0, var1, var2);
   }

   static byte[] access$600(String var0, String var1, byte[] var2) throws NTLMEngineException {
      return ntlmv2Hash(var0, var1, var2);
   }

   static byte[] access$700(byte[] var0, byte[] var1, byte[] var2) {
      return createBlob(var0, var1, var2);
   }

   static byte[] access$800(byte[] var0, byte[] var1, byte[] var2) throws NTLMEngineException {
      return lmv2Response(var0, var1, var2);
   }

   static Key access$900(byte[] var0, int var1) {
      return createDESKey(var0, var1);
   }

   static byte[] access$1000() {
      return SIGNATURE;
   }

   static int access$1100(byte[] var0, int var1) throws NTLMEngineException {
      return readUShort(var0, var1);
   }

   static int access$1200(byte[] var0, int var1) throws NTLMEngineException {
      return readULong(var0, var1);
   }

   static byte[] access$1300(byte[] var0, int var1) throws NTLMEngineException {
      return readSecurityBuffer(var0, var1);
   }

   static String access$1400(String var0) {
      return convertHost(var0);
   }

   static String access$1500(String var0) {
      return convertDomain(var0);
   }

   static {
      SecureRandom var0 = null;

      try {
         var0 = SecureRandom.getInstance("SHA1PRNG");
      } catch (Exception var2) {
      }

      RND_GEN = var0;
      byte[] var3 = EncodingUtils.getBytes("NTLMSSP", "ASCII");
      SIGNATURE = new byte[var3.length + 1];
      System.arraycopy(var3, 0, SIGNATURE, 0, var3.length);
      SIGNATURE[var3.length] = 0;
   }

   static class HMACMD5 {
      protected byte[] ipad;
      protected byte[] opad;
      protected MessageDigest md5;

      HMACMD5(byte[] var1) throws NTLMEngineException {
         byte[] var2 = var1;

         try {
            this.md5 = MessageDigest.getInstance("MD5");
         } catch (Exception var5) {
            throw new NTLMEngineException("Error getting md5 message digest implementation: " + var5.getMessage(), var5);
         }

         this.ipad = new byte[64];
         this.opad = new byte[64];
         int var3 = var1.length;
         if (var3 > 64) {
            this.md5.update(var1);
            var2 = this.md5.digest();
            var3 = var2.length;
         }

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            this.ipad[var4] = (byte)(var2[var4] ^ 54);
            this.opad[var4] = (byte)(var2[var4] ^ 92);
         }

         while(var4 < 64) {
            this.ipad[var4] = 54;
            this.opad[var4] = 92;
            ++var4;
         }

         this.md5.reset();
         this.md5.update(this.ipad);
      }

      byte[] getOutput() {
         byte[] var1 = this.md5.digest();
         this.md5.update(this.opad);
         return this.md5.digest(var1);
      }

      void update(byte[] var1) {
         this.md5.update(var1);
      }

      void update(byte[] var1, int var2, int var3) {
         this.md5.update(var1, var2, var3);
      }
   }

   static class MD4 {
      protected int A = 1732584193;
      protected int B = -271733879;
      protected int C = -1732584194;
      protected int D = 271733878;
      protected long count = 0L;
      protected byte[] dataBuffer = new byte[64];

      void update(byte[] var1) {
         int var2 = (int)(this.count & 63L);
         int var3 = 0;

         int var4;
         while(var1.length - var3 + var2 >= this.dataBuffer.length) {
            var4 = this.dataBuffer.length - var2;
            System.arraycopy(var1, var3, this.dataBuffer, var2, var4);
            this.count += (long)var4;
            var2 = 0;
            var3 += var4;
            this.processBuffer();
         }

         if (var3 < var1.length) {
            var4 = var1.length - var3;
            System.arraycopy(var1, var3, this.dataBuffer, var2, var4);
            this.count += (long)var4;
            int var10000 = var2 + var4;
         }

      }

      byte[] getOutput() {
         int var1 = (int)(this.count & 63L);
         int var2 = var1 < 56 ? 56 - var1 : 120 - var1;
         byte[] var3 = new byte[var2 + 8];
         var3[0] = -128;

         for(int var4 = 0; var4 < 8; ++var4) {
            var3[var2 + var4] = (byte)((int)(this.count * 8L >>> 8 * var4));
         }

         this.update(var3);
         byte[] var5 = new byte[16];
         NTLMEngineImpl.writeULong(var5, this.A, 0);
         NTLMEngineImpl.writeULong(var5, this.B, 4);
         NTLMEngineImpl.writeULong(var5, this.C, 8);
         NTLMEngineImpl.writeULong(var5, this.D, 12);
         return var5;
      }

      protected void processBuffer() {
         int[] var1 = new int[16];

         int var2;
         for(var2 = 0; var2 < 16; ++var2) {
            var1[var2] = (this.dataBuffer[var2 * 4] & 255) + ((this.dataBuffer[var2 * 4 + 1] & 255) << 8) + ((this.dataBuffer[var2 * 4 + 2] & 255) << 16) + ((this.dataBuffer[var2 * 4 + 3] & 255) << 24);
         }

         var2 = this.A;
         int var3 = this.B;
         int var4 = this.C;
         int var5 = this.D;
         this.round1(var1);
         this.round2(var1);
         this.round3(var1);
         this.A += var2;
         this.B += var3;
         this.C += var4;
         this.D += var5;
      }

      protected void round1(int[] var1) {
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + var1[0], 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + var1[1], 7);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + var1[2], 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + var1[3], 19);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + var1[4], 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + var1[5], 7);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + var1[6], 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + var1[7], 19);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + var1[8], 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + var1[9], 7);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + var1[10], 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + var1[11], 19);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + var1[12], 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + var1[13], 7);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + var1[14], 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + var1[15], 19);
      }

      protected void round2(int[] var1) {
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + var1[0] + 1518500249, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + var1[4] + 1518500249, 5);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + var1[8] + 1518500249, 9);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + var1[12] + 1518500249, 13);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + var1[1] + 1518500249, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + var1[5] + 1518500249, 5);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + var1[9] + 1518500249, 9);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + var1[13] + 1518500249, 13);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + var1[2] + 1518500249, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + var1[6] + 1518500249, 5);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + var1[10] + 1518500249, 9);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + var1[14] + 1518500249, 13);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + var1[3] + 1518500249, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + var1[7] + 1518500249, 5);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + var1[11] + 1518500249, 9);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + var1[15] + 1518500249, 13);
      }

      protected void round3(int[] var1) {
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + var1[0] + 1859775393, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + var1[8] + 1859775393, 9);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + var1[4] + 1859775393, 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + var1[12] + 1859775393, 15);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + var1[2] + 1859775393, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + var1[10] + 1859775393, 9);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + var1[6] + 1859775393, 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + var1[14] + 1859775393, 15);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + var1[1] + 1859775393, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + var1[9] + 1859775393, 9);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + var1[5] + 1859775393, 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + var1[13] + 1859775393, 15);
         this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + var1[3] + 1859775393, 3);
         this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + var1[11] + 1859775393, 9);
         this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + var1[7] + 1859775393, 11);
         this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + var1[15] + 1859775393, 15);
      }
   }

   static class Type3Message extends NTLMEngineImpl.NTLMMessage {
      protected int type2Flags;
      protected byte[] domainBytes;
      protected byte[] hostBytes;
      protected byte[] userBytes;
      protected byte[] lmResp;
      protected byte[] ntResp;
      protected byte[] sessionKey;

      Type3Message(String var1, String var2, String var3, String var4, byte[] var5, int var6, String var7, byte[] var8) throws NTLMEngineException {
         this.type2Flags = var6;
         String var9 = NTLMEngineImpl.access$1400(var2);
         String var10 = NTLMEngineImpl.access$1500(var1);
         NTLMEngineImpl.CipherGen var11 = new NTLMEngineImpl.CipherGen(var10, var3, var4, var5, var7, var8);

         byte[] var12;
         try {
            if ((var6 & 8388608) != 0 && var8 != null && var7 != null) {
               this.ntResp = var11.getNTLMv2Response();
               this.lmResp = var11.getLMv2Response();
               if ((var6 & 128) != 0) {
                  var12 = var11.getLanManagerSessionKey();
               } else {
                  var12 = var11.getNTLMv2UserSessionKey();
               }
            } else if ((var6 & 524288) != 0) {
               this.ntResp = var11.getNTLM2SessionResponse();
               this.lmResp = var11.getLM2SessionResponse();
               if ((var6 & 128) != 0) {
                  var12 = var11.getLanManagerSessionKey();
               } else {
                  var12 = var11.getNTLM2SessionResponseUserSessionKey();
               }
            } else {
               this.ntResp = var11.getNTLMResponse();
               this.lmResp = var11.getLMResponse();
               if ((var6 & 128) != 0) {
                  var12 = var11.getLanManagerSessionKey();
               } else {
                  var12 = var11.getNTLMUserSessionKey();
               }
            }
         } catch (NTLMEngineException var15) {
            this.ntResp = new byte[0];
            this.lmResp = var11.getLMResponse();
            if ((var6 & 128) != 0) {
               var12 = var11.getLanManagerSessionKey();
            } else {
               var12 = var11.getLMUserSessionKey();
            }
         }

         if ((var6 & 16) != 0) {
            if ((var6 & 1073741824) != 0) {
               this.sessionKey = NTLMEngineImpl.RC4(var11.getSecondaryKey(), var12);
            } else {
               this.sessionKey = var12;
            }
         } else {
            this.sessionKey = null;
         }

         try {
            this.hostBytes = var9 != null ? var9.getBytes("UnicodeLittleUnmarked") : null;
            this.domainBytes = var10 != null ? var10.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked") : null;
            this.userBytes = var3.getBytes("UnicodeLittleUnmarked");
         } catch (UnsupportedEncodingException var14) {
            throw new NTLMEngineException("Unicode not supported: " + var14.getMessage(), var14);
         }
      }

      String getResponse() {
         int var1 = this.ntResp.length;
         int var2 = this.lmResp.length;
         int var3 = this.domainBytes != null ? this.domainBytes.length : 0;
         int var4 = this.hostBytes != null ? this.hostBytes.length : 0;
         int var5 = this.userBytes.length;
         int var6;
         if (this.sessionKey != null) {
            var6 = this.sessionKey.length;
         } else {
            var6 = 0;
         }

         boolean var7 = true;
         int var8 = 72 + var2;
         int var9 = var8 + var1;
         int var10 = var9 + var3;
         int var11 = var10 + var5;
         int var12 = var11 + var4;
         int var13 = var12 + var6;
         this.prepareResponse(var13, 3);
         this.addUShort(var2);
         this.addUShort(var2);
         this.addULong(72);
         this.addUShort(var1);
         this.addUShort(var1);
         this.addULong(var8);
         this.addUShort(var3);
         this.addUShort(var3);
         this.addULong(var9);
         this.addUShort(var5);
         this.addUShort(var5);
         this.addULong(var10);
         this.addUShort(var4);
         this.addUShort(var4);
         this.addULong(var11);
         this.addUShort(var6);
         this.addUShort(var6);
         this.addULong(var12);
         this.addULong(this.type2Flags & 128 | this.type2Flags & 512 | this.type2Flags & 524288 | 33554432 | this.type2Flags & '耀' | this.type2Flags & 32 | this.type2Flags & 16 | this.type2Flags & 536870912 | this.type2Flags & Integer.MIN_VALUE | this.type2Flags & 1073741824 | this.type2Flags & 8388608 | this.type2Flags & 1 | this.type2Flags & 4);
         this.addUShort(261);
         this.addULong(2600);
         this.addUShort(3840);
         this.addBytes(this.lmResp);
         this.addBytes(this.ntResp);
         this.addBytes(this.domainBytes);
         this.addBytes(this.userBytes);
         this.addBytes(this.hostBytes);
         if (this.sessionKey != null) {
            this.addBytes(this.sessionKey);
         }

         return super.getResponse();
      }
   }

   static class Type2Message extends NTLMEngineImpl.NTLMMessage {
      protected byte[] challenge = new byte[8];
      protected String target;
      protected byte[] targetInfo;
      protected int flags;

      Type2Message(String var1) throws NTLMEngineException {
         super(var1, 2);
         this.readBytes(this.challenge, 24);
         this.flags = this.readULong(20);
         if ((this.flags & 1) == 0) {
            throw new NTLMEngineException("NTLM type 2 message has flags that make no sense: " + Integer.toString(this.flags));
         } else {
            this.target = null;
            byte[] var2;
            if (this.getMessageLength() >= 20) {
               var2 = this.readSecurityBuffer(12);
               if (var2.length != 0) {
                  try {
                     this.target = new String(var2, "UnicodeLittleUnmarked");
                  } catch (UnsupportedEncodingException var4) {
                     throw new NTLMEngineException(var4.getMessage(), var4);
                  }
               }
            }

            this.targetInfo = null;
            if (this.getMessageLength() >= 48) {
               var2 = this.readSecurityBuffer(40);
               if (var2.length != 0) {
                  this.targetInfo = var2;
               }
            }

         }
      }

      byte[] getChallenge() {
         return this.challenge;
      }

      String getTarget() {
         return this.target;
      }

      byte[] getTargetInfo() {
         return this.targetInfo;
      }

      int getFlags() {
         return this.flags;
      }
   }

   static class Type1Message extends NTLMEngineImpl.NTLMMessage {
      protected byte[] hostBytes;
      protected byte[] domainBytes;

      Type1Message(String var1, String var2) throws NTLMEngineException {
         try {
            String var3 = NTLMEngineImpl.access$1400(var2);
            String var4 = NTLMEngineImpl.access$1500(var1);
            this.hostBytes = var3 != null ? var3.getBytes("ASCII") : null;
            this.domainBytes = var4 != null ? var4.toUpperCase(Locale.US).getBytes("ASCII") : null;
         } catch (UnsupportedEncodingException var5) {
            throw new NTLMEngineException("Unicode unsupported: " + var5.getMessage(), var5);
         }
      }

      String getResponse() {
         boolean var1 = true;
         this.prepareResponse(40, 1);
         this.addULong(-1576500735);
         this.addUShort(0);
         this.addUShort(0);
         this.addULong(40);
         this.addUShort(0);
         this.addUShort(0);
         this.addULong(40);
         this.addUShort(261);
         this.addULong(2600);
         this.addUShort(3840);
         return super.getResponse();
      }
   }

   static class NTLMMessage {
      private byte[] messageContents = null;
      private int currentOutputPosition = 0;

      NTLMMessage() {
      }

      NTLMMessage(String var1, int var2) throws NTLMEngineException {
         this.messageContents = Base64.decodeBase64(EncodingUtils.getBytes(var1, "ASCII"));
         if (this.messageContents.length < NTLMEngineImpl.access$1000().length) {
            throw new NTLMEngineException("NTLM message decoding error - packet too short");
         } else {
            for(int var3 = 0; var3 < NTLMEngineImpl.access$1000().length; ++var3) {
               if (this.messageContents[var3] != NTLMEngineImpl.access$1000()[var3]) {
                  throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
               }
            }

            int var4 = this.readULong(NTLMEngineImpl.access$1000().length);
            if (var4 != var2) {
               throw new NTLMEngineException("NTLM type " + Integer.toString(var2) + " message expected - instead got type " + Integer.toString(var4));
            } else {
               this.currentOutputPosition = this.messageContents.length;
            }
         }
      }

      protected int getPreambleLength() {
         return NTLMEngineImpl.access$1000().length + 4;
      }

      protected int getMessageLength() {
         return this.currentOutputPosition;
      }

      protected byte readByte(int var1) throws NTLMEngineException {
         if (this.messageContents.length < var1 + 1) {
            throw new NTLMEngineException("NTLM: Message too short");
         } else {
            return this.messageContents[var1];
         }
      }

      protected void readBytes(byte[] var1, int var2) throws NTLMEngineException {
         if (this.messageContents.length < var2 + var1.length) {
            throw new NTLMEngineException("NTLM: Message too short");
         } else {
            System.arraycopy(this.messageContents, var2, var1, 0, var1.length);
         }
      }

      protected int readUShort(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.access$1100(this.messageContents, var1);
      }

      protected int readULong(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.access$1200(this.messageContents, var1);
      }

      protected byte[] readSecurityBuffer(int var1) throws NTLMEngineException {
         return NTLMEngineImpl.access$1300(this.messageContents, var1);
      }

      protected void prepareResponse(int var1, int var2) {
         this.messageContents = new byte[var1];
         this.currentOutputPosition = 0;
         this.addBytes(NTLMEngineImpl.access$1000());
         this.addULong(var2);
      }

      protected void addByte(byte var1) {
         this.messageContents[this.currentOutputPosition] = var1;
         ++this.currentOutputPosition;
      }

      protected void addBytes(byte[] var1) {
         if (var1 != null) {
            byte[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               byte var5 = var2[var4];
               this.messageContents[this.currentOutputPosition] = var5;
               ++this.currentOutputPosition;
            }

         }
      }

      protected void addUShort(int var1) {
         this.addByte((byte)(var1 & 255));
         this.addByte((byte)(var1 >> 8 & 255));
      }

      protected void addULong(int var1) {
         this.addByte((byte)(var1 & 255));
         this.addByte((byte)(var1 >> 8 & 255));
         this.addByte((byte)(var1 >> 16 & 255));
         this.addByte((byte)(var1 >> 24 & 255));
      }

      String getResponse() {
         byte[] var1;
         if (this.messageContents.length > this.currentOutputPosition) {
            byte[] var2 = new byte[this.currentOutputPosition];
            System.arraycopy(this.messageContents, 0, var2, 0, this.currentOutputPosition);
            var1 = var2;
         } else {
            var1 = this.messageContents;
         }

         return EncodingUtils.getAsciiString(Base64.encodeBase64(var1));
      }
   }

   protected static class CipherGen {
      protected final String domain;
      protected final String user;
      protected final String password;
      protected final byte[] challenge;
      protected final String target;
      protected final byte[] targetInformation;
      protected byte[] clientChallenge;
      protected byte[] clientChallenge2;
      protected byte[] secondaryKey;
      protected byte[] timestamp;
      protected byte[] lmHash;
      protected byte[] lmResponse;
      protected byte[] ntlmHash;
      protected byte[] ntlmResponse;
      protected byte[] ntlmv2Hash;
      protected byte[] lmv2Hash;
      protected byte[] lmv2Response;
      protected byte[] ntlmv2Blob;
      protected byte[] ntlmv2Response;
      protected byte[] ntlm2SessionResponse;
      protected byte[] lm2SessionResponse;
      protected byte[] lmUserSessionKey;
      protected byte[] ntlmUserSessionKey;
      protected byte[] ntlmv2UserSessionKey;
      protected byte[] ntlm2SessionResponseUserSessionKey;
      protected byte[] lanManagerSessionKey;

      public CipherGen(String var1, String var2, String var3, byte[] var4, String var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9, byte[] var10) {
         this.lmHash = null;
         this.lmResponse = null;
         this.ntlmHash = null;
         this.ntlmResponse = null;
         this.ntlmv2Hash = null;
         this.lmv2Hash = null;
         this.lmv2Response = null;
         this.ntlmv2Blob = null;
         this.ntlmv2Response = null;
         this.ntlm2SessionResponse = null;
         this.lm2SessionResponse = null;
         this.lmUserSessionKey = null;
         this.ntlmUserSessionKey = null;
         this.ntlmv2UserSessionKey = null;
         this.ntlm2SessionResponseUserSessionKey = null;
         this.lanManagerSessionKey = null;
         this.domain = var1;
         this.target = var5;
         this.user = var2;
         this.password = var3;
         this.challenge = var4;
         this.targetInformation = var6;
         this.clientChallenge = var7;
         this.clientChallenge2 = var8;
         this.secondaryKey = var9;
         this.timestamp = var10;
      }

      public CipherGen(String var1, String var2, String var3, byte[] var4, String var5, byte[] var6) {
         this(var1, var2, var3, var4, var5, var6, (byte[])null, (byte[])null, (byte[])null, (byte[])null);
      }

      public byte[] getClientChallenge() throws NTLMEngineException {
         if (this.clientChallenge == null) {
            this.clientChallenge = NTLMEngineImpl.access$000();
         }

         return this.clientChallenge;
      }

      public byte[] getClientChallenge2() throws NTLMEngineException {
         if (this.clientChallenge2 == null) {
            this.clientChallenge2 = NTLMEngineImpl.access$000();
         }

         return this.clientChallenge2;
      }

      public byte[] getSecondaryKey() throws NTLMEngineException {
         if (this.secondaryKey == null) {
            this.secondaryKey = NTLMEngineImpl.access$100();
         }

         return this.secondaryKey;
      }

      public byte[] getLMHash() throws NTLMEngineException {
         if (this.lmHash == null) {
            this.lmHash = NTLMEngineImpl.access$200(this.password);
         }

         return this.lmHash;
      }

      public byte[] getLMResponse() throws NTLMEngineException {
         if (this.lmResponse == null) {
            this.lmResponse = NTLMEngineImpl.access$300(this.getLMHash(), this.challenge);
         }

         return this.lmResponse;
      }

      public byte[] getNTLMHash() throws NTLMEngineException {
         if (this.ntlmHash == null) {
            this.ntlmHash = NTLMEngineImpl.access$400(this.password);
         }

         return this.ntlmHash;
      }

      public byte[] getNTLMResponse() throws NTLMEngineException {
         if (this.ntlmResponse == null) {
            this.ntlmResponse = NTLMEngineImpl.access$300(this.getNTLMHash(), this.challenge);
         }

         return this.ntlmResponse;
      }

      public byte[] getLMv2Hash() throws NTLMEngineException {
         if (this.lmv2Hash == null) {
            this.lmv2Hash = NTLMEngineImpl.access$500(this.domain, this.user, this.getNTLMHash());
         }

         return this.lmv2Hash;
      }

      public byte[] getNTLMv2Hash() throws NTLMEngineException {
         if (this.ntlmv2Hash == null) {
            this.ntlmv2Hash = NTLMEngineImpl.access$600(this.domain, this.user, this.getNTLMHash());
         }

         return this.ntlmv2Hash;
      }

      public byte[] getTimestamp() {
         if (this.timestamp == null) {
            long var1 = System.currentTimeMillis();
            var1 += 11644473600000L;
            var1 *= 10000L;
            this.timestamp = new byte[8];

            for(int var3 = 0; var3 < 8; ++var3) {
               this.timestamp[var3] = (byte)((int)var1);
               var1 >>>= 8;
            }
         }

         return this.timestamp;
      }

      public byte[] getNTLMv2Blob() throws NTLMEngineException {
         if (this.ntlmv2Blob == null) {
            this.ntlmv2Blob = NTLMEngineImpl.access$700(this.getClientChallenge2(), this.targetInformation, this.getTimestamp());
         }

         return this.ntlmv2Blob;
      }

      public byte[] getNTLMv2Response() throws NTLMEngineException {
         if (this.ntlmv2Response == null) {
            this.ntlmv2Response = NTLMEngineImpl.access$800(this.getNTLMv2Hash(), this.challenge, this.getNTLMv2Blob());
         }

         return this.ntlmv2Response;
      }

      public byte[] getLMv2Response() throws NTLMEngineException {
         if (this.lmv2Response == null) {
            this.lmv2Response = NTLMEngineImpl.access$800(this.getLMv2Hash(), this.challenge, this.getClientChallenge());
         }

         return this.lmv2Response;
      }

      public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
         if (this.ntlm2SessionResponse == null) {
            this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(this.getNTLMHash(), this.challenge, this.getClientChallenge());
         }

         return this.ntlm2SessionResponse;
      }

      public byte[] getLM2SessionResponse() throws NTLMEngineException {
         if (this.lm2SessionResponse == null) {
            byte[] var1 = this.getClientChallenge();
            this.lm2SessionResponse = new byte[24];
            System.arraycopy(var1, 0, this.lm2SessionResponse, 0, var1.length);
            Arrays.fill(this.lm2SessionResponse, var1.length, this.lm2SessionResponse.length, (byte)0);
         }

         return this.lm2SessionResponse;
      }

      public byte[] getLMUserSessionKey() throws NTLMEngineException {
         if (this.lmUserSessionKey == null) {
            byte[] var1 = this.getLMHash();
            this.lmUserSessionKey = new byte[16];
            System.arraycopy(var1, 0, this.lmUserSessionKey, 0, 8);
            Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
         }

         return this.lmUserSessionKey;
      }

      public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
         if (this.ntlmUserSessionKey == null) {
            byte[] var1 = this.getNTLMHash();
            NTLMEngineImpl.MD4 var2 = new NTLMEngineImpl.MD4();
            var2.update(var1);
            this.ntlmUserSessionKey = var2.getOutput();
         }

         return this.ntlmUserSessionKey;
      }

      public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
         if (this.ntlmv2UserSessionKey == null) {
            byte[] var1 = this.getNTLMv2Hash();
            byte[] var2 = new byte[16];
            System.arraycopy(this.getNTLMv2Response(), 0, var2, 0, 16);
            this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(var2, var1);
         }

         return this.ntlmv2UserSessionKey;
      }

      public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
         if (this.ntlm2SessionResponseUserSessionKey == null) {
            byte[] var1 = this.getNTLMUserSessionKey();
            byte[] var2 = this.getLM2SessionResponse();
            byte[] var3 = new byte[this.challenge.length + var2.length];
            System.arraycopy(this.challenge, 0, var3, 0, this.challenge.length);
            System.arraycopy(var2, 0, var3, this.challenge.length, var2.length);
            this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(var3, var1);
         }

         return this.ntlm2SessionResponseUserSessionKey;
      }

      public byte[] getLanManagerSessionKey() throws NTLMEngineException {
         if (this.lanManagerSessionKey == null) {
            byte[] var1 = this.getLMHash();
            byte[] var2 = this.getLMResponse();

            try {
               byte[] var3 = new byte[14];
               System.arraycopy(var1, 0, var3, 0, 8);
               Arrays.fill(var3, 8, var3.length, (byte)-67);
               Key var4 = NTLMEngineImpl.access$900(var3, 0);
               Key var5 = NTLMEngineImpl.access$900(var3, 7);
               byte[] var6 = new byte[8];
               System.arraycopy(var2, 0, var6, 0, var6.length);
               Cipher var7 = Cipher.getInstance("DES/ECB/NoPadding");
               var7.init(1, var4);
               byte[] var8 = var7.doFinal(var6);
               var7 = Cipher.getInstance("DES/ECB/NoPadding");
               var7.init(1, var5);
               byte[] var9 = var7.doFinal(var6);
               this.lanManagerSessionKey = new byte[16];
               System.arraycopy(var8, 0, this.lanManagerSessionKey, 0, var8.length);
               System.arraycopy(var9, 0, this.lanManagerSessionKey, var8.length, var9.length);
            } catch (Exception var10) {
               throw new NTLMEngineException(var10.getMessage(), var10);
            }
         }

         return this.lanManagerSessionKey;
      }
   }
}
