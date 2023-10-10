package com.jcraft.jogg;

public class SyncState {
   public byte[] data;
   int storage;
   int fill;
   int returned;
   int unsynced;
   int headerbytes;
   int bodybytes;
   private Page pageseek = new Page();
   private byte[] chksum = new byte[4];

   public int clear() {
      this.data = null;
      return 0;
   }

   public int buffer(int var1) {
      if (this.returned != 0) {
         this.fill -= this.returned;
         if (this.fill > 0) {
            System.arraycopy(this.data, this.returned, this.data, 0, this.fill);
         }

         this.returned = 0;
      }

      if (var1 > this.storage - this.fill) {
         int var2 = var1 + this.fill + 4096;
         if (this.data != null) {
            byte[] var3 = new byte[var2];
            System.arraycopy(this.data, 0, var3, 0, this.data.length);
            this.data = var3;
         } else {
            this.data = new byte[var2];
         }

         this.storage = var2;
      }

      return this.fill;
   }

   public int wrote(int var1) {
      if (this.fill + var1 > this.storage) {
         return -1;
      } else {
         this.fill += var1;
         return 0;
      }
   }

   public int pageseek(Page var1) {
      int var2 = this.returned;
      int var4 = this.fill - this.returned;
      int var3;
      int var7;
      if (this.headerbytes == 0) {
         if (var4 < 27) {
            return 0;
         }

         if (this.data[var2] != 79 || this.data[var2 + 1] != 103 || this.data[var2 + 2] != 103 || this.data[var2 + 3] != 83) {
            this.headerbytes = 0;
            this.bodybytes = 0;
            var3 = 0;

            for(var7 = 0; var7 < var4 - 1; ++var7) {
               if (this.data[var2 + 1 + var7] == 79) {
                  var3 = var2 + 1 + var7;
                  break;
               }
            }

            if (var3 == 0) {
               var3 = this.fill;
            }

            this.returned = var3;
            return -(var3 - var2);
         }

         int var5 = (this.data[var2 + 26] & 255) + 27;
         if (var4 < var5) {
            return 0;
         }

         for(int var6 = 0; var6 < (this.data[var2 + 26] & 255); ++var6) {
            this.bodybytes += this.data[var2 + 27 + var6] & 255;
         }

         this.headerbytes = var5;
      }

      if (this.bodybytes + this.headerbytes > var4) {
         return 0;
      } else {
         byte[] var9;
         synchronized(var9 = this.chksum){}
         System.arraycopy(this.data, var2 + 22, this.chksum, 0, 4);
         this.data[var2 + 22] = 0;
         this.data[var2 + 23] = 0;
         this.data[var2 + 24] = 0;
         this.data[var2 + 25] = 0;
         Page var10 = this.pageseek;
         var10.header_base = this.data;
         var10.header = var2;
         var10.header_len = this.headerbytes;
         var10.body_base = this.data;
         var10.body = var2 + this.headerbytes;
         var10.body_len = this.bodybytes;
         var10.checksum();
         if (this.chksum[0] == this.data[var2 + 22] && this.chksum[1] == this.data[var2 + 23] && this.chksum[2] == this.data[var2 + 24] && this.chksum[3] == this.data[var2 + 25]) {
            var2 = this.returned;
            if (var1 != null) {
               var1.header_base = this.data;
               var1.header = var2;
               var1.header_len = this.headerbytes;
               var1.body_base = this.data;
               var1.body = var2 + this.headerbytes;
               var1.body_len = this.bodybytes;
            }

            this.unsynced = 0;
            this.returned += var4 = this.headerbytes + this.bodybytes;
            this.headerbytes = 0;
            this.bodybytes = 0;
            return var4;
         } else {
            System.arraycopy(this.chksum, 0, this.data, var2 + 22, 4);
            this.headerbytes = 0;
            this.bodybytes = 0;
            var3 = 0;

            for(var7 = 0; var7 < var4 - 1; ++var7) {
               if (this.data[var2 + 1 + var7] == 79) {
                  var3 = var2 + 1 + var7;
                  break;
               }
            }

            if (var3 == 0) {
               var3 = this.fill;
            }

            this.returned = var3;
            return -(var3 - var2);
         }
      }
   }

   public int pageout(Page var1) {
      do {
         int var2 = this.pageseek(var1);
         if (var2 > 0) {
            return 1;
         }

         if (var2 == 0) {
            return 0;
         }
      } while(this.unsynced != 0);

      this.unsynced = 1;
      return -1;
   }

   public int reset() {
      this.fill = 0;
      this.returned = 0;
      this.unsynced = 0;
      this.headerbytes = 0;
      this.bodybytes = 0;
      return 0;
   }

   public void init() {
   }

   public int getDataOffset() {
      return this.returned;
   }

   public int getBufferOffset() {
      return this.fill;
   }
}
