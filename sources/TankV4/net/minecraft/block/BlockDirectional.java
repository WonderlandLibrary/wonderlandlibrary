package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockDirectional extends Block {
   public static final PropertyDirection FACING;

   protected BlockDirectional(Material var1) {
      super(var1);
   }

   protected BlockDirectional(Material var1, MapColor var2) {
      super(var1, var2);
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }
}
