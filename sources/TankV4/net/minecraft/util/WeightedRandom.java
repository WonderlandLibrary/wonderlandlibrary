package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom {
   public static int getTotalWeight(Collection var0) {
      int var1 = 0;

      WeightedRandom.Item var2;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1 += var2.itemWeight) {
         var2 = (WeightedRandom.Item)var3.next();
      }

      return var1;
   }

   public static WeightedRandom.Item getRandomItem(Collection var0, int var1) {
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         WeightedRandom.Item var2 = (WeightedRandom.Item)var3.next();
         var1 -= var2.itemWeight;
         if (var1 < 0) {
            return var2;
         }
      }

      return null;
   }

   public static WeightedRandom.Item getRandomItem(Random var0, Collection var1) {
      return getRandomItem(var0, var1, getTotalWeight(var1));
   }

   public static WeightedRandom.Item getRandomItem(Random var0, Collection var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.nextInt(var2);
         return getRandomItem(var1, var3);
      }
   }

   public static class Item {
      protected int itemWeight;

      public Item(int var1) {
         this.itemWeight = var1;
      }
   }
}
