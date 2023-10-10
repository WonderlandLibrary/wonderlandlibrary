package com.jcraft.jogg;

public class Page {
   private static int[] crc_lookup = new int[256];
   public byte[] header_base;
   public int header;
   public int header_len;
   public byte[] body_base;
   public int body;
   public int body_len;

   private static int crc_entry(int var0) {
      int var1 = var0 << 24;

      for(int var2 = 0; var2 < 8; ++var2) {
         if ((var1 & Integer.MIN_VALUE) != 0) {
            var1 = var1 << 1 ^ 79764919;
         } else {
            var1 <<= 1;
         }
      }

      return var1 & -1;
   }

   int version() {
      return this.header_base[this.header + 4] & 255;
   }

   int continued() {
      return this.header_base[this.header + 5] & 1;
   }

   public int bos() {
      return this.header_base[this.header + 5] & 2;
   }

   public int eos() {
      return this.header_base[this.header + 5] & 4;
   }

   public long granulepos() {
      long var1 = (long)(this.header_base[this.header + 13] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 12] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 11] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 10] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 9] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 8] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 7] & 255);
      var1 = var1 << 8 | (long)(this.header_base[this.header + 6] & 255);
      return var1;
   }

   public int serialno() {
      return this.header_base[this.header + 14] & 255 | (this.header_base[this.header + 15] & 255) << 8 | (this.header_base[this.header + 16] & 255) << 16 | (this.header_base[this.header + 17] & 255) << 24;
   }

   int pageno() {
      return this.header_base[this.header + 18] & 255 | (this.header_base[this.header + 19] & 255) << 8 | (this.header_base[this.header + 20] & 255) << 16 | (this.header_base[this.header + 21] & 255) << 24;
   }

   void checksum() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.header_len; ++var2) {
         var1 = var1 << 8 ^ crc_lookup[var1 >>> 24 & 255 ^ this.header_base[this.header + var2] & 255];
      }

      for(var2 = 0; var2 < this.body_len; ++var2) {
         var1 = var1 << 8 ^ crc_lookup[var1 >>> 24 & 255 ^ this.body_base[this.body + var2] & 255];
      }

      this.header_base[this.header + 22] = (byte)var1;
      this.header_base[this.header + 23] = (byte)(var1 >>> 8);
      this.header_base[this.header + 24] = (byte)(var1 >>> 16);
      this.header_base[this.header + 25] = (byte)(var1 >>> 24);
   }

   public Page copy() {
      return this.copy(new Page());
   }

   public Page copy(Page var1) {
      byte[] var2 = new byte[this.header_len];
      System.arraycopy(this.header_base, this.header, var2, 0, this.header_len);
      var1.header_len = this.header_len;
      var1.header_base = var2;
      var1.header = 0;
      var2 = new byte[this.body_len];
      System.arraycopy(this.body_base, this.body, var2, 0, this.body_len);
      var1.body_len = this.body_len;
      var1.body_base = var2;
      var1.body = 0;
      return var1;
   }

   static {
      for(int var0 = 0; var0 < crc_lookup.length; ++var0) {
         crc_lookup[var0] = crc_entry(var0);
      }

   }
}
