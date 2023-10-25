package net.minecraft.block;

import java.util.List;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block {
  public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.create("variant", DirtType.class);
  
  public static final PropertyBool SNOWY = PropertyBool.create("snowy");
  
  protected BlockDirt() {
    super(Material.ground);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, DirtType.DIRT).withProperty((IProperty)SNOWY, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public MapColor getMapColor(IBlockState state) {
    return ((DirtType)state.getValue((IProperty)VARIANT)).func_181066_d();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if (state.getValue((IProperty)VARIANT) == DirtType.PODZOL) {
      Block block = worldIn.getBlockState(pos.up()).getBlock();
      state = state.withProperty((IProperty)SNOWY, Boolean.valueOf((block == Blocks.snow || block == Blocks.snow_layer)));
    } 
    return state;
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
    list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
    list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return (iblockstate.getBlock() != this) ? 0 : ((DirtType)iblockstate.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, DirtType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((DirtType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)VARIANT, (IProperty)SNOWY });
  }
  
  public int damageDropped(IBlockState state) {
    DirtType blockdirt$dirttype = (DirtType)state.getValue((IProperty)VARIANT);
    if (blockdirt$dirttype == DirtType.PODZOL)
      blockdirt$dirttype = DirtType.DIRT; 
    return blockdirt$dirttype.getMetadata();
  }
  
  public enum DirtType implements IStringSerializable {
    DIRT(0, "dirt", "default", (String)MapColor.dirtColor),
    COARSE_DIRT(1, "coarse_dirt", "coarse", (String)MapColor.dirtColor),
    PODZOL(2, "podzol", MapColor.obsidianColor);
    
    private static final DirtType[] METADATA_LOOKUP = new DirtType[(values()).length];
    
    private final int metadata;
    
    private final String name;
    
    private final String unlocalizedName;
    
    private final MapColor field_181067_h;
    
    static {
      for (DirtType blockdirt$dirttype : values())
        METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype; 
    }
    
    DirtType(int p_i46397_3_, String p_i46397_4_, String p_i46397_5_, MapColor p_i46397_6_) {
      this.metadata = p_i46397_3_;
      this.name = p_i46397_4_;
      this.unlocalizedName = p_i46397_5_;
      this.field_181067_h = p_i46397_6_;
    }
    
    public int getMetadata() {
      return this.metadata;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
    
    public MapColor func_181066_d() {
      return this.field_181067_h;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static DirtType byMetadata(int metadata) {
      if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
        metadata = 0; 
      return METADATA_LOOKUP[metadata];
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockDirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */