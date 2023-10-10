package javassist.bytecode;

class ByteVector implements Cloneable {
   private byte[] buffer = new byte[64];
   private int size = 0;

   public ByteVector() {
   }

   public Object clone() throws CloneNotSupportedException {
      ByteVector var1 = (ByteVector)super.clone();
      var1.buffer = (byte[])((byte[])this.buffer.clone());
      return var1;
   }

   public final int getSize() {
      return this.size;
   }

   public final byte[] copy() {
      byte[] var1 = new byte[this.size];
      System.arraycopy(this.buffer, 0, var1, 0, this.size);
      return var1;
   }

   public int read(int var1) {
      if (var1 >= 0 && this.size > var1) {
         return this.buffer[var1];
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public void write(int var1, int var2) {
      if (var1 >= 0 && this.size > var1) {
         this.buffer[var1] = (byte)var2;
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public void add(int var1) {
      this.addGap(1);
      this.buffer[this.size - 1] = (byte)var1;
   }

   public void add(int var1, int var2) {
      this.addGap(2);
      this.buffer[this.size - 2] = (byte)var1;
      this.buffer[this.size - 1] = (byte)var2;
   }

   public void add(int var1, int var2, int var3, int var4) {
      this.addGap(4);
      this.buffer[this.size - 4] = (byte)var1;
      this.buffer[this.size - 3] = (byte)var2;
      this.buffer[this.size - 2] = (byte)var3;
      this.buffer[this.size - 1] = (byte)var4;
   }

   public void addGap(int var1) {
      if (this.size + var1 > this.buffer.length) {
         int var2 = this.size << 1;
         if (var2 < this.size + var1) {
            var2 = this.size + var1;
         }

         byte[] var3 = new byte[var2];
         System.arraycopy(this.buffer, 0, var3, 0, this.size);
         this.buffer = var3;
      }

      this.size += var1;
   }
}
