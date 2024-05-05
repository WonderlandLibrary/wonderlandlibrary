package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot
  extends BlockContainer
{
  public static final PropertyInteger field_176444_a = PropertyInteger.create("legacy_data", 0, 15);
  public static final PropertyEnum field_176443_b = PropertyEnum.create("contents", EnumFlowerType.class);
  private static final String __OBFID = "CL_00000247";
  
  public BlockFlowerPot()
  {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176443_b, EnumFlowerType.EMPTY).withProperty(field_176444_a, Integer.valueOf(0)));
    setBlockBoundsForItemRender();
  }
  
  public void setBlockBoundsForItemRender()
  {
    float var1 = 0.375F;
    float var2 = var1 / 2.0F;
    setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public int getRenderType()
  {
    return 3;
  }
  
  public boolean isFullCube()
  {
    return false;
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
  {
    TileEntity var4 = worldIn.getTileEntity(pos);
    if ((var4 instanceof TileEntityFlowerPot))
    {
      Item var5 = ((TileEntityFlowerPot)var4).getFlowerPotItem();
      if ((var5 instanceof ItemBlock)) {
        return Block.getBlockFromItem(var5).colorMultiplier(worldIn, pos, renderPass);
      }
    }
    return 16777215;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    ItemStack var9 = playerIn.inventory.getCurrentItem();
    if ((var9 != null) && ((var9.getItem() instanceof ItemBlock)))
    {
      TileEntityFlowerPot var10 = func_176442_d(worldIn, pos);
      if (var10 == null) {
        return false;
      }
      if (var10.getFlowerPotItem() != null) {
        return false;
      }
      Block var11 = Block.getBlockFromItem(var9.getItem());
      if (!func_149928_a(var11, var9.getMetadata())) {
        return false;
      }
      var10.func_145964_a(var9.getItem(), var9.getMetadata());
      var10.markDirty();
      worldIn.markBlockForUpdate(pos);
      if (!playerIn.capabilities.isCreativeMode) {
        if (--var9.stackSize <= 0) {
          playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
        }
      }
      return true;
    }
    return false;
  }
  
  private boolean func_149928_a(Block p_149928_1_, int p_149928_2_)
  {
    return (p_149928_1_ == Blocks.tallgrass) && (p_149928_2_ == BlockTallGrass.EnumType.FERN.func_177044_a());
  }
  
  public Item getItem(World worldIn, BlockPos pos)
  {
    TileEntityFlowerPot var3 = func_176442_d(worldIn, pos);
    return (var3 != null) && (var3.getFlowerPotItem() != null) ? var3.getFlowerPotItem() : Items.flower_pot;
  }
  
  public int getDamageValue(World worldIn, BlockPos pos)
  {
    TileEntityFlowerPot var3 = func_176442_d(worldIn, pos);
    return (var3 != null) && (var3.getFlowerPotItem() != null) ? var3.getFlowerPotData() : 0;
  }
  
  public boolean isFlowerPot()
  {
    return true;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    return (super.canPlaceBlockAt(worldIn, pos)) && (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()))
    {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    }
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    TileEntityFlowerPot var4 = func_176442_d(worldIn, pos);
    if ((var4 != null) && (var4.getFlowerPotItem() != null)) {
      spawnAsEntity(worldIn, pos, new ItemStack(var4.getFlowerPotItem(), 1, var4.getFlowerPotData()));
    }
    super.breakBlock(worldIn, pos, state);
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
  {
    super.onBlockHarvested(worldIn, pos, state, playerIn);
    if (playerIn.capabilities.isCreativeMode)
    {
      TileEntityFlowerPot var5 = func_176442_d(worldIn, pos);
      if (var5 != null) {
        var5.func_145964_a((Item)null, 0);
      }
    }
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return Items.flower_pot;
  }
  
  private TileEntityFlowerPot func_176442_d(World worldIn, BlockPos p_176442_2_)
  {
    TileEntity var3 = worldIn.getTileEntity(p_176442_2_);
    return (var3 instanceof TileEntityFlowerPot) ? (TileEntityFlowerPot)var3 : null;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta)
  {
    Object var3 = null;
    int var4 = 0;
    switch (meta)
    {
    case 1: 
      var3 = Blocks.red_flower;
      var4 = BlockFlower.EnumFlowerType.POPPY.func_176968_b();
      break;
    case 2: 
      var3 = Blocks.yellow_flower;
      break;
    case 3: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.OAK.func_176839_a();
      break;
    case 4: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.SPRUCE.func_176839_a();
      break;
    case 5: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.BIRCH.func_176839_a();
      break;
    case 6: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.JUNGLE.func_176839_a();
      break;
    case 7: 
      var3 = Blocks.red_mushroom;
      break;
    case 8: 
      var3 = Blocks.brown_mushroom;
      break;
    case 9: 
      var3 = Blocks.cactus;
      break;
    case 10: 
      var3 = Blocks.deadbush;
      break;
    case 11: 
      var3 = Blocks.tallgrass;
      var4 = BlockTallGrass.EnumType.FERN.func_177044_a();
      break;
    case 12: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.ACACIA.func_176839_a();
      break;
    case 13: 
      var3 = Blocks.sapling;
      var4 = BlockPlanks.EnumType.DARK_OAK.func_176839_a();
    }
    return new TileEntityFlowerPot(Item.getItemFromBlock((Block)var3), var4);
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176443_b, field_176444_a });
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((Integer)state.getValue(field_176444_a)).intValue();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    EnumFlowerType var4 = EnumFlowerType.EMPTY;
    TileEntity var5 = worldIn.getTileEntity(pos);
    if ((var5 instanceof TileEntityFlowerPot))
    {
      TileEntityFlowerPot var6 = (TileEntityFlowerPot)var5;
      Item var7 = var6.getFlowerPotItem();
      if ((var7 instanceof ItemBlock))
      {
        int var8 = var6.getFlowerPotData();
        Block var9 = Block.getBlockFromItem(var7);
        if (var9 == Blocks.sapling) {
          switch (SwitchEnumType.field_180353_a[BlockPlanks.EnumType.func_176837_a(var8).ordinal()])
          {
          case 1: 
            var4 = EnumFlowerType.OAK_SAPLING;
            break;
          case 2: 
            var4 = EnumFlowerType.SPRUCE_SAPLING;
            break;
          case 3: 
            var4 = EnumFlowerType.BIRCH_SAPLING;
            break;
          case 4: 
            var4 = EnumFlowerType.JUNGLE_SAPLING;
            break;
          case 5: 
            var4 = EnumFlowerType.ACACIA_SAPLING;
            break;
          case 6: 
            var4 = EnumFlowerType.DARK_OAK_SAPLING;
            break;
          default: 
            var4 = EnumFlowerType.EMPTY;break;
          }
        } else if (var9 == Blocks.tallgrass) {
          switch (var8)
          {
          case 0: 
            var4 = EnumFlowerType.DEAD_BUSH;
            break;
          case 2: 
            var4 = EnumFlowerType.FERN;
            break;
          default: 
            var4 = EnumFlowerType.EMPTY;break;
          }
        } else if (var9 == Blocks.yellow_flower) {
          var4 = EnumFlowerType.DANDELION;
        } else if (var9 == Blocks.red_flower) {
          switch (SwitchEnumType.field_180352_b[BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.RED, var8).ordinal()])
          {
          case 1: 
            var4 = EnumFlowerType.POPPY;
            break;
          case 2: 
            var4 = EnumFlowerType.BLUE_ORCHID;
            break;
          case 3: 
            var4 = EnumFlowerType.ALLIUM;
            break;
          case 4: 
            var4 = EnumFlowerType.HOUSTONIA;
            break;
          case 5: 
            var4 = EnumFlowerType.RED_TULIP;
            break;
          case 6: 
            var4 = EnumFlowerType.ORANGE_TULIP;
            break;
          case 7: 
            var4 = EnumFlowerType.WHITE_TULIP;
            break;
          case 8: 
            var4 = EnumFlowerType.PINK_TULIP;
            break;
          case 9: 
            var4 = EnumFlowerType.OXEYE_DAISY;
            break;
          default: 
            var4 = EnumFlowerType.EMPTY;break;
          }
        } else if (var9 == Blocks.red_mushroom) {
          var4 = EnumFlowerType.MUSHROOM_RED;
        } else if (var9 == Blocks.brown_mushroom) {
          var4 = EnumFlowerType.MUSHROOM_BROWN;
        } else if (var9 == Blocks.deadbush) {
          var4 = EnumFlowerType.DEAD_BUSH;
        } else if (var9 == Blocks.cactus) {
          var4 = EnumFlowerType.CACTUS;
        }
      }
    }
    return state.withProperty(field_176443_b, var4);
  }
  
  public EnumWorldBlockLayer getBlockLayer()
  {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public static enum EnumFlowerType
    implements IStringSerializable
  {
    private final String field_177006_w;
    private static final EnumFlowerType[] $VALUES = { EMPTY, POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, DANDELION, OAK_SAPLING, SPRUCE_SAPLING, BIRCH_SAPLING, JUNGLE_SAPLING, ACACIA_SAPLING, DARK_OAK_SAPLING, MUSHROOM_RED, MUSHROOM_BROWN, DEAD_BUSH, FERN, CACTUS };
    private static final String __OBFID = "CL_00002115";
    
    private EnumFlowerType(String p_i45715_1_, int p_i45715_2_, String p_i45715_3_)
    {
      this.field_177006_w = p_i45715_3_;
    }
    
    public String toString()
    {
      return this.field_177006_w;
    }
    
    public String getName()
    {
      return this.field_177006_w;
    }
  }
  
  static final class SwitchEnumType
  {
    static final int[] field_180353_a;
    static final int[] field_180352_b = new int[BlockFlower.EnumFlowerType.values().length];
    private static final String __OBFID = "CL_00002116";
    
    SwitchEnumType() {}
    
    static
    {
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.POPPY.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.BLUE_ORCHID.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.ALLIUM.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.HOUSTONIA.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.RED_TULIP.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.ORANGE_TULIP.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.WHITE_TULIP.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.PINK_TULIP.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        field_180352_b[BlockFlower.EnumFlowerType.OXEYE_DAISY.ordinal()] = 9;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      field_180353_a = new int[BlockPlanks.EnumType.values().length];
      try
      {
        field_180353_a[BlockPlanks.EnumType.OAK.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      try
      {
        field_180353_a[BlockPlanks.EnumType.SPRUCE.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        field_180353_a[BlockPlanks.EnumType.BIRCH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        field_180353_a[BlockPlanks.EnumType.JUNGLE.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        field_180353_a[BlockPlanks.EnumType.ACACIA.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        field_180353_a[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
    }
  }
}
