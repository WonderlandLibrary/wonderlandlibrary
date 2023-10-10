package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.util.Comparator;

class WorldRenderer$1 implements Comparator {
   final WorldRenderer field_181660_b;
   final float[] field_181659_a;

   public int compare(Integer var1, Integer var2) {
      return Floats.compare(this.field_181659_a[var2], this.field_181659_a[var1]);
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Integer)var1, (Integer)var2);
   }

   WorldRenderer$1(WorldRenderer var1, float[] var2) {
      this.field_181660_b = var1;
      this.field_181659_a = var2;
   }
}
