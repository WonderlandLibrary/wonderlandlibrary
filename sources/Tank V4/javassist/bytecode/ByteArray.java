package javassist.bytecode;

public class ByteArray {
   public static int readU16bit(byte[] var0, int var1) {
      return (var0[var1] & 255) << 8 | var0[var1 + 1] & 255;
   }

   public static int readS16bit(byte[] var0, int var1) {
      return var0[var1] << 8 | var0[var1 + 1] & 255;
   }

   public static void write16bit(int var0, byte[] var1, int var2) {
      var1[var2] = (byte)(var0 >>> 8);
      var1[var2 + 1] = (byte)var0;
   }

   public static int read32bit(byte[] var0, int var1) {
      return var0[var1] << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
   }

   public static void write32bit(int var0, byte[] var1, int var2) {
      var1[var2] = (byte)(var0 >>> 24);
      var1[var2 + 1] = (byte)(var0 >>> 16);
      var1[var2 + 2] = (byte)(var0 >>> 8);
      var1[var2 + 3] = (byte)var0;
   }

   static void copy32bit(byte[] var0, int var1, byte[] var2, int var3) {
      var2[var3] = var0[var1];
      var2[var3 + 1] = var0[var1 + 1];
      var2[var3 + 2] = var0[var1 + 2];
      var2[var3 + 3] = var0[var1 + 3];
   }
}
