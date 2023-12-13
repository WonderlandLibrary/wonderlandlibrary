package net.minecraft.block.material;

public class MaterialPortal extends Material {
   public boolean isSolid() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }

   public boolean blocksMovement() {
      return false;
   }

   public MaterialPortal(MapColor var1) {
      super(var1);
   }
}
