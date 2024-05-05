package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
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

public class BlockSapling
extends BlockBush
implements IGrowable {
    public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create((String)"type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create((String)"stage", (int)0, (int)1);

    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, (Comparable)BlockPlanks.EnumType.OAK).withProperty((IProperty)STAGE, (Comparable)Integer.valueOf((int)0)));
        float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 2.0f, 0.5f + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)(this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name"));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if ((Integer)state.getValue((IProperty)STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty((IProperty)STAGE), 4);
        } else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        WorldGenBigTree worldgenerator = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int i = 0;
        int j = 0;
        boolean flag = false;
        switch (1.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[((BlockPlanks.EnumType)state.getValue(TYPE)).ordinal()]) {
            case 1: {
                block7: for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (!this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) continue;
                        worldgenerator = new WorldGenMegaPineTree(false, rand.nextBoolean());
                        flag = true;
                        break block7;
                    }
                }
                if (flag) break;
                j = 0;
                i = 0;
                worldgenerator = new WorldGenTaiga2(true);
                break;
            }
            case 2: {
                worldgenerator = new WorldGenForest(true, false);
                break;
            }
            case 3: {
                IBlockState iblockstate = Blocks.log.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, (Comparable)BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.leaves.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, (Comparable)BlockPlanks.EnumType.JUNGLE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)false));
                block9: for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (!this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) continue;
                        worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
                        flag = true;
                        break block9;
                    }
                }
                if (flag) break;
                j = 0;
                i = 0;
                worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
                break;
            }
            case 4: {
                worldgenerator = new WorldGenSavannaTree(true);
                break;
            }
            case 5: {
                block11: for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (!this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) continue;
                        worldgenerator = new WorldGenCanopyTree(true);
                        flag = true;
                        break block11;
                    }
                }
                if (flag) break;
                return;
            }
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
        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            if (flag) {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            } else {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }

    private boolean func_181624_a(World p_181624_1_, BlockPos p_181624_2_, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType p_181624_5_) {
        return this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_);
    }

    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this && iblockstate.getValue(TYPE) == type;
    }

    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values()) {
            list.add((Object)new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
        }
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return (double)worldIn.rand.nextFloat() < 0.45;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, (Comparable)BlockPlanks.EnumType.byMetadata((int)(meta & 7))).withProperty((IProperty)STAGE, (Comparable)Integer.valueOf((int)((meta & 8) >> 3)));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
        return i |= (Integer)state.getValue((IProperty)STAGE) << 3;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{TYPE, STAGE});
    }
}
