package net.minecraft.world.chunk;

public class NibbleArray {
   private final byte[] data;

   public void setIndex(int var1, int var2) {
      int var3 = this.getNibbleIndex(var1);
      if (var1 == 0) {
         this.data[var3] = (byte)(this.data[var3] & 240 | var2 & 15);
      } else {
         this.data[var3] = (byte)(this.data[var3] & 15 | (var2 & 15) << 4);
      }

   }

   private int getCoordinateIndex(int var1, int var2, int var3) {
      return var2 << 8 | var3 << 4 | var1;
   }

   public void set(int var1, int var2, int var3, int var4) {
      this.setIndex(this.getCoordinateIndex(var1, var2, var3), var4);
   }

   public NibbleArray(byte[] var1) {
      this.data = var1;
      if (var1.length != 2048) {
         throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + var1.length);
      }
   }

   public byte[] getData() {
      return this.data;
   }

   public int getFromIndex(int var1) {
      int var2 = this.getNibbleIndex(var1);
      return var1 == 0 ? this.data[var2] & 15 : this.data[var2] >> 4 & 15;
   }

   private int getNibbleIndex(int var1) {
      return var1 >> 1;
   }

   public NibbleArray() {
      this.data = new byte[2048];
   }

   public int get(int var1, int var2, int var3) {
      return this.getFromIndex(this.getCoordinateIndex(var1, var2, var3));
   }
}
