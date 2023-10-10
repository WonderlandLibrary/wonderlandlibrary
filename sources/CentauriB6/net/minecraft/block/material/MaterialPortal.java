package net.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialPortal extends Material {
   public MaterialPortal(MapColor color) {
      super(color);
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
