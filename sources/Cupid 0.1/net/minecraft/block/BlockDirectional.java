package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockDirectional extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  protected BlockDirectional(Material materialIn) {
    super(materialIn);
  }
  
  protected BlockDirectional(Material p_i46398_1_, MapColor p_i46398_2_) {
    super(p_i46398_1_, p_i46398_2_);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockDirectional.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */