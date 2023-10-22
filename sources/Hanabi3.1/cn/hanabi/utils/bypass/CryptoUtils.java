package cn.hanabi.utils.bypass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CryptoUtils {
   public static void encryptString(OutputStream out, String str) throws IOException {
      if (str == null) {
         encrypt(out, -1);
      } else if (str.isEmpty()) {
         encrypt(out, 0);
      } else {
         byte[] bs = str.getBytes(StandardCharsets.UTF_8);
         encrypt(out, bs.length);
         out.write(bs);
      }

   }

   public static String decryptString(InputStream in) throws IOException {
      int length = decrypt(in);
      if (length < 0) {
         return null;
      } else if (length == 0) {
         return "";
      } else {
         byte[] value = new byte[length];
         in.read(value);
         return new String(value, StandardCharsets.UTF_8);
      }
   }

   public static void encrypt(OutputStream out, int i) throws IOException {
      out.write((byte)(i >> 24));
      out.write((byte)(i >> 16));
      out.write((byte)(i >> 8));
      out.write(i);
   }

   public static int decrypt(InputStream in) throws IOException {
      return ((byte)in.read() & 255) << 24 | ((byte)in.read() & 255) << 16 | ((byte)in.read() & 255) << 8 | (byte)in.read() & 255;
   }
}
