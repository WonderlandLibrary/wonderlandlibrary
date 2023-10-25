package net.minecraft.block;

import java.util.List;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
  public static final PropertyBool UP = PropertyBool.create("up");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockWall(Block modelBlock) {
    super(modelBlock.blockMaterial);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)VARIANT, EnumType.NORMAL));
    setHardness(modelBlock.blockHardness);
    setResistance(modelBlock.blockResistance / 3.0F);
    setStepSound(modelBlock.stepSound);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.NORMAL.getUnlocalizedName() + ".name");
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    boolean flag = canConnectTo(worldIn, pos.north());
    boolean flag1 = canConnectTo(worldIn, pos.south());
    boolean flag2 = canConnectTo(worldIn, pos.west());
    boolean flag3 = canConnectTo(worldIn, pos.east());
    float f = 0.25F;
    float f1 = 0.75F;
    float f2 = 0.25F;
    float f3 = 0.75F;
    float f4 = 1.0F;
    if (flag)
      f2 = 0.0F; 
    if (flag1)
      f3 = 1.0F; 
    if (flag2)
      f = 0.0F; 
    if (flag3)
      f1 = 1.0F; 
    if (flag && flag1 && !flag2 && !flag3) {
      f4 = 0.8125F;
      f = 0.3125F;
      f1 = 0.6875F;
    } else if (!flag && !flag1 && flag2 && flag3) {
      f4 = 0.8125F;
      f2 = 0.3125F;
      f3 = 0.6875F;
    } 
    setBlockBounds(f, 0.0F, f2, f1, f4, f3);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    this.maxY = 1.5D;
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }
  
  public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos).getBlock();
    return (block == Blocks.barrier) ? false : ((block != this && !(block instanceof BlockFenceGate)) ? ((block.blockMaterial.isOpaque() && block.isFullCube()) ? ((block.blockMaterial != Material.gourd)) : false) : true);
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (EnumType blockwall$enumtype : EnumType.values())
      list.add(new ItemStack(itemIn, 1, blockwall$enumtype.getMetadata())); 
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    return (side == EnumFacing.DOWN) ? super.shouldSideBeRendered(worldIn, pos, side) : true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)UP, Boolean.valueOf(!worldIn.isAirBlock(pos.up()))).withProperty((IProperty)NORTH, Boolean.valueOf(canConnectTo(worldIn, pos.north()))).withProperty((IProperty)EAST, Boolean.valueOf(canConnectTo(worldIn, pos.east()))).withProperty((IProperty)SOUTH, Boolean.valueOf(canConnectTo(worldIn, pos.south()))).withProperty((IProperty)WEST, Boolean.valueOf(canConnectTo(worldIn, pos.west())));
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)UP, (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH, (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    NORMAL(0, "cobblestone", "normal"),
    MOSSY(1, "mossy_cobblestone", "mossy");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private String unlocalizedName;
    
    static {
      for (EnumType blockwall$enumtype : values())
        META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype; 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.meta;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */