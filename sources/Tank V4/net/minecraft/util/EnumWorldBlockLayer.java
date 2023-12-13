package net.minecraft.util;

public enum EnumWorldBlockLayer {
   CUTOUT("Cutout"),
   CUTOUT_MIPPED("Mipped Cutout");

   private static final EnumWorldBlockLayer[] ENUM$VALUES = new EnumWorldBlockLayer[]{SOLID, CUTOUT_MIPPED, CUTOUT, TRANSLUCENT};
   SOLID("Solid"),
   TRANSLUCENT("Translucent");

   private final String layerName;

   private EnumWorldBlockLayer(String var3) {
      this.layerName = var3;
   }

   public String toString() {
      return this.layerName;
   }
}
