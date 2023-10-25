/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire
extends Block {
    public static final PropertyBool NORTH;
    public static final PropertyBool SOUTH;
    public static final PropertyBool SUSPENDED;
    public static final PropertyBool WEST;
    public static final PropertyBool POWERED;
    public static final PropertyBool ATTACHED;
    public static final PropertyBool DISARMED;
    public static final PropertyBool EAST;

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        if (!world.isRemote && !iBlockState.getValue(POWERED).booleanValue()) {
            this.updateState(world, blockPos);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        boolean bl = iBlockState.getValue(ATTACHED);
        boolean bl2 = iBlockState.getValue(SUSPENDED);
        if (!bl2) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
        } else if (!bl) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.string;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.string;
    }

    static {
        POWERED = PropertyBool.create("powered");
        SUSPENDED = PropertyBool.create("suspended");
        ATTACHED = PropertyBool.create("attached");
        DISARMED = PropertyBool.create("disarmed");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    private void notifyHook(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing[] enumFacingArray = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST};
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            int n3 = 1;
            while (n3 < 42) {
                BlockPos blockPos2 = blockPos.offset(enumFacing, n3);
                IBlockState iBlockState2 = world.getBlockState(blockPos2);
                if (iBlockState2.getBlock() == Blocks.tripwire_hook) {
                    if (iBlockState2.getValue(BlockTripWireHook.FACING) != enumFacing.getOpposite()) break;
                    Blocks.tripwire_hook.func_176260_a(world, blockPos2, iBlockState2, false, true, n3, iBlockState);
                    break;
                }
                if (iBlockState2.getBlock() != Blocks.tripwire) break;
                ++n3;
            }
            ++n2;
        }
    }

    private void updateState(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        boolean bl = iBlockState.getValue(POWERED);
        boolean bl2 = false;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((double)blockPos.getX() + this.minX, (double)blockPos.getY() + this.minY, (double)blockPos.getZ() + this.minZ, (double)blockPos.getX() + this.maxX, (double)blockPos.getY() + this.maxY, (double)blockPos.getZ() + this.maxZ));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.doesEntityNotTriggerPressurePlate()) continue;
                bl2 = true;
                break;
            }
        }
        if (bl2 != bl) {
            iBlockState = iBlockState.withProperty(POWERED, bl2);
            world.setBlockState(blockPos, iBlockState, 3);
            this.notifyHook(world, blockPos, iBlockState);
        }
        if (bl2) {
            world.scheduleUpdate(blockPos, this, this.tickRate(world));
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        if (iBlockState.getValue(POWERED).booleanValue()) {
            n |= 1;
        }
        if (iBlockState.getValue(SUSPENDED).booleanValue()) {
            n |= 2;
        }
        if (iBlockState.getValue(ATTACHED).booleanValue()) {
            n |= 4;
        }
        if (iBlockState.getValue(DISARMED).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        iBlockState = iBlockState.withProperty(SUSPENDED, !World.doesBlockHaveSolidTopSurface(world, blockPos.down()));
        world.setBlockState(blockPos, iBlockState, 3);
        this.notifyHook(world, blockPos, iBlockState);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public static boolean isConnectedTo(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos2);
        Block block = iBlockState2.getBlock();
        if (block == Blocks.tripwire_hook) {
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            return iBlockState2.getValue(BlockTripWireHook.FACING) == enumFacing2;
        }
        if (block == Blocks.tripwire) {
            boolean bl;
            boolean bl2 = iBlockState.getValue(SUSPENDED);
            return bl2 == (bl = iBlockState2.getValue(SUSPENDED).booleanValue());
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        if (!world.isRemote && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().getItem() == Items.shears) {
            world.setBlockState(blockPos, iBlockState.withProperty(DISARMED, true), 4);
        }
    }

    @Override
    public void randomTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockState.withProperty(NORTH, BlockTripWire.isConnectedTo(iBlockAccess, blockPos, iBlockState, EnumFacing.NORTH)).withProperty(EAST, BlockTripWire.isConnectedTo(iBlockAccess, blockPos, iBlockState, EnumFacing.EAST)).withProperty(SOUTH, BlockTripWire.isConnectedTo(iBlockAccess, blockPos, iBlockState, EnumFacing.SOUTH)).withProperty(WEST, BlockTripWire.isConnectedTo(iBlockAccess, blockPos, iBlockState, EnumFacing.WEST));
    }

    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(SUSPENDED, false).withProperty(ATTACHED, false).withProperty(DISARMED, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote && world.getBlockState(blockPos).getValue(POWERED).booleanValue()) {
            this.updateState(world, blockPos);
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        boolean bl;
        boolean bl2 = iBlockState.getValue(SUSPENDED);
        boolean bl3 = bl = !World.doesBlockHaveSolidTopSurface(world, blockPos.down());
        if (bl2 != bl) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(POWERED, (n & 1) > 0).withProperty(SUSPENDED, (n & 2) > 0).withProperty(ATTACHED, (n & 4) > 0).withProperty(DISARMED, (n & 8) > 0);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.notifyHook(world, blockPos, iBlockState.withProperty(POWERED, true));
    }
}

