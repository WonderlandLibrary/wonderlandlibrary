package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower
  extends BlockBush
{
  protected PropertyEnum field_176496_a;
  private static final String __OBFID = "CL_00000246";
  
  protected BlockFlower()
  {
    super(Material.plants);
    setDefaultState(this.blockState.getBaseState().withProperty(func_176494_l(), func_176495_j() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((EnumFlowerType)state.getValue(func_176494_l())).func_176968_b();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    EnumFlowerType[] var4 = EnumFlowerType.func_176966_a(func_176495_j());
    int var5 = var4.length;
    for (int var6 = 0; var6 < var5; var6++)
    {
      EnumFlowerType var7 = var4[var6];
      list.add(new ItemStack(itemIn, 1, var7.func_176968_b()));
    }
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(func_176494_l(), EnumFlowerType.func_176967_a(func_176495_j(), meta));
  }
  
  public abstract EnumFlowerColor func_176495_j();
  
  public IProperty func_176494_l()
  {
    if (this.field_176496_a == null) {
      this.field_176496_a = PropertyEnum.create("type", EnumFlowerType.class, new Predicate()
      {
        private static final String __OBFID = "CL_00002120";
        
        public boolean func_180354_a(BlockFlower.EnumFlowerType p_180354_1_)
        {
          return p_180354_1_.func_176964_a() == BlockFlower.this.func_176495_j();
        }
        
        public boolean apply(Object p_apply_1_)
        {
          return func_180354_a((BlockFlower.EnumFlowerType)p_apply_1_);
        }
      });
    }
    return this.field_176496_a;
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumFlowerType)state.getValue(func_176494_l())).func_176968_b();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { func_176494_l() });
  }
  
  public Block.EnumOffsetType getOffsetType()
  {
    return Block.EnumOffsetType.XZ;
  }
  
  public static enum EnumFlowerColor
  {
    private static final EnumFlowerColor[] $VALUES = { YELLOW, RED };
    private static final String __OBFID = "CL_00002117";
    
    public BlockFlower func_180346_a()
    {
      return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
    }
  }
  
  public static enum EnumFlowerType
    implements IStringSerializable
  {
    private static final EnumFlowerType[][] field_176981_k;
    private final BlockFlower.EnumFlowerColor field_176978_l;
    private final int field_176979_m;
    private final String field_176976_n;
    private final String field_176977_o;
    private static final EnumFlowerType[] $VALUES;
    private static final String __OBFID = "CL_00002119";
    
    private EnumFlowerType(String p_i45718_1_, int p_i45718_2_, BlockFlower.EnumFlowerColor p_i45718_3_, int p_i45718_4_, String p_i45718_5_)
    {
      this(p_i45718_1_, p_i45718_2_, p_i45718_3_, p_i45718_4_, p_i45718_5_, p_i45718_5_);
    }
    
    private EnumFlowerType(String p_i45719_1_, int p_i45719_2_, BlockFlower.EnumFlowerColor p_i45719_3_, int p_i45719_4_, String p_i45719_5_, String p_i45719_6_)
    {
      this.field_176978_l = p_i45719_3_;
      this.field_176979_m = p_i45719_4_;
      this.field_176976_n = p_i45719_5_;
      this.field_176977_o = p_i45719_6_;
    }
    
    public BlockFlower.EnumFlowerColor func_176964_a()
    {
      return this.field_176978_l;
    }
    
    public int func_176968_b()
    {
      return this.field_176979_m;
    }
    
    public static EnumFlowerType func_176967_a(BlockFlower.EnumFlowerColor p_176967_0_, int p_176967_1_)
    {
      EnumFlowerType[] var2 = field_176981_k[p_176967_0_.ordinal()];
      if ((p_176967_1_ < 0) || (p_176967_1_ >= var2.length)) {
        p_176967_1_ = 0;
      }
      return var2[p_176967_1_];
    }
    
    public static EnumFlowerType[] func_176966_a(BlockFlower.EnumFlowerColor p_176966_0_)
    {
      return field_176981_k[p_176966_0_.ordinal()];
    }
    
    public String toString()
    {
      return this.field_176976_n;
    }
    
    public String getName()
    {
      return this.field_176976_n;
    }
    
    public String func_176963_d()
    {
      return this.field_176977_o;
    }
    
    static
    {
      field_176981_k = new EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
      
      $VALUES = new EnumFlowerType[] { DANDELION, POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY };
      
      BlockFlower.EnumFlowerColor[] var0 = BlockFlower.EnumFlowerColor.values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        BlockFlower.EnumFlowerColor var3 = var0[var2];
        Collection var4 = Collections2.filter(Lists.newArrayList(values()), new Predicate()
        {
          private static final String __OBFID = "CL_00002118";
          
          public boolean func_180350_a(BlockFlower.EnumFlowerType p_180350_1_)
          {
            return p_180350_1_.func_176964_a() == this.val$var3;
          }
          
          public boolean apply(Object p_apply_1_)
          {
            return func_180350_a((BlockFlower.EnumFlowerType)p_apply_1_);
          }
        });
        field_176981_k[var3.ordinal()] = ((EnumFlowerType[])(EnumFlowerType[])var4.toArray(new EnumFlowerType[var4.size()]));
      }
    }
  }
}
