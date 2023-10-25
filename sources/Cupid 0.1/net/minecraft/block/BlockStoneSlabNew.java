package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class BlockStoneSlabNew extends BlockSlab {
  public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
  
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockStoneSlabNew() {
    super(Material.rock);
    IBlockState iblockstate = this.blockState.getBaseState();
    if (isDouble()) {
      iblockstate = iblockstate.withProperty((IProperty)SEAMLESS, Boolean.valueOf(false));
    } else {
      iblockstate = iblockstate.withProperty((IProperty)HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    } 
    setDefaultState(iblockstate.withProperty((IProperty)VARIANT, EnumType.RED_SANDSTONE));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal(getUnlocalizedName() + ".red_sandstone.name");
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.stone_slab2);
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Item.getItemFromBlock(Blocks.stone_slab2);
  }
  
  public String getUnlocalizedName(int meta) {
    return getUnlocalizedName() + "." + EnumType.byMetadata(meta).getUnlocalizedName();
  }
  
  public IProperty<?> getVariantProperty() {
    return (IProperty<?>)VARIANT;
  }
  
  public Object getVariant(ItemStack stack) {
    return EnumType.byMetadata(stack.getMetadata() & 0x7);
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab2))
      for (EnumType blockstoneslabnew$enumtype : EnumType.values())
        list.add(new ItemStack(itemIn, 1, blockstoneslabnew$enumtype.getMetadata()));  
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta & 0x7));
    if (isDouble()) {
      iblockstate = iblockstate.withProperty((IProperty)SEAMLESS, Boolean.valueOf(((meta & 0x8) != 0)));
    } else {
      iblockstate = iblockstate.withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    } 
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
    if (isDouble()) {
      if (((Boolean)state.getValue((IProperty)SEAMLESS)).booleanValue())
        i |= 0x8; 
    } else if (state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP) {
      i |= 0x8;
    } 
    return i;
  }
  
  protected BlockState createBlockState() {
    return isDouble() ? new BlockState(this, new IProperty[] { (IProperty)SEAMLESS, (IProperty)VARIANT }) : new BlockState(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT });
  }
  
  public MapColor getMapColor(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).func_181068_c();
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public enum EnumType implements IStringSerializable {
    RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.getMapColor());
    
    private final MapColor field_181069_e;
    
    private final String name;
    
    private final int meta;
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    static {
      for (EnumType blockstoneslabnew$enumtype : values())
        META_LOOKUP[blockstoneslabnew$enumtype.getMetadata()] = blockstoneslabnew$enumtype; 
    }
    
    EnumType(int p_i46391_3_, String p_i46391_4_, MapColor p_i46391_5_) {
      this.meta = p_i46391_3_;
      this.name = p_i46391_4_;
      this.field_181069_e = p_i46391_5_;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public MapColor func_181068_c() {
      return this.field_181069_e;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getUnlocalizedName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockStoneSlabNew.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */