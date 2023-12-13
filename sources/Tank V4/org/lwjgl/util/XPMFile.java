package org.lwjgl.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class XPMFile {
   private byte[] bytes;
   private static final int WIDTH = 0;
   private static final int HEIGHT = 1;
   private static final int NUMBER_OF_COLORS = 2;
   private static final int CHARACTERS_PER_PIXEL = 3;
   private static int[] format = new int[4];

   private XPMFile() {
   }

   public static XPMFile load(String var0) throws IOException {
      return load((InputStream)(new FileInputStream(new File(var0))));
   }

   public static XPMFile load(InputStream var0) {
      XPMFile var1 = new XPMFile();
      var1.readImage(var0);
      return var1;
   }

   public int getHeight() {
      return format[1];
   }

   public int getWidth() {
      return format[0];
   }

   public byte[] getBytes() {
      return this.bytes;
   }

   private void readImage(InputStream var1) {
      try {
         LineNumberReader var2 = new LineNumberReader(new InputStreamReader(var1));
         HashMap var3 = new HashMap();
         format = parseFormat(nextLineOfInterest(var2));

         int var4;
         for(var4 = 0; var4 < format[2]; ++var4) {
            Object[] var5 = parseColor(nextLineOfInterest(var2));
            var3.put((String)var5[0], (Integer)var5[1]);
         }

         this.bytes = new byte[format[0] * format[1] * 4];

         for(var4 = 0; var4 < format[1]; ++var4) {
            this.parseImageLine(nextLineOfInterest(var2), format, var3, var4);
         }

      } catch (Exception var6) {
         var6.printStackTrace();
         throw new IllegalArgumentException("Unable to parse XPM File");
      }
   }

   private static String nextLineOfInterest(LineNumberReader var0) throws IOException {
      String var1;
      do {
         var1 = var0.readLine();
      } while(!var1.startsWith("\""));

      return var1.substring(1, var1.lastIndexOf(34));
   }

   private static int[] parseFormat(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0);
      return new int[]{Integer.parseInt(var1.nextToken()), Integer.parseInt(var1.nextToken()), Integer.parseInt(var1.nextToken()), Integer.parseInt(var1.nextToken())};
   }

   private static Object[] parseColor(String var0) {
      String var1 = var0.substring(0, format[3]);
      String var2 = var0.substring(format[3] + 4);
      return new Object[]{var1, Integer.parseInt(var2, 16)};
   }

   private void parseImageLine(String var1, int[] var2, HashMap var3, int var4) {
      int var5 = var4 * 4 * var2[0];

      for(int var6 = 0; var6 < var2[0]; ++var6) {
         String var7 = var1.substring(var6 * var2[3], var6 * var2[3] + var2[3]);
         int var8 = (Integer)var3.get(var7);
         this.bytes[var5 + var6 * 4] = (byte)((var8 & 16711680) >> 16);
         this.bytes[var5 + var6 * 4 + 1] = (byte)((var8 & '\uff00') >> 8);
         this.bytes[var5 + var6 * 4 + 2] = (byte)((var8 & 255) >> 0);
         this.bytes[var5 + var6 * 4 + 3] = -1;
      }

   }

   public static void main(String[] var0) {
      if (var0.length != 1) {
         System.out.println("usage:\nXPMFile <file>");
      }

      try {
         String var1 = var0[0].substring(0, var0[0].indexOf(".")) + ".raw";
         XPMFile var2 = load(var0[0]);
         BufferedOutputStream var3 = new BufferedOutputStream(new FileOutputStream(new File(var1)));
         var3.write(var2.getBytes());
         var3.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
