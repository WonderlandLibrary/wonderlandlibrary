package net.minecraft.block.material;

public class MaterialLogic extends Material {
   public boolean blocksMovement() {
      return false;
   }

   public boolean isSolid() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }

   public MaterialLogic(MapColor var1) {
      super(var1);
      this.setAdventureModeExempt();
   }
}
