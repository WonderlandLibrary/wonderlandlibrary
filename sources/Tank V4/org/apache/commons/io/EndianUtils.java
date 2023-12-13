package org.apache.commons.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
   public static short swapShort(short var0) {
      return (short)(((var0 >> 0 & 255) << 8) + ((var0 >> 8 & 255) << 0));
   }

   public static int swapInteger(int var0) {
      return ((var0 >> 0 & 255) << 24) + ((var0 >> 8 & 255) << 16) + ((var0 >> 16 & 255) << 8) + ((var0 >> 24 & 255) << 0);
   }

   public static long swapLong(long var0) {
      return ((var0 >> 0 & 255L) << 56) + ((var0 >> 8 & 255L) << 48) + ((var0 >> 16 & 255L) << 40) + ((var0 >> 24 & 255L) << 32) + ((var0 >> 32 & 255L) << 24) + ((var0 >> 40 & 255L) << 16) + ((var0 >> 48 & 255L) << 8) + ((var0 >> 56 & 255L) << 0);
   }

   public static float swapFloat(float var0) {
      return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(var0)));
   }

   public static double swapDouble(double var0) {
      return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(var0)));
   }

   public static void writeSwappedShort(byte[] var0, int var1, short var2) {
      var0[var1 + 0] = (byte)(var2 >> 0 & 255);
      var0[var1 + 1] = (byte)(var2 >> 8 & 255);
   }

   public static short readSwappedShort(byte[] var0, int var1) {
      return (short)(((var0[var1 + 0] & 255) << 0) + ((var0[var1 + 1] & 255) << 8));
   }

   public static int readSwappedUnsignedShort(byte[] var0, int var1) {
      return ((var0[var1 + 0] & 255) << 0) + ((var0[var1 + 1] & 255) << 8);
   }

   public static void writeSwappedInteger(byte[] var0, int var1, int var2) {
      var0[var1 + 0] = (byte)(var2 >> 0 & 255);
      var0[var1 + 1] = (byte)(var2 >> 8 & 255);
      var0[var1 + 2] = (byte)(var2 >> 16 & 255);
      var0[var1 + 3] = (byte)(var2 >> 24 & 255);
   }

   public static int readSwappedInteger(byte[] var0, int var1) {
      return ((var0[var1 + 0] & 255) << 0) + ((var0[var1 + 1] & 255) << 8) + ((var0[var1 + 2] & 255) << 16) + ((var0[var1 + 3] & 255) << 24);
   }

   public static long readSwappedUnsignedInteger(byte[] var0, int var1) {
      long var2 = (long)(((var0[var1 + 0] & 255) << 0) + ((var0[var1 + 1] & 255) << 8) + ((var0[var1 + 2] & 255) << 16));
      long var4 = (long)(var0[var1 + 3] & 255);
      return (var4 << 24) + (4294967295L & var2);
   }

   public static void writeSwappedLong(byte[] var0, int var1, long var2) {
      var0[var1 + 0] = (byte)((int)(var2 >> 0 & 255L));
      var0[var1 + 1] = (byte)((int)(var2 >> 8 & 255L));
      var0[var1 + 2] = (byte)((int)(var2 >> 16 & 255L));
      var0[var1 + 3] = (byte)((int)(var2 >> 24 & 255L));
      var0[var1 + 4] = (byte)((int)(var2 >> 32 & 255L));
      var0[var1 + 5] = (byte)((int)(var2 >> 40 & 255L));
      var0[var1 + 6] = (byte)((int)(var2 >> 48 & 255L));
      var0[var1 + 7] = (byte)((int)(var2 >> 56 & 255L));
   }

   public static long readSwappedLong(byte[] var0, int var1) {
      long var2 = (long)(((var0[var1 + 0] & 255) << 0) + ((var0[var1 + 1] & 255) << 8) + ((var0[var1 + 2] & 255) << 16) + ((var0[var1 + 3] & 255) << 24));
      long var4 = (long)(((var0[var1 + 4] & 255) << 0) + ((var0[var1 + 5] & 255) << 8) + ((var0[var1 + 6] & 255) << 16) + ((var0[var1 + 7] & 255) << 24));
      return (var4 << 32) + (4294967295L & var2);
   }

   public static void writeSwappedFloat(byte[] var0, int var1, float var2) {
      writeSwappedInteger(var0, var1, Float.floatToIntBits(var2));
   }

   public static float readSwappedFloat(byte[] var0, int var1) {
      return Float.intBitsToFloat(readSwappedInteger(var0, var1));
   }

   public static void writeSwappedDouble(byte[] var0, int var1, double var2) {
      writeSwappedLong(var0, var1, Double.doubleToLongBits(var2));
   }

   public static double readSwappedDouble(byte[] var0, int var1) {
      return Double.longBitsToDouble(readSwappedLong(var0, var1));
   }

   public static void writeSwappedShort(OutputStream var0, short var1) throws IOException {
      var0.write((byte)(var1 >> 0 & 255));
      var0.write((byte)(var1 >> 8 & 255));
   }

   public static short readSwappedShort(InputStream var0) throws IOException {
      return (short)(((read(var0) & 255) << 0) + ((read(var0) & 255) << 8));
   }

   public static int readSwappedUnsignedShort(InputStream var0) throws IOException {
      int var1 = read(var0);
      int var2 = read(var0);
      return ((var1 & 255) << 0) + ((var2 & 255) << 8);
   }

   public static void writeSwappedInteger(OutputStream var0, int var1) throws IOException {
      var0.write((byte)(var1 >> 0 & 255));
      var0.write((byte)(var1 >> 8 & 255));
      var0.write((byte)(var1 >> 16 & 255));
      var0.write((byte)(var1 >> 24 & 255));
   }

   public static int readSwappedInteger(InputStream var0) throws IOException {
      int var1 = read(var0);
      int var2 = read(var0);
      int var3 = read(var0);
      int var4 = read(var0);
      return ((var1 & 255) << 0) + ((var2 & 255) << 8) + ((var3 & 255) << 16) + ((var4 & 255) << 24);
   }

   public static long readSwappedUnsignedInteger(InputStream var0) throws IOException {
      int var1 = read(var0);
      int var2 = read(var0);
      int var3 = read(var0);
      int var4 = read(var0);
      long var5 = (long)(((var1 & 255) << 0) + ((var2 & 255) << 8) + ((var3 & 255) << 16));
      long var7 = (long)(var4 & 255);
      return (var7 << 24) + (4294967295L & var5);
   }

   public static void writeSwappedLong(OutputStream var0, long var1) throws IOException {
      var0.write((byte)((int)(var1 >> 0 & 255L)));
      var0.write((byte)((int)(var1 >> 8 & 255L)));
      var0.write((byte)((int)(var1 >> 16 & 255L)));
      var0.write((byte)((int)(var1 >> 24 & 255L)));
      var0.write((byte)((int)(var1 >> 32 & 255L)));
      var0.write((byte)((int)(var1 >> 40 & 255L)));
      var0.write((byte)((int)(var1 >> 48 & 255L)));
      var0.write((byte)((int)(var1 >> 56 & 255L)));
   }

   public static long readSwappedLong(InputStream var0) throws IOException {
      byte[] var1 = new byte[8];

      for(int var2 = 0; var2 < 8; ++var2) {
         var1[var2] = (byte)read(var0);
      }

      return readSwappedLong(var1, 0);
   }

   public static void writeSwappedFloat(OutputStream var0, float var1) throws IOException {
      writeSwappedInteger(var0, Float.floatToIntBits(var1));
   }

   public static float readSwappedFloat(InputStream var0) throws IOException {
      return Float.intBitsToFloat(readSwappedInteger(var0));
   }

   public static void writeSwappedDouble(OutputStream var0, double var1) throws IOException {
      writeSwappedLong(var0, Double.doubleToLongBits(var1));
   }

   public static double readSwappedDouble(InputStream var0) throws IOException {
      return Double.longBitsToDouble(readSwappedLong(var0));
   }

   private static int read(InputStream var0) throws IOException {
      int var1 = var0.read();
      if (-1 == var1) {
         throw new EOFException("Unexpected EOF reached");
      } else {
         return var1;
      }
   }
}
