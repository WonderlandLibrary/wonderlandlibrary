package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate
  extends BlockBasePressurePlate
{
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  private final Sensitivity sensitivity;
  private static final String __OBFID = "CL_00000289";
  
  protected BlockPressurePlate(Material p_i45693_1_, Sensitivity p_i45693_2_)
  {
    super(p_i45693_1_);
    setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
    this.sensitivity = p_i45693_2_;
  }
  
  protected int getRedstoneStrength(IBlockState p_176576_1_)
  {
    return ((Boolean)p_176576_1_.getValue(POWERED)).booleanValue() ? 15 : 0;
  }
  
  protected IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_)
  {
    return p_176575_1_.withProperty(POWERED, Boolean.valueOf(p_176575_2_ > 0));
  }
  
  protected int computeRedstoneStrength(World worldIn, BlockPos pos)
  {
    AxisAlignedBB var3 = getSensitiveAABB(pos);
    List var4;
    List var4;
    switch (SwitchSensitivity.SENSITIVITY_ARRAY[this.sensitivity.ordinal()])
    {
    case 1: 
      var4 = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, var3);
      break;
    case 2: 
      var4 = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, var3);
      break;
    default: 
      return 0;
    }
    List var4;
    if (!var4.isEmpty())
    {
      Iterator var5 = var4.iterator();
      while (var5.hasNext())
      {
        Entity var6 = (Entity)var5.next();
        if (!var6.doesEntityNotTriggerPressurePlate()) {
          return 15;
        }
      }
    }
    return 0;
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((Boolean)state.getValue(POWERED)).booleanValue() ? 1 : 0;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { POWERED });
  }
  
  public static enum Sensitivity
  {
    private static final Sensitivity[] $VALUES = { EVERYTHING, MOBS };
    private static final String __OBFID = "CL_00000290";
  }
  
  static final class SwitchSensitivity
  {
    static final int[] SENSITIVITY_ARRAY = new int[BlockPressurePlate.Sensitivity.values().length];
    private static final String __OBFID = "CL_00002078";
    
    SwitchSensitivity() {}
    
    static
    {
      try
      {
        SENSITIVITY_ARRAY[BlockPressurePlate.Sensitivity.EVERYTHING.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        SENSITIVITY_ARRAY[BlockPressurePlate.Sensitivity.MOBS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
    }
  }
}
