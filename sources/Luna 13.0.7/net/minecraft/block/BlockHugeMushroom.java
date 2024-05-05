package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockHugeMushroom
  extends Block
{
  public static final PropertyEnum field_176380_a = PropertyEnum.create("variant", EnumType.class);
  private final Block field_176379_b;
  private static final String __OBFID = "CL_00000258";
  
  public BlockHugeMushroom(Material p_i45711_1_, Block p_i45711_2_)
  {
    super(p_i45711_1_);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176380_a, EnumType.ALL_OUTSIDE));
    this.field_176379_b = p_i45711_2_;
  }
  
  public int quantityDropped(Random random)
  {
    return Math.max(0, random.nextInt(10) - 7);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return Item.getItemFromBlock(this.field_176379_b);
  }
  
  public Item getItem(World worldIn, BlockPos pos)
  {
    return Item.getItemFromBlock(this.field_176379_b);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    return getDefaultState();
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(field_176380_a, EnumType.func_176895_a(meta));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumType)state.getValue(field_176380_a)).func_176896_a();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176380_a });
  }
  
  public static enum EnumType
    implements IStringSerializable
  {
    private static final EnumType[] field_176905_n;
    private final int field_176906_o;
    private final String field_176914_p;
    private static final EnumType[] $VALUES;
    private static final String __OBFID = "CL_00002105";
    
    private EnumType(String p_i45710_1_, int p_i45710_2_, int p_i45710_3_, String p_i45710_4_)
    {
      this.field_176906_o = p_i45710_3_;
      this.field_176914_p = p_i45710_4_;
    }
    
    public int func_176896_a()
    {
      return this.field_176906_o;
    }
    
    public String toString()
    {
      return this.field_176914_p;
    }
    
    public static EnumType func_176895_a(int p_176895_0_)
    {
      if ((p_176895_0_ < 0) || (p_176895_0_ >= field_176905_n.length)) {
        p_176895_0_ = 0;
      }
      EnumType var1 = field_176905_n[p_176895_0_];
      return var1 == null ? field_176905_n[0] : var1;
    }
    
    public String getName()
    {
      return this.field_176914_p;
    }
    
    static
    {
      field_176905_n = new EnumType[16];
      
      $VALUES = new EnumType[] { NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST, STEM, ALL_INSIDE, ALL_OUTSIDE, ALL_STEM };
      
      EnumType[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumType var3 = var0[var2];
        field_176905_n[var3.func_176896_a()] = var3;
      }
    }
  }
}
