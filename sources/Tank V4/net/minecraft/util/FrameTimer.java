package net.minecraft.util;

public class FrameTimer {
   private int field_181753_b;
   private int field_181755_d;
   private final long[] field_181752_a = new long[240];
   private int field_181754_c;

   public int func_181750_b() {
      return this.field_181755_d;
   }

   public int func_181748_a(long var1, int var3) {
      double var4 = (double)var1 / 1.6666666E7D;
      return (int)(var4 * (double)var3);
   }

   public long[] func_181746_c() {
      return this.field_181752_a;
   }

   public int func_181749_a() {
      return this.field_181753_b;
   }

   public int func_181751_b(int var1) {
      return var1 % 240;
   }

   public void func_181747_a(long var1) {
      this.field_181752_a[this.field_181755_d] = var1;
      ++this.field_181755_d;
      if (this.field_181755_d == 240) {
         this.field_181755_d = 0;
      }

      if (this.field_181754_c < 240) {
         this.field_181753_b = 0;
         ++this.field_181754_c;
      } else {
         this.field_181753_b = this.func_181751_b(this.field_181755_d + 1);
      }

   }
}
