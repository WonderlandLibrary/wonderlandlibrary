// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockPlanks.EnumType> TYPE;
    public static final PropertyInteger STAGE;
    protected static final AxisAlignedBB SAPLING_AABB;
    
    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockSapling.STAGE, 0));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockSapling.SAPLING_AABB;
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + "." + BlockPlanks.EnumType.OAK.getTranslationKey() + ".name");
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
    
    public void grow(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue((IProperty<Integer>)BlockSapling.STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty((IProperty<Comparable>)BlockSapling.STAGE), 4);
        }
        else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }
    
    public void generateTree(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        WorldGenerator worldgenerator = (rand.nextInt(10) == 0) ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int i = 0;
        int j = 0;
        boolean flag = false;
        switch (state.getValue(BlockSapling.TYPE)) {
            case SPRUCE: {
            Label_0163:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
                            worldgenerator = new WorldGenMegaPineTree(false, rand.nextBoolean());
                            flag = true;
                            break Label_0163;
                        }
                    }
                }
                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTaiga2(true);
                    break;
                }
                break;
            }
            case BIRCH: {
                worldgenerator = new WorldGenBirchTree(true, false);
                break;
            }
            case JUNGLE: {
                final IBlockState iblockstate = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockstate2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
            Label_0321:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
                            worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate2);
                            flag = true;
                            break Label_0321;
                        }
                    }
                }
                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate2, false);
                    break;
                }
                break;
            }
            case ACACIA: {
                worldgenerator = new WorldGenSavannaTree(true);
                break;
            }
            case DARK_OAK: {
            Label_0434:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                            worldgenerator = new WorldGenCanopyTree(true);
                            flag = true;
                            break Label_0434;
                        }
                    }
                }
                if (!flag) {
                    return;
                }
                break;
            }
        }
        final IBlockState iblockstate3 = Blocks.AIR.getDefaultState();
        if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate3, 4);
        }
        else {
            worldIn.setBlockState(pos, iblockstate3, 4);
        }
        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            if (flag) {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
    
    private boolean isTwoByTwoOfType(final World worldIn, final BlockPos pos, final int p_181624_3_, final int p_181624_4_, final BlockPlanks.EnumType type) {
        return this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type);
    }
    
    public boolean isTypeAt(final World worldIn, final BlockPos pos, final BlockPlanks.EnumType type) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this && iblockstate.getValue(BlockSapling.TYPE) == type;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSapling.TYPE).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values()) {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return worldIn.rand.nextFloat() < 0.45;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockSapling.STAGE, (meta & 0x8) >> 3);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockSapling.TYPE).getMetadata();
        i |= state.getValue((IProperty<Integer>)BlockSapling.STAGE) << 3;
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockSapling.TYPE, BlockSapling.STAGE });
    }
    
    static {
        TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
        STAGE = PropertyInteger.create("stage", 0, 1);
        SAPLING_AABB = new AxisAlignedBB(0.09999999403953552, 0.0, 0.09999999403953552, 0.8999999761581421, 0.800000011920929, 0.8999999761581421);
    }
}
