package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot extends BlockContainer {
  public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
  
  public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
  
  public BlockFlowerPot() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)CONTENTS, EnumFlowerType.EMPTY).withProperty((IProperty)LEGACY_DATA, Integer.valueOf(0)));
    setBlockBoundsForItemRender();
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal("item.flowerPot.name");
  }
  
  public void setBlockBoundsForItemRender() {
    float f = 0.375F;
    float f1 = f / 2.0F;
    setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public int getRenderType() {
    return 3;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityFlowerPot) {
      Item item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem();
      if (item instanceof net.minecraft.item.ItemBlock)
        return Block.getBlockFromItem(item).colorMultiplier(worldIn, pos, renderPass); 
    } 
    return 16777215;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = playerIn.inventory.getCurrentItem();
    if (itemstack != null && itemstack.getItem() instanceof net.minecraft.item.ItemBlock) {
      TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
      if (tileentityflowerpot == null)
        return false; 
      if (tileentityflowerpot.getFlowerPotItem() != null)
        return false; 
      Block block = Block.getBlockFromItem(itemstack.getItem());
      if (!canNotContain(block, itemstack.getMetadata()))
        return false; 
      tileentityflowerpot.setFlowerPotData(itemstack.getItem(), itemstack.getMetadata());
      tileentityflowerpot.markDirty();
      worldIn.markBlockForUpdate(pos);
      playerIn.triggerAchievement(StatList.field_181736_T);
      if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null); 
      return true;
    } 
    return false;
  }
  
  private boolean canNotContain(Block blockIn, int meta) {
    return (blockIn != Blocks.yellow_flower && blockIn != Blocks.red_flower && blockIn != Blocks.cactus && blockIn != Blocks.brown_mushroom && blockIn != Blocks.red_mushroom && blockIn != Blocks.sapling && blockIn != Blocks.deadbush) ? ((blockIn == Blocks.tallgrass && meta == BlockTallGrass.EnumType.FERN.getMeta())) : true;
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    return (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) ? tileentityflowerpot.getFlowerPotItem() : Items.flower_pot;
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    return (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) ? tileentityflowerpot.getFlowerPotData() : 0;
  }
  
  public boolean isFlowerPot() {
    return true;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null)
      spawnAsEntity(worldIn, pos, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData())); 
    super.breakBlock(worldIn, pos, state);
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    super.onBlockHarvested(worldIn, pos, state, player);
    if (player.capabilities.isCreativeMode) {
      TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
      if (tileentityflowerpot != null)
        tileentityflowerpot.setFlowerPotData((Item)null, 0); 
    } 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.flower_pot;
  }
  
  private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return (tileentity instanceof TileEntityFlowerPot) ? (TileEntityFlowerPot)tileentity : null;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    Block block = null;
    int i = 0;
    switch (meta) {
      case 1:
        block = Blocks.red_flower;
        i = BlockFlower.EnumFlowerType.POPPY.getMeta();
        break;
      case 2:
        block = Blocks.yellow_flower;
        break;
      case 3:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.OAK.getMetadata();
        break;
      case 4:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.SPRUCE.getMetadata();
        break;
      case 5:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.BIRCH.getMetadata();
        break;
      case 6:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.JUNGLE.getMetadata();
        break;
      case 7:
        block = Blocks.red_mushroom;
        break;
      case 8:
        block = Blocks.brown_mushroom;
        break;
      case 9:
        block = Blocks.cactus;
        break;
      case 10:
        block = Blocks.deadbush;
        break;
      case 11:
        block = Blocks.tallgrass;
        i = BlockTallGrass.EnumType.FERN.getMeta();
        break;
      case 12:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.ACACIA.getMetadata();
        break;
      case 13:
        block = Blocks.sapling;
        i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
        break;
    } 
    return (TileEntity)new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)CONTENTS, (IProperty)LEGACY_DATA });
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)LEGACY_DATA)).intValue();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityFlowerPot) {
      TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
      Item item = tileentityflowerpot.getFlowerPotItem();
      if (item instanceof net.minecraft.item.ItemBlock) {
        int i = tileentityflowerpot.getFlowerPotData();
        Block block = Block.getBlockFromItem(item);
        if (block == Blocks.sapling) {
          switch (BlockPlanks.EnumType.byMetadata(i)) {
            case POPPY:
              blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case BLUE_ORCHID:
              blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case ALLIUM:
              blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case HOUSTONIA:
              blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case RED_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case ORANGE_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.tallgrass) {
          switch (i) {
            case 0:
              blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case 2:
              blockflowerpot$enumflowertype = EnumFlowerType.FERN;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.yellow_flower) {
          blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
        } else if (block == Blocks.red_flower) {
          switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) {
            case POPPY:
              blockflowerpot$enumflowertype = EnumFlowerType.POPPY;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case BLUE_ORCHID:
              blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case ALLIUM:
              blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case HOUSTONIA:
              blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case RED_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case ORANGE_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case WHITE_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case PINK_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case OXEYE_DAISY:
              blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.red_mushroom) {
          blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
        } else if (block == Blocks.brown_mushroom) {
          blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
        } else if (block == Blocks.deadbush) {
          blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
        } else if (block == Blocks.cactus) {
          blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
        } 
      } 
    } 
    return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public enum EnumFlowerType implements IStringSerializable {
    EMPTY("empty"),
    POPPY("rose"),
    BLUE_ORCHID("blue_orchid"),
    ALLIUM("allium"),
    HOUSTONIA("houstonia"),
    RED_TULIP("red_tulip"),
    ORANGE_TULIP("orange_tulip"),
    WHITE_TULIP("white_tulip"),
    PINK_TULIP("pink_tulip"),
    OXEYE_DAISY("oxeye_daisy"),
    DANDELION("dandelion"),
    OAK_SAPLING("oak_sapling"),
    SPRUCE_SAPLING("spruce_sapling"),
    BIRCH_SAPLING("birch_sapling"),
    JUNGLE_SAPLING("jungle_sapling"),
    ACACIA_SAPLING("acacia_sapling"),
    DARK_OAK_SAPLING("dark_oak_sapling"),
    MUSHROOM_RED("mushroom_red"),
    MUSHROOM_BROWN("mushroom_brown"),
    DEAD_BUSH("dead_bush"),
    FERN("fern"),
    CACTUS("cactus");
    
    private final String name;
    
    EnumFlowerType(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockFlowerPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */