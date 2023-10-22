package cn.hanabi.utils.bypass;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.fileSystem.Base58;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;

public class AESUtil {
   private final String KEY;

   public static void main(String[] args) {
      AESUtil aesUtil = new AESUtil(1);
      String s = aesUtil.AESEncode("1");
      Hanabi.INSTANCE.println(s);
   }

   public AESUtil(int key) {
      this.KEY = this.genKey(key);
   }

   private String genKey(int a) {
      String base = Base64.getEncoder().encodeToString("114514".getBytes(StandardCharsets.UTF_8));
      long hour = Long.parseLong(new String(Base64.getDecoder().decode(base)));
      Random random = new Random(hour * 2L);

      for(int i = a; i != 0; --i) {
         random.nextInt(63667);
      }

      String key = DigestUtils.md5Hex(String.valueOf(random.nextInt(18833)));
      return DigestUtils.md5Hex(Base64.getEncoder().encodeToString(key.getBytes()).substring(a / 4, a / 2));
   }

   public String AESEncode(String content) {
      try {
         KeyGenerator keygen = KeyGenerator.getInstance("AES");
         SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
         random.setSeed(this.KEY.getBytes());
         keygen.init(128, random);
         SecretKey original_key = keygen.generateKey();
         byte[] raw = original_key.getEncoded();
         SecretKey key = new SecretKeySpec(raw, "AES");
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(1, key);
         byte[] byte_encode = content.getBytes(StandardCharsets.UTF_8);
         byte[] byte_AES = cipher.doFinal(byte_encode);
         return new String(Base64.getEncoder().encode(byte_AES));
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }

   public String Encode(String content) {
      try {
         KeyGenerator keygen = KeyGenerator.getInstance("AES");
         SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
         random.setSeed(this.KEY.getBytes());
         keygen.init(128, random);
         SecretKey original_key = keygen.generateKey();
         byte[] raw = original_key.getEncoded();
         SecretKey key = new SecretKeySpec(raw, "AES");
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(1, key);
         byte[] byte_encode = content.getBytes(StandardCharsets.ISO_8859_1);
         byte[] byte_AES = cipher.doFinal(byte_encode);
         return (new Base58(23241)).encode(byte_AES);
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }
}
