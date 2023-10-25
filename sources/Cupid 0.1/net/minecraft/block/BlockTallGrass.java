package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {
  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
  
  protected BlockTallGrass() {
    super(Material.vine);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, EnumType.DEAD_BUSH));
    float f = 0.4F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
  }
  
  public int getBlockColor() {
    return ColorizerGrass.getGrassColor(0.5D, 1.0D);
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
  }
  
  public boolean isReplaceable(World worldIn, BlockPos pos) {
    return true;
  }
  
  public int getRenderColor(IBlockState state) {
    if (state.getBlock() != this)
      return super.getRenderColor(state); 
    EnumType blocktallgrass$enumtype = (EnumType)state.getValue((IProperty)TYPE);
    return (blocktallgrass$enumtype == EnumType.DEAD_BUSH) ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return (rand.nextInt(8) == 0) ? Items.wheat_seeds : null;
  }
  
  public int quantityDroppedWithBonus(int fortune, Random random) {
    return 1 + random.nextInt(fortune * 2 + 1);
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
    if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
      player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
      spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, ((EnumType)state.getValue((IProperty)TYPE)).getMeta()));
    } else {
      super.harvestBlock(worldIn, player, pos, state, te);
    } 
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return iblockstate.getBlock().getMetaFromState(iblockstate);
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (int i = 1; i < 3; i++)
      list.add(new ItemStack(itemIn, 1, i)); 
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return (state.getValue((IProperty)TYPE) != EnumType.DEAD_BUSH);
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
    if (state.getValue((IProperty)TYPE) == EnumType.FERN)
      blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN; 
    if (Blocks.double_plant.canPlaceBlockAt(worldIn, pos))
      Blocks.double_plant.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2); 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)TYPE, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)TYPE)).getMeta();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)TYPE });
  }
  
  public Block.EnumOffsetType getOffsetType() {
    return Block.EnumOffsetType.XYZ;
  }
  
  public enum EnumType implements IStringSerializable {
    DEAD_BUSH(0, "dead_bush"),
    GRASS(1, "tall_grass"),
    FERN(2, "fern");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    static {
      for (EnumType blocktallgrass$enumtype : values())
        META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype; 
    }
    
    EnumType(int meta, String name) {
      this.meta = meta;
      this.name = name;
    }
    
    public int getMeta() {
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
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockTallGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */