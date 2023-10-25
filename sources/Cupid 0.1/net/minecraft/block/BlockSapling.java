package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable {
  public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
  
  public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
  
  protected BlockSapling() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.OAK).withProperty((IProperty)STAGE, Integer.valueOf(0)));
    float f = 0.4F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal(getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote) {
      super.updateTick(worldIn, pos, state, rand);
      if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
        grow(worldIn, pos, state, rand); 
    } 
  }
  
  public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (((Integer)state.getValue((IProperty)STAGE)).intValue() == 0) {
      worldIn.setBlockState(pos, state.cycleProperty((IProperty)STAGE), 4);
    } else {
      generateTree(worldIn, pos, state, rand);
    } 
  }
  
  public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    WorldGenForest worldGenForest;
    WorldGenSavannaTree worldGenSavannaTree;
    WorldGenCanopyTree worldGenCanopyTree;
    IBlockState iblockstate, iblockstate1;
    WorldGenerator worldgenerator = (rand.nextInt(10) == 0) ? (WorldGenerator)new WorldGenBigTree(true) : (WorldGenerator)new WorldGenTrees(true);
    int i = 0;
    int j = 0;
    boolean flag = false;
    switch ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)) {
      case SPRUCE:
        label68: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
              WorldGenMegaPineTree worldGenMegaPineTree = new WorldGenMegaPineTree(false, rand.nextBoolean());
              flag = true;
              break label68;
            } 
          } 
        } 
        if (!flag) {
          j = 0;
          i = 0;
          WorldGenTaiga2 worldGenTaiga2 = new WorldGenTaiga2(true);
        } 
        break;
      case BIRCH:
        worldGenForest = new WorldGenForest(true, false);
        break;
      case JUNGLE:
        iblockstate = Blocks.log.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
        iblockstate1 = Blocks.leaves.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
        label69: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
              WorldGenMegaJungle worldGenMegaJungle = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
              flag = true;
              break label69;
            } 
          } 
        } 
        if (!flag) {
          j = 0;
          i = 0;
          WorldGenTrees worldGenTrees = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
        } 
        break;
      case ACACIA:
        worldGenSavannaTree = new WorldGenSavannaTree(true);
        break;
      case DARK_OAK:
        label70: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
              worldGenCanopyTree = new WorldGenCanopyTree(true);
              flag = true;
              break label70;
            } 
          } 
        } 
        if (!flag)
          return; 
        break;
    } 
    IBlockState iblockstate2 = Blocks.air.getDefaultState();
    if (flag) {
      worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
    } else {
      worldIn.setBlockState(pos, iblockstate2, 4);
    } 
    if (!worldGenCanopyTree.generate(worldIn, rand, pos.add(i, 0, j)))
      if (flag) {
        worldIn.setBlockState(pos.add(i, 0, j), state, 4);
        worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
        worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
        worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
      } else {
        worldIn.setBlockState(pos, state, 4);
      }  
  }
  
  private boolean func_181624_a(World p_181624_1_, BlockPos p_181624_2_, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType p_181624_5_) {
    return (isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_));
  }
  
  public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return (iblockstate.getBlock() == this && iblockstate.getValue((IProperty)TYPE) == type);
  }
  
  public int damageDropped(IBlockState state) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
      list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata())); 
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return true;
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return (worldIn.rand.nextFloat() < 0.45D);
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    grow(worldIn, pos, state, rand);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.byMetadata(meta & 0x7)).withProperty((IProperty)STAGE, Integer.valueOf((meta & 0x8) >> 3));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
    i |= ((Integer)state.getValue((IProperty)STAGE)).intValue() << 3;
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)TYPE, (IProperty)STAGE });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockSapling.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */