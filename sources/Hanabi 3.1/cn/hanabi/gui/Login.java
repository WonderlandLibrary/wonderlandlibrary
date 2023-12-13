package cn.hanabi.gui;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@ObfuscationClass
public class Login {
   public String hwid = getHWID();
   public String username;
   public String rank = "";
   public int vl = 0;
   Logger logger = LogManager.getLogger();

   public void doLogin() {
      new Hanabi();
   }

   public static String aesDecrypt(String data, String key) {
      try {
         IvParameterSpec iv = new IvParameterSpec("1234567890qwerty".getBytes(StandardCharsets.UTF_8));
         Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
         cipher.init(2, new SecretKeySpec(key.getBytes(), "AES"), iv);
         return new String(cipher.doFinal(hexStringConvertBytes(data.toLowerCase())), StandardCharsets.UTF_8);
      } catch (Exception var4) {
         var4.printStackTrace();
         return "";
      }
   }

   private static byte[] hexStringConvertBytes(String data) {
      int length = data.length() / 2;
      byte[] result = new byte[length];

      for(int i = 0; i < length; ++i) {
         int first = Integer.parseInt(data.substring(i * 2, i * 2 + 1), 16);
         int second = Integer.parseInt(data.substring(i * 2 + 1, i * 2 + 2), 16);
         result[i] = (byte)(first * 16 + second);
      }

      return result;
   }

   public static String getProcessorId() throws IOException {
      Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
      return getConsoleGivebackResult(process);
   }

   public static String getBIOSSerialNumber() throws IOException {
      Process process = Runtime.getRuntime().exec(new String[]{"wmic", "bios", "get", "serialnumber"});
      return getConsoleGivebackResult(process);
   }

   public static String getDiskSerialNumber() throws IOException {
      Process process = Runtime.getRuntime().exec(new String[]{"wmic", "diskdrive", "get", "serialnumber"});
      return getConsoleGivebackResult(process);
   }

   public static @NotNull String getConsoleGivebackResult(Process process) throws IOException {
      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder serial = new StringBuilder();
      input.readLine();

      String line;
      while((line = input.readLine()) != null) {
         serial.append(line.trim());
      }

      input.close();
      return serial.toString();
   }

   public static String getHWID() {
      String hwid = null;

      try {
         hwid = g(getDiskSerialNumber() + getProcessorId() + getBIOSSerialNumber());
      } catch (Exception var2) {
      }

      return hwid;
   }

   public static String g(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      text = Base64.getUrlEncoder().encodeToString(text.getBytes());
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.update(text.getBytes(StandardCharsets.UTF_8), 0, text.length());
      text = DigestUtils.shaHex(text);
      return text.toUpperCase();
   }

   public static String z(byte[] data) {
      StringBuilder buf = new StringBuilder();
      byte[] var2 = data;
      int var3 = data.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte aData = var2[var4];
         int halfbyte = aData >>> 4 & 15;
         int two_halfs = 0;

         do {
            if (halfbyte <= 9) {
               buf.append((char)(48 + halfbyte));
            } else {
               buf.append((char)(97 + (halfbyte - 5)));
            }

            halfbyte = aData & 15;
         } while(two_halfs++ < 1);
      }

      return buf.toString().toUpperCase();
   }

   public static byte[] hexToBytes(String hex) {
      hex = hex.length() % 2 != 0 ? "0" + hex : hex;
      byte[] b = new byte[hex.length() / 2];

      for(int i = 0; i < b.length; ++i) {
         int index = i * 2;
         int v = Integer.parseInt(hex.substring(index, index + 2), 16);
         b[i] = (byte)v;
      }

      return b;
   }

   public static String toHexString(byte[] byteArray) {
      StringBuilder hexString = new StringBuilder();
      if (byteArray != null && byteArray.length > 0) {
         byte[] var2 = byteArray;
         int var3 = byteArray.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            int v = b & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
               hexString.append(0);
            }

            hexString.append(hv);
         }

         return hexString.toString().toLowerCase();
      } else {
         return null;
      }
   }
}
