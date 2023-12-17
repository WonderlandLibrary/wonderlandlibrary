// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockChorusFlower extends Block
{
    public static final PropertyInteger AGE;
    
    protected BlockChorusFlower() {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockChorusFlower.AGE, 0));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!this.canSurvive(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
        else {
            final BlockPos blockpos = pos.up();
            if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 256) {
                final int i = state.getValue((IProperty<Integer>)BlockChorusFlower.AGE);
                if (i < 5 && rand.nextInt(1) == 0) {
                    boolean flag = false;
                    boolean flag2 = false;
                    final IBlockState iblockstate = worldIn.getBlockState(pos.down());
                    final Block block = iblockstate.getBlock();
                    if (block == Blocks.END_STONE) {
                        flag = true;
                    }
                    else if (block == Blocks.CHORUS_PLANT) {
                        int j = 1;
                        int k = 0;
                        while (k < 4) {
                            final Block block2 = worldIn.getBlockState(pos.down(j + 1)).getBlock();
                            if (block2 != Blocks.CHORUS_PLANT) {
                                if (block2 == Blocks.END_STONE) {
                                    flag2 = true;
                                    break;
                                }
                                break;
                            }
                            else {
                                ++j;
                                ++k;
                            }
                        }
                        int i2 = 4;
                        if (flag2) {
                            ++i2;
                        }
                        if (j < 2 || rand.nextInt(i2) >= j) {
                            flag = true;
                        }
                    }
                    else if (iblockstate.getMaterial() == Material.AIR) {
                        flag = true;
                    }
                    if (flag && areAllNeighborsEmpty(worldIn, blockpos, null) && worldIn.isAirBlock(pos.up(2))) {
                        worldIn.setBlockState(pos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                        this.placeGrownFlower(worldIn, blockpos, i);
                    }
                    else if (i < 4) {
                        int l = rand.nextInt(4);
                        boolean flag3 = false;
                        if (flag2) {
                            ++l;
                        }
                        for (int j2 = 0; j2 < l; ++j2) {
                            final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                            final BlockPos blockpos2 = pos.offset(enumfacing);
                            if (worldIn.isAirBlock(blockpos2) && worldIn.isAirBlock(blockpos2.down()) && areAllNeighborsEmpty(worldIn, blockpos2, enumfacing.getOpposite())) {
                                this.placeGrownFlower(worldIn, blockpos2, i + 1);
                                flag3 = true;
                            }
                        }
                        if (flag3) {
                            worldIn.setBlockState(pos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                        }
                        else {
                            this.placeDeadFlower(worldIn, pos);
                        }
                    }
                    else if (i == 4) {
                        this.placeDeadFlower(worldIn, pos);
                    }
                }
            }
        }
    }
    
    private void placeGrownFlower(final World worldIn, final BlockPos pos, final int age) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty((IProperty<Comparable>)BlockChorusFlower.AGE, age), 2);
        worldIn.playEvent(1033, pos, 0);
    }
    
    private void placeDeadFlower(final World worldIn, final BlockPos pos) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty((IProperty<Comparable>)BlockChorusFlower.AGE, 5), 2);
        worldIn.playEvent(1034, pos, 0);
    }
    
    private static boolean areAllNeighborsEmpty(final World worldIn, final BlockPos pos, final EnumFacing excludingSide) {
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != excludingSide && !worldIn.isAirBlock(pos.offset(enumfacing))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canSurvive(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canSurvive(worldIn, pos)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }
    
    public boolean canSurvive(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos.down());
        final Block block = iblockstate.getBlock();
        if (block == Blocks.CHORUS_PLANT || block == Blocks.END_STONE) {
            return true;
        }
        if (iblockstate.getMaterial() == Material.AIR) {
            int i = 0;
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                final IBlockState iblockstate2 = worldIn.getBlockState(pos.offset(enumfacing));
                final Block block2 = iblockstate2.getBlock();
                if (block2 == Blocks.CHORUS_PLANT) {
                    ++i;
                }
                else {
                    if (iblockstate2.getMaterial() != Material.AIR) {
                        return false;
                    }
                    continue;
                }
            }
            return i == 1;
        }
        return false;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockChorusFlower.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockChorusFlower.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockChorusFlower.AGE });
    }
    
    public static void generatePlant(final World worldIn, final BlockPos pos, final Random rand, final int p_185603_3_) {
        worldIn.setBlockState(pos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        growTreeRecursive(worldIn, pos, rand, pos, p_185603_3_, 0);
    }
    
    private static void growTreeRecursive(final World worldIn, final BlockPos p_185601_1_, final Random rand, final BlockPos p_185601_3_, final int p_185601_4_, final int p_185601_5_) {
        int i = rand.nextInt(4) + 1;
        if (p_185601_5_ == 0) {
            ++i;
        }
        for (int j = 0; j < i; ++j) {
            final BlockPos blockpos = p_185601_1_.up(j + 1);
            if (!areAllNeighborsEmpty(worldIn, blockpos, null)) {
                return;
            }
            worldIn.setBlockState(blockpos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        }
        boolean flag = false;
        if (p_185601_5_ < 4) {
            int l = rand.nextInt(4);
            if (p_185601_5_ == 0) {
                ++l;
            }
            for (int k = 0; k < l; ++k) {
                final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                final BlockPos blockpos2 = p_185601_1_.up(i).offset(enumfacing);
                if (Math.abs(blockpos2.getX() - p_185601_3_.getX()) < p_185601_4_ && Math.abs(blockpos2.getZ() - p_185601_3_.getZ()) < p_185601_4_ && worldIn.isAirBlock(blockpos2) && worldIn.isAirBlock(blockpos2.down()) && areAllNeighborsEmpty(worldIn, blockpos2, enumfacing.getOpposite())) {
                    flag = true;
                    worldIn.setBlockState(blockpos2, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                    growTreeRecursive(worldIn, blockpos2, rand, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
                }
            }
        }
        if (!flag) {
            worldIn.setBlockState(p_185601_1_.up(i), Blocks.CHORUS_FLOWER.getDefaultState().withProperty((IProperty<Comparable>)BlockChorusFlower.AGE, 5), 2);
        }
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 5);
    }
}
