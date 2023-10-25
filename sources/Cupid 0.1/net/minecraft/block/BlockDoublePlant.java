package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockDoublePlant extends BlockBush implements IGrowable {
  public static final PropertyEnum<EnumPlantType> VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
  
  public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
  
  public static final PropertyEnum<EnumFacing> field_181084_N = (PropertyEnum<EnumFacing>)BlockDirectional.FACING;
  
  public BlockDoublePlant() {
    super(Material.vine);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumPlantType.SUNFLOWER).withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)field_181084_N, (Comparable)EnumFacing.NORTH));
    setHardness(0.0F);
    setStepSound(soundTypeGrass);
    setUnlocalizedName("doublePlant");
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public EnumPlantType getVariant(IBlockAccess worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() == this) {
      iblockstate = getActualState(iblockstate, worldIn, pos);
      return (EnumPlantType)iblockstate.getValue((IProperty)VARIANT);
    } 
    return EnumPlantType.FERN;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up()));
  }
  
  public boolean isReplaceable(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() != this)
      return true; 
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)getActualState(iblockstate, (IBlockAccess)worldIn, pos).getValue((IProperty)VARIANT);
    return (blockdoubleplant$enumplanttype == EnumPlantType.FERN || blockdoubleplant$enumplanttype == EnumPlantType.GRASS);
  }
  
  protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!canBlockStay(worldIn, pos, state)) {
      boolean flag = (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER);
      BlockPos blockpos = flag ? pos : pos.up();
      BlockPos blockpos1 = flag ? pos.down() : pos;
      Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
      Block block1 = flag ? worldIn.getBlockState(blockpos1).getBlock() : this;
      if (block == this)
        worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2); 
      if (block1 == this) {
        worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
        if (!flag)
          dropBlockAsItem(worldIn, blockpos1, state, 0); 
      } 
    } 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
      return (worldIn.getBlockState(pos.down()).getBlock() == this); 
    IBlockState iblockstate = worldIn.getBlockState(pos.up());
    return (iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate));
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
      return null; 
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
    return (blockdoubleplant$enumplanttype == EnumPlantType.FERN) ? null : ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? ((rand.nextInt(8) == 0) ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
  }
  
  public int damageDropped(IBlockState state) {
    return (state.getValue((IProperty)HALF) != EnumBlockHalf.UPPER && state.getValue((IProperty)VARIANT) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta() : 0;
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    EnumPlantType blockdoubleplant$enumplanttype = getVariant(worldIn, pos);
    return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
  }
  
  public void placeAt(World worldIn, BlockPos lowerPos, EnumPlantType variant, int flags) {
    worldIn.setBlockState(lowerPos, getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, variant), flags);
    worldIn.setBlockState(lowerPos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), flags);
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(pos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), 2);
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
    if (worldIn.isRemote || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Items.shears || state.getValue((IProperty)HALF) != EnumBlockHalf.LOWER || !onHarvest(worldIn, pos, state, player))
      super.harvestBlock(worldIn, player, pos, state, te); 
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
      if (worldIn.getBlockState(pos.down()).getBlock() == this)
        if (!player.capabilities.isCreativeMode) {
          IBlockState iblockstate = worldIn.getBlockState(pos.down());
          EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getValue((IProperty)VARIANT);
          if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
            worldIn.destroyBlock(pos.down(), true);
          } else if (!worldIn.isRemote) {
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
              onHarvest(worldIn, pos, iblockstate, player);
              worldIn.setBlockToAir(pos.down());
            } else {
              worldIn.destroyBlock(pos.down(), true);
            } 
          } else {
            worldIn.setBlockToAir(pos.down());
          } 
        } else {
          worldIn.setBlockToAir(pos.down());
        }  
    } else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this) {
      worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
    } 
    super.onBlockHarvested(worldIn, pos, state, player);
  }
  
  private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
    if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS)
      return false; 
    player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
    int i = ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
    spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 2, i));
    return true;
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (EnumPlantType blockdoubleplant$enumplanttype : EnumPlantType.values())
      list.add(new ItemStack(itemIn, 1, blockdoubleplant$enumplanttype.getMeta())); 
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    return getVariant((IBlockAccess)worldIn, pos).getMeta();
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    EnumPlantType blockdoubleplant$enumplanttype = getVariant((IBlockAccess)worldIn, pos);
    return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN);
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    spawnAsEntity(worldIn, pos, new ItemStack(this, 1, getVariant((IBlockAccess)worldIn, pos).getMeta()));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return ((meta & 0x8) > 0) ? getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER) : getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, EnumPlantType.byMetadata(meta & 0x7));
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
      IBlockState iblockstate = worldIn.getBlockState(pos.down());
      if (iblockstate.getBlock() == this)
        state = state.withProperty((IProperty)VARIANT, iblockstate.getValue((IProperty)VARIANT)); 
    } 
    return state;
  }
  
  public int getMetaFromState(IBlockState state) {
    return (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) ? (0x8 | ((EnumFacing)state.getValue((IProperty)field_181084_N)).getHorizontalIndex()) : ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT, (IProperty)field_181084_N });
  }
  
  public Block.EnumOffsetType getOffsetType() {
    return Block.EnumOffsetType.XZ;
  }
  
  public enum EnumBlockHalf implements IStringSerializable {
    UPPER, LOWER;
    
    public String toString() {
      return getName();
    }
    
    public String getName() {
      return (this == UPPER) ? "upper" : "lower";
    }
  }
  
  public enum EnumPlantType implements IStringSerializable {
    SUNFLOWER(0, "sunflower"),
    SYRINGA(1, "syringa"),
    GRASS(2, "double_grass", "grass"),
    FERN(3, "double_fern", "fern"),
    ROSE(4, "double_rose", "rose"),
    PAEONIA(5, "paeonia");
    
    private static final EnumPlantType[] META_LOOKUP = new EnumPlantType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      for (EnumPlantType blockdoubleplant$enumplanttype : values())
        META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype; 
    }
    
    EnumPlantType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMeta() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumPlantType byMetadata(int meta) {
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockDoublePlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */