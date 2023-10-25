package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockPlanks extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockPlanks() {
    super(Material.wood);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.OAK));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (EnumType blockplanks$enumtype : EnumType.values())
      list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata())); 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public MapColor getMapColor(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).func_181070_c();
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    OAK(0, "oak", MapColor.woodColor),
    SPRUCE(1, "spruce", MapColor.obsidianColor),
    BIRCH(2, "birch", MapColor.sandColor),
    JUNGLE(3, "jungle", MapColor.dirtColor),
    ACACIA(4, "acacia", MapColor.adobeColor),
    DARK_OAK(5, "dark_oak", "big_oak", (String)MapColor.brownColor);
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    private final MapColor field_181071_k;
    
    static {
      for (EnumType blockplanks$enumtype : values())
        META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype; 
    }
    
    EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_) {
      this.meta = p_i46389_3_;
      this.name = p_i46389_4_;
      this.unlocalizedName = p_i46389_5_;
      this.field_181071_k = p_i46389_6_;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public MapColor func_181070_c() {
      return this.field_181071_k;
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
      return this.unlocalizedName;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockPlanks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */