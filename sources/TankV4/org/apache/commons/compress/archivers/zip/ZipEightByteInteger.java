package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;
import java.math.BigInteger;

public final class ZipEightByteInteger implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final int BYTE_1 = 1;
   private static final int BYTE_1_MASK = 65280;
   private static final int BYTE_1_SHIFT = 8;
   private static final int BYTE_2 = 2;
   private static final int BYTE_2_MASK = 16711680;
   private static final int BYTE_2_SHIFT = 16;
   private static final int BYTE_3 = 3;
   private static final long BYTE_3_MASK = 4278190080L;
   private static final int BYTE_3_SHIFT = 24;
   private static final int BYTE_4 = 4;
   private static final long BYTE_4_MASK = 1095216660480L;
   private static final int BYTE_4_SHIFT = 32;
   private static final int BYTE_5 = 5;
   private static final long BYTE_5_MASK = 280375465082880L;
   private static final int BYTE_5_SHIFT = 40;
   private static final int BYTE_6 = 6;
   private static final long BYTE_6_MASK = 71776119061217280L;
   private static final int BYTE_6_SHIFT = 48;
   private static final int BYTE_7 = 7;
   private static final long BYTE_7_MASK = 9151314442816847872L;
   private static final int BYTE_7_SHIFT = 56;
   private static final int LEFTMOST_BIT_SHIFT = 63;
   private static final byte LEFTMOST_BIT = -128;
   private final BigInteger value;
   public static final ZipEightByteInteger ZERO = new ZipEightByteInteger(0L);

   public ZipEightByteInteger(long var1) {
      this(BigInteger.valueOf(var1));
   }

   public ZipEightByteInteger(BigInteger var1) {
      this.value = var1;
   }

   public ZipEightByteInteger(byte[] var1) {
      this(var1, 0);
   }

   public ZipEightByteInteger(byte[] var1, int var2) {
      this.value = getValue(var1, var2);
   }

   public byte[] getBytes() {
      return getBytes(this.value);
   }

   public long getLongValue() {
      return this.value.longValue();
   }

   public BigInteger getValue() {
      return this.value;
   }

   public static byte[] getBytes(long var0) {
      return getBytes(BigInteger.valueOf(var0));
   }

   public static byte[] getBytes(BigInteger var0) {
      byte[] var1 = new byte[8];
      long var2 = var0.longValue();
      var1[0] = (byte)((int)(var2 & 255L));
      var1[1] = (byte)((int)((var2 & 65280L) >> 8));
      var1[2] = (byte)((int)((var2 & 16711680L) >> 16));
      var1[3] = (byte)((int)((var2 & 4278190080L) >> 24));
      var1[4] = (byte)((int)((var2 & 1095216660480L) >> 32));
      var1[5] = (byte)((int)((var2 & 280375465082880L) >> 40));
      var1[6] = (byte)((int)((var2 & 71776119061217280L) >> 48));
      var1[7] = (byte)((int)((var2 & 9151314442816847872L) >> 56));
      if (var0.testBit(63)) {
         var1[7] |= -128;
      }

      return var1;
   }

   public static long getLongValue(byte[] var0, int var1) {
      return getValue(var0, var1).longValue();
   }

   public static BigInteger getValue(byte[] var0, int var1) {
      long var2 = (long)var0[var1 + 7] << 56 & 9151314442816847872L;
      var2 += (long)var0[var1 + 6] << 48 & 71776119061217280L;
      var2 += (long)var0[var1 + 5] << 40 & 280375465082880L;
      var2 += (long)var0[var1 + 4] << 32 & 1095216660480L;
      var2 += (long)var0[var1 + 3] << 24 & 4278190080L;
      var2 += (long)var0[var1 + 2] << 16 & 16711680L;
      var2 += (long)var0[var1 + 1] << 8 & 65280L;
      var2 += (long)var0[var1] & 255L;
      BigInteger var4 = BigInteger.valueOf(var2);
      return (var0[var1 + 7] & -128) == -128 ? var4.setBit(63) : var4;
   }

   public static long getLongValue(byte[] var0) {
      return getLongValue(var0, 0);
   }

   public static BigInteger getValue(byte[] var0) {
      return getValue(var0, 0);
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof ZipEightByteInteger ? this.value.equals(((ZipEightByteInteger)var1).getValue()) : false;
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public String toString() {
      return "ZipEightByteInteger value: " + this.value;
   }
}
