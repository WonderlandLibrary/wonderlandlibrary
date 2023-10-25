package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
  
  private final int field_150068_a;
  
  protected BlockPressurePlateWeighted(Material p_i46379_1_, int p_i46379_2_) {
    this(p_i46379_1_, p_i46379_2_, p_i46379_1_.getMaterialMapColor());
  }
  
  protected BlockPressurePlateWeighted(Material p_i46380_1_, int p_i46380_2_, MapColor p_i46380_3_) {
    super(p_i46380_1_, p_i46380_3_);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWER, Integer.valueOf(0)));
    this.field_150068_a = p_i46380_2_;
  }
  
  protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
    int i = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, getSensitiveAABB(pos)).size(), this.field_150068_a);
    if (i > 0) {
      float f = Math.min(this.field_150068_a, i) / this.field_150068_a;
      return MathHelper.ceiling_float_int(f * 15.0F);
    } 
    return 0;
  }
  
  protected int getRedstoneStrength(IBlockState state) {
    return ((Integer)state.getValue((IProperty)POWER)).intValue();
  }
  
  protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
    return state.withProperty((IProperty)POWER, Integer.valueOf(strength));
  }
  
  public int tickRate(World worldIn) {
    return 10;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWER, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)POWER)).intValue();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)POWER });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockPressurePlateWeighted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */