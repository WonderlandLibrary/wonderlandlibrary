package net.minecraft.world.border;

public enum EnumBorderStatus {
   SHRINKING(16724016);

   private static final EnumBorderStatus[] ENUM$VALUES = new EnumBorderStatus[]{GROWING, SHRINKING, STATIONARY};
   private final int id;
   STATIONARY(2138367),
   GROWING(4259712);

   private EnumBorderStatus(int var3) {
      this.id = var3;
   }

   public int getID() {
      return this.id;
   }
}
