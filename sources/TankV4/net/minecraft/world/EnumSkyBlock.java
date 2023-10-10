package net.minecraft.world;

public enum EnumSkyBlock {
   SKY(15),
   BLOCK(0);

   private static final EnumSkyBlock[] ENUM$VALUES = new EnumSkyBlock[]{SKY, BLOCK};
   public final int defaultLightValue;

   private EnumSkyBlock(int var3) {
      this.defaultLightValue = var3;
   }
}
