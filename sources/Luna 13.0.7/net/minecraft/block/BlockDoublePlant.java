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
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockDoublePlant
  extends BlockBush
  implements IGrowable
{
  public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumPlantType.class);
  public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", EnumBlockHalf.class);
  private static final String __OBFID = "CL_00000231";
  
  public BlockDoublePlant()
  {
    super(Material.vine);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, EnumPlantType.SUNFLOWER).withProperty(HALF_PROP, EnumBlockHalf.LOWER));
    setHardness(0.0F);
    setStepSound(soundTypeGrass);
    setUnlocalizedName("doublePlant");
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public EnumPlantType func_176490_e(IBlockAccess p_176490_1_, BlockPos p_176490_2_)
  {
    IBlockState var3 = p_176490_1_.getBlockState(p_176490_2_);
    if (var3.getBlock() == this)
    {
      var3 = getActualState(var3, p_176490_1_, p_176490_2_);
      return (EnumPlantType)var3.getValue(VARIANT_PROP);
    }
    return EnumPlantType.FERN;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    return (super.canPlaceBlockAt(worldIn, pos)) && (worldIn.isAirBlock(pos.offsetUp()));
  }
  
  public boolean isReplaceable(World worldIn, BlockPos pos)
  {
    IBlockState var3 = worldIn.getBlockState(pos);
    if (var3.getBlock() != this) {
      return true;
    }
    EnumPlantType var4 = (EnumPlantType)getActualState(var3, worldIn, pos).getValue(VARIANT_PROP);
    return (var4 == EnumPlantType.FERN) || (var4 == EnumPlantType.GRASS);
  }
  
  protected void func_176475_e(World worldIn, BlockPos p_176475_2_, IBlockState p_176475_3_)
  {
    if (!canBlockStay(worldIn, p_176475_2_, p_176475_3_))
    {
      boolean var4 = p_176475_3_.getValue(HALF_PROP) == EnumBlockHalf.UPPER;
      BlockPos var5 = var4 ? p_176475_2_ : p_176475_2_.offsetUp();
      BlockPos var6 = var4 ? p_176475_2_.offsetDown() : p_176475_2_;
      Object var7 = var4 ? this : worldIn.getBlockState(var5).getBlock();
      Object var8 = var4 ? worldIn.getBlockState(var6).getBlock() : this;
      if (var7 == this) {
        worldIn.setBlockState(var5, Blocks.air.getDefaultState(), 3);
      }
      if (var8 == this)
      {
        worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 3);
        if (!var4) {
          dropBlockAsItem(worldIn, var6, p_176475_3_, 0);
        }
      }
    }
  }
  
  public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_)
  {
    if (p_180671_3_.getValue(HALF_PROP) == EnumBlockHalf.UPPER) {
      return worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock() == this;
    }
    IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetUp());
    return (var4.getBlock() == this) && (super.canBlockStay(worldIn, p_180671_2_, var4));
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER) {
      return null;
    }
    EnumPlantType var4 = (EnumPlantType)state.getValue(VARIANT_PROP);
    return var4 == EnumPlantType.GRASS ? null : rand.nextInt(8) == 0 ? Items.wheat_seeds : var4 == EnumPlantType.FERN ? null : Item.getItemFromBlock(this);
  }
  
  public int damageDropped(IBlockState state)
  {
    return (state.getValue(HALF_PROP) != EnumBlockHalf.UPPER) && (state.getValue(VARIANT_PROP) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue(VARIANT_PROP)).func_176936_a() : 0;
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
  {
    EnumPlantType var4 = func_176490_e(worldIn, pos);
    return (var4 != EnumPlantType.GRASS) && (var4 != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.func_180286_a(worldIn, pos);
  }
  
  public void func_176491_a(World worldIn, BlockPos p_176491_2_, EnumPlantType p_176491_3_, int p_176491_4_)
  {
    worldIn.setBlockState(p_176491_2_, getDefaultState().withProperty(HALF_PROP, EnumBlockHalf.LOWER).withProperty(VARIANT_PROP, p_176491_3_), p_176491_4_);
    worldIn.setBlockState(p_176491_2_.offsetUp(), getDefaultState().withProperty(HALF_PROP, EnumBlockHalf.UPPER), p_176491_4_);
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  {
    worldIn.setBlockState(pos.offsetUp(), getDefaultState().withProperty(HALF_PROP, EnumBlockHalf.UPPER), 2);
  }
  
  public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te)
  {
    if ((worldIn.isRemote) || (playerIn.getCurrentEquippedItem() == null) || (playerIn.getCurrentEquippedItem().getItem() != Items.shears) || (state.getValue(HALF_PROP) != EnumBlockHalf.LOWER) || (!func_176489_b(worldIn, pos, state, playerIn))) {
      super.harvestBlock(worldIn, playerIn, pos, state, te);
    }
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
  {
    if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER)
    {
      if (worldIn.getBlockState(pos.offsetDown()).getBlock() == this) {
        if (!playerIn.capabilities.isCreativeMode)
        {
          IBlockState var5 = worldIn.getBlockState(pos.offsetDown());
          EnumPlantType var6 = (EnumPlantType)var5.getValue(VARIANT_PROP);
          if ((var6 != EnumPlantType.FERN) && (var6 != EnumPlantType.GRASS)) {
            worldIn.destroyBlock(pos.offsetDown(), true);
          } else if (!worldIn.isRemote)
          {
            if ((playerIn.getCurrentEquippedItem() != null) && (playerIn.getCurrentEquippedItem().getItem() == Items.shears))
            {
              func_176489_b(worldIn, pos, var5, playerIn);
              worldIn.setBlockToAir(pos.offsetDown());
            }
            else
            {
              worldIn.destroyBlock(pos.offsetDown(), true);
            }
          }
          else {
            worldIn.setBlockToAir(pos.offsetDown());
          }
        }
        else
        {
          worldIn.setBlockToAir(pos.offsetDown());
        }
      }
    }
    else if ((playerIn.capabilities.isCreativeMode) && (worldIn.getBlockState(pos.offsetUp()).getBlock() == this)) {
      worldIn.setBlockState(pos.offsetUp(), Blocks.air.getDefaultState(), 2);
    }
    super.onBlockHarvested(worldIn, pos, state, playerIn);
  }
  
  private boolean func_176489_b(World worldIn, BlockPos p_176489_2_, IBlockState p_176489_3_, EntityPlayer p_176489_4_)
  {
    EnumPlantType var5 = (EnumPlantType)p_176489_3_.getValue(VARIANT_PROP);
    if ((var5 != EnumPlantType.FERN) && (var5 != EnumPlantType.GRASS)) {
      return false;
    }
    p_176489_4_.triggerAchievement(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
    int var6 = (var5 == EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();
    spawnAsEntity(worldIn, p_176489_2_, new ItemStack(Blocks.tallgrass, 2, var6));
    return true;
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    EnumPlantType[] var4 = EnumPlantType.values();
    int var5 = var4.length;
    for (int var6 = 0; var6 < var5; var6++)
    {
      EnumPlantType var7 = var4[var6];
      list.add(new ItemStack(itemIn, 1, var7.func_176936_a()));
    }
  }
  
  public int getDamageValue(World worldIn, BlockPos pos)
  {
    return func_176490_e(worldIn, pos).func_176936_a();
  }
  
  public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_)
  {
    EnumPlantType var5 = func_176490_e(worldIn, p_176473_2_);
    return (var5 != EnumPlantType.GRASS) && (var5 != EnumPlantType.FERN);
  }
  
  public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_)
  {
    return true;
  }
  
  public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
  {
    spawnAsEntity(worldIn, p_176474_3_, new ItemStack(this, 1, func_176490_e(worldIn, p_176474_3_).func_176936_a()));
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return (meta & 0x8) > 0 ? getDefaultState().withProperty(HALF_PROP, EnumBlockHalf.UPPER) : getDefaultState().withProperty(HALF_PROP, EnumBlockHalf.LOWER).withProperty(VARIANT_PROP, EnumPlantType.func_176938_a(meta & 0x7));
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER)
    {
      IBlockState var4 = worldIn.getBlockState(pos.offsetDown());
      if (var4.getBlock() == this) {
        state = state.withProperty(VARIANT_PROP, var4.getValue(VARIANT_PROP));
      }
    }
    return state;
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return state.getValue(HALF_PROP) == EnumBlockHalf.UPPER ? 8 : ((EnumPlantType)state.getValue(VARIANT_PROP)).func_176936_a();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { HALF_PROP, VARIANT_PROP });
  }
  
  public Block.EnumOffsetType getOffsetType()
  {
    return Block.EnumOffsetType.XZ;
  }
  
  static enum EnumBlockHalf
    implements IStringSerializable
  {
    private static final EnumBlockHalf[] $VALUES = { UPPER, LOWER };
    private static final String __OBFID = "CL_00002122";
    
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return this == UPPER ? "upper" : "lower";
    }
  }
  
  public static enum EnumPlantType
    implements IStringSerializable
  {
    private static final EnumPlantType[] field_176941_g;
    private final int field_176949_h;
    private final String field_176950_i;
    private final String field_176947_j;
    private static final EnumPlantType[] $VALUES;
    private static final String __OBFID = "CL_00002121";
    
    private EnumPlantType(String p_i45722_1_, int p_i45722_2_, int p_i45722_3_, String p_i45722_4_)
    {
      this(p_i45722_1_, p_i45722_2_, p_i45722_3_, p_i45722_4_, p_i45722_4_);
    }
    
    private EnumPlantType(String p_i45723_1_, int p_i45723_2_, int p_i45723_3_, String p_i45723_4_, String p_i45723_5_)
    {
      this.field_176949_h = p_i45723_3_;
      this.field_176950_i = p_i45723_4_;
      this.field_176947_j = p_i45723_5_;
    }
    
    public int func_176936_a()
    {
      return this.field_176949_h;
    }
    
    public String toString()
    {
      return this.field_176950_i;
    }
    
    public static EnumPlantType func_176938_a(int p_176938_0_)
    {
      if ((p_176938_0_ < 0) || (p_176938_0_ >= field_176941_g.length)) {
        p_176938_0_ = 0;
      }
      return field_176941_g[p_176938_0_];
    }
    
    public String getName()
    {
      return this.field_176950_i;
    }
    
    public String func_176939_c()
    {
      return this.field_176947_j;
    }
    
    static
    {
      field_176941_g = new EnumPlantType[values().length];
      
      $VALUES = new EnumPlantType[] { SUNFLOWER, SYRINGA, GRASS, FERN, ROSE, PAEONIA };
      
      EnumPlantType[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumPlantType var3 = var0[var2];
        field_176941_g[var3.func_176936_a()] = var3;
      }
    }
  }
}
