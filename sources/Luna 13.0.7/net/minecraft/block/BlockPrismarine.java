package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockPrismarine
  extends Block
{
  public static final PropertyEnum VARIANTS = PropertyEnum.create("variant", EnumType.class);
  public static final int ROUGHMETA = EnumType.ROUGH.getMetadata();
  public static final int BRICKSMETA = EnumType.BRICKS.getMetadata();
  public static final int DARKMETA = EnumType.DARK.getMetadata();
  private static final String __OBFID = "CL_00002077";
  
  public BlockPrismarine()
  {
    super(Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANTS, EnumType.ROUGH));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((EnumType)state.getValue(VARIANTS)).getMetadata();
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumType)state.getValue(VARIANTS)).getMetadata();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { VARIANTS });
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(VARIANTS, EnumType.func_176810_a(meta));
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    list.add(new ItemStack(itemIn, 1, ROUGHMETA));
    list.add(new ItemStack(itemIn, 1, BRICKSMETA));
    list.add(new ItemStack(itemIn, 1, DARKMETA));
  }
  
  public static enum EnumType
    implements IStringSerializable
  {
    private static final EnumType[] field_176813_d;
    private final int meta;
    private final String field_176811_f;
    private final String field_176812_g;
    private static final EnumType[] $VALUES;
    private static final String __OBFID = "CL_00002076";
    
    private EnumType(String p_i45692_1_, int p_i45692_2_, int p_i45692_3_, String p_i45692_4_, String p_i45692_5_)
    {
      this.meta = p_i45692_3_;
      this.field_176811_f = p_i45692_4_;
      this.field_176812_g = p_i45692_5_;
    }
    
    public int getMetadata()
    {
      return this.meta;
    }
    
    public String toString()
    {
      return this.field_176811_f;
    }
    
    public static EnumType func_176810_a(int p_176810_0_)
    {
      if ((p_176810_0_ < 0) || (p_176810_0_ >= field_176813_d.length)) {
        p_176810_0_ = 0;
      }
      return field_176813_d[p_176810_0_];
    }
    
    public String getName()
    {
      return this.field_176811_f;
    }
    
    public String func_176809_c()
    {
      return this.field_176812_g;
    }
    
    static
    {
      field_176813_d = new EnumType[values().length];
      
      $VALUES = new EnumType[] { ROUGH, BRICKS, DARK };
      
      EnumType[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumType var3 = var0[var2];
        field_176813_d[var3.getMetadata()] = var3;
      }
    }
  }
}
