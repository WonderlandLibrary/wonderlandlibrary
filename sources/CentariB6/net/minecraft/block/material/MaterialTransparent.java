package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialTransparent extends Material {
   public MaterialTransparent(MapColor color) {
      super(color);
      this.setReplaceable();
   }

   public boolean blocksMovement() {
      return false;
   }

   public boolean isSolid() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }
}
