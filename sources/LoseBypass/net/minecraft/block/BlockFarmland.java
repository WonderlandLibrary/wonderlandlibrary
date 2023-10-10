/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland
extends Block {
    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);

    protected BlockFarmland() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, 0));
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = state.getValue(MOISTURE);
        if (!this.hasWater(worldIn, pos) && !worldIn.canLightningStrike(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.withProperty(MOISTURE, i - 1), 2);
                return;
            }
            if (this.hasCrops(worldIn, pos)) return;
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            return;
        }
        if (i >= 7) return;
        worldIn.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!(entityIn instanceof EntityLivingBase)) return;
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f) {
            if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getBoolean("mobGriefing")) {
                return;
            }
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    private boolean hasCrops(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockCrops) return true;
        if (block instanceof BlockStem) return true;
        return false;
    }

    private boolean hasWater(World worldIn, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos;
        Iterator<BlockPos.MutableBlockPos> iterator = BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while (worldIn.getBlockState(blockpos$mutableblockpos = iterator.next()).getBlock().getMaterial() != Material.water);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (!worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid()) return;
        worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[side.ordinal()]) {
            case 1: {
                return true;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                Block block = worldIn.getBlockState(pos).getBlock();
                if (block.isOpaqueCube()) return false;
                if (block == Blocks.farmland) return false;
                return true;
            }
        }
        return super.shouldSideBeRendered(worldIn, pos, side);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MOISTURE, meta & 7);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MOISTURE);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, MOISTURE);
    }
}

