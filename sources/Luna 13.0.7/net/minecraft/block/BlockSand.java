package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockSand
  extends BlockFalling
{
  public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
  private static final String __OBFID = "CL_00000303";
  
  public BlockSand()
  {
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, EnumType.SAND));
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((EnumType)state.getValue(VARIANT_PROP)).func_176688_a();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    EnumType[] var4 = EnumType.values();
    int var5 = var4.length;
    for (int var6 = 0; var6 < var5; var6++)
    {
      EnumType var7 = var4[var6];
      list.add(new ItemStack(itemIn, 1, var7.func_176688_a()));
    }
  }
  
  public MapColor getMapColor(IBlockState state)
  {
    return ((EnumType)state.getValue(VARIANT_PROP)).func_176687_c();
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(VARIANT_PROP, EnumType.func_176686_a(meta));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumType)state.getValue(VARIANT_PROP)).func_176688_a();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { VARIANT_PROP });
  }
  
  public static enum EnumType
    implements IStringSerializable
  {
    private static final EnumType[] field_176695_c;
    private final int field_176692_d;
    private final String field_176693_e;
    private final MapColor field_176690_f;
    private final String field_176691_g;
    private static final EnumType[] $VALUES;
    private static final String __OBFID = "CL_00002069";
    
    private EnumType(String p_i45687_1_, int p_i45687_2_, int p_i45687_3_, String p_i45687_4_, String p_i45687_5_, MapColor p_i45687_6_)
    {
      this.field_176692_d = p_i45687_3_;
      this.field_176693_e = p_i45687_4_;
      this.field_176690_f = p_i45687_6_;
      this.field_176691_g = p_i45687_5_;
    }
    
    public int func_176688_a()
    {
      return this.field_176692_d;
    }
    
    public String toString()
    {
      return this.field_176693_e;
    }
    
    public MapColor func_176687_c()
    {
      return this.field_176690_f;
    }
    
    public static EnumType func_176686_a(int p_176686_0_)
    {
      if ((p_176686_0_ < 0) || (p_176686_0_ >= field_176695_c.length)) {
        p_176686_0_ = 0;
      }
      return field_176695_c[p_176686_0_];
    }
    
    public String getName()
    {
      return this.field_176693_e;
    }
    
    public String func_176685_d()
    {
      return this.field_176691_g;
    }
    
    static
    {
      field_176695_c = new EnumType[values().length];
      
      $VALUES = new EnumType[] { SAND, RED_SAND };
      
      EnumType[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumType var3 = var0[var2];
        field_176695_c[var3.func_176688_a()] = var3;
      }
    }
  }
}
