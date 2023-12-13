package javassist.bytecode;

final class LongVector {
   static final int ASIZE = 128;
   static final int ABITS = 7;
   static final int VSIZE = 8;
   private ConstInfo[][] objects;
   private int elements;

   public LongVector() {
      this.objects = new ConstInfo[8][];
      this.elements = 0;
   }

   public LongVector(int var1) {
      int var2 = (var1 >> 7 & -8) + 8;
      this.objects = new ConstInfo[var2][];
      this.elements = 0;
   }

   public int size() {
      return this.elements;
   }

   public int capacity() {
      return this.objects.length * 128;
   }

   public ConstInfo elementAt(int var1) {
      return var1 >= 0 && this.elements > var1 ? this.objects[var1 >> 7][var1 & 127] : null;
   }

   public void addElement(ConstInfo var1) {
      int var2 = this.elements >> 7;
      int var3 = this.elements & 127;
      int var4 = this.objects.length;
      if (var2 >= var4) {
         ConstInfo[][] var5 = new ConstInfo[var4 + 8][];
         System.arraycopy(this.objects, 0, var5, 0, var4);
         this.objects = var5;
      }

      if (this.objects[var2] == null) {
         this.objects[var2] = new ConstInfo[128];
      }

      this.objects[var2][var3] = var1;
      ++this.elements;
   }
}
