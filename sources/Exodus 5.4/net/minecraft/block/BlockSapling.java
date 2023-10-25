/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
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
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling
extends BlockBush
implements IGrowable {
    public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE, STAGE);
    }

    public void generateTree(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        IBlockState iBlockState2;
        WorldGenAbstractTree worldGenAbstractTree = random.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int n = 0;
        int n2 = 0;
        boolean bl = false;
        switch (iBlockState.getValue(TYPE)) {
            case SPRUCE: {
                n = 0;
                block7: while (n >= -1) {
                    n2 = 0;
                    while (n2 >= -1) {
                        if (this.func_181624_a(world, blockPos, n, n2, BlockPlanks.EnumType.SPRUCE)) {
                            worldGenAbstractTree = new WorldGenMegaPineTree(false, random.nextBoolean());
                            bl = true;
                            break block7;
                        }
                        --n2;
                    }
                    --n;
                }
                if (bl) break;
                n2 = 0;
                n = 0;
                worldGenAbstractTree = new WorldGenTaiga2(true);
                break;
            }
            case BIRCH: {
                worldGenAbstractTree = new WorldGenForest(true, false);
                break;
            }
            case JUNGLE: {
                iBlockState2 = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                IBlockState iBlockState3 = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);
                n = 0;
                block9: while (n >= -1) {
                    n2 = 0;
                    while (n2 >= -1) {
                        if (this.func_181624_a(world, blockPos, n, n2, BlockPlanks.EnumType.JUNGLE)) {
                            worldGenAbstractTree = new WorldGenMegaJungle(true, 10, 20, iBlockState2, iBlockState3);
                            bl = true;
                            break block9;
                        }
                        --n2;
                    }
                    --n;
                }
                if (bl) break;
                n2 = 0;
                n = 0;
                worldGenAbstractTree = new WorldGenTrees(true, 4 + random.nextInt(7), iBlockState2, iBlockState3, false);
                break;
            }
            case ACACIA: {
                worldGenAbstractTree = new WorldGenSavannaTree(true);
                break;
            }
            case DARK_OAK: {
                n = 0;
                block11: while (n >= -1) {
                    n2 = 0;
                    while (n2 >= -1) {
                        if (this.func_181624_a(world, blockPos, n, n2, BlockPlanks.EnumType.DARK_OAK)) {
                            worldGenAbstractTree = new WorldGenCanopyTree(true);
                            bl = true;
                            break block11;
                        }
                        --n2;
                    }
                    --n;
                }
                if (bl) break;
                return;
            }
        }
        iBlockState2 = Blocks.air.getDefaultState();
        if (bl) {
            world.setBlockState(blockPos.add(n, 0, n2), iBlockState2, 4);
            world.setBlockState(blockPos.add(n + 1, 0, n2), iBlockState2, 4);
            world.setBlockState(blockPos.add(n, 0, n2 + 1), iBlockState2, 4);
            world.setBlockState(blockPos.add(n + 1, 0, n2 + 1), iBlockState2, 4);
        } else {
            world.setBlockState(blockPos, iBlockState2, 4);
        }
        if (!((WorldGenerator)worldGenAbstractTree).generate(world, random, blockPos.add(n, 0, n2))) {
            if (bl) {
                world.setBlockState(blockPos.add(n, 0, n2), iBlockState, 4);
                world.setBlockState(blockPos.add(n + 1, 0, n2), iBlockState, 4);
                world.setBlockState(blockPos.add(n, 0, n2 + 1), iBlockState, 4);
                world.setBlockState(blockPos.add(n + 1, 0, n2 + 1), iBlockState, 4);
            } else {
                world.setBlockState(blockPos, iBlockState, 4);
            }
        }
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return (double)world.rand.nextFloat() < 0.45;
    }

    private boolean func_181624_a(World world, BlockPos blockPos, int n, int n2, BlockPlanks.EnumType enumType) {
        return this.isTypeAt(world, blockPos.add(n, 0, n2), enumType) && this.isTypeAt(world, blockPos.add(n + 1, 0, n2), enumType) && this.isTypeAt(world, blockPos.add(n, 0, n2 + 1), enumType) && this.isTypeAt(world, blockPos.add(n + 1, 0, n2 + 1), enumType);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(n & 7)).withProperty(STAGE, (n & 8) >> 3);
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return true;
    }

    public boolean isTypeAt(World world, BlockPos blockPos, BlockPlanks.EnumType enumType) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        return iBlockState.getBlock() == this && iBlockState.getValue(TYPE) == enumType;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        BlockPlanks.EnumType[] enumTypeArray = BlockPlanks.EnumType.values();
        int n = enumTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            BlockPlanks.EnumType enumType = enumTypeArray[n2];
            list.add(new ItemStack(item, 1, enumType.getMetadata()));
            ++n2;
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(TYPE).getMetadata();
        return n |= iBlockState.getValue(STAGE) << 3;
    }

    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, 0));
        float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 2.0f, 0.5f + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void grow(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (iBlockState.getValue(STAGE) == 0) {
            world.setBlockState(blockPos, iBlockState.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(world, blockPos, iBlockState, random);
        }
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        this.grow(world, blockPos, iBlockState, random);
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(TYPE).getMetadata();
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote) {
            super.updateTick(world, blockPos, iBlockState, random);
            if (world.getLightFromNeighbors(blockPos.up()) >= 9 && random.nextInt(7) == 0) {
                this.grow(world, blockPos, iBlockState, random);
            }
        }
    }
}

