package net.minecraft.world.gen.layer;

public class GenLayerFuzzyZoom extends GenLayerZoom {
   public GenLayerFuzzyZoom(long var1, GenLayer var3) {
      super(var1, var3);
   }

   protected int selectModeOrRandom(int var1, int var2, int var3, int var4) {
      return this.selectRandom(new int[]{var1, var2, var3, var4});
   }
}
