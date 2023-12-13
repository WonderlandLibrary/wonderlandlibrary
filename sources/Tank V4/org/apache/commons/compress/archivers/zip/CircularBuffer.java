package org.apache.commons.compress.archivers.zip;

class CircularBuffer {
   private final int size;
   private final byte[] buffer;
   private int readIndex;
   private int writeIndex;

   CircularBuffer(int var1) {
      this.size = var1;
      this.buffer = new byte[var1];
   }

   public void put(int var1) {
      this.buffer[this.writeIndex] = (byte)var1;
      this.writeIndex = (this.writeIndex + 1) % this.size;
   }

   public int get() {
      // $FF: Couldn't be decompiled
   }

   public void copy(int var1, int var2) {
      int var3 = this.writeIndex - var1;
      int var4 = var3 + var2;

      for(int var5 = var3; var5 < var4; ++var5) {
         this.buffer[this.writeIndex] = this.buffer[(var5 + this.size) % this.size];
         this.writeIndex = (this.writeIndex + 1) % this.size;
      }

   }
}
