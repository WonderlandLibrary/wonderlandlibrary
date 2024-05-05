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
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(SUSPENDED, false).withProperty(ATTACHED, false).withProperty(DISARMED, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.NORTH)).withProperty(EAST, BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.EAST)).withProperty(SOUTH, BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH)).withProperty(WEST, BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.WEST));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.string;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.string;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean flag = state.getValue(SUSPENDED);
        boolean flag1 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        if (flag == flag1) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = iblockstate.getValue(ATTACHED);
        boolean flag1 = iblockstate.getValue(SUSPENDED);
        if (!flag1) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
            return;
        }
        if (!flag) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
            return;
        }
        this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        state = state.withProperty(SUSPENDED, !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()));
        worldIn.setBlockState(pos, state, 3);
        this.notifyHook(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.notifyHook(worldIn, pos, state.withProperty(POWERED, true));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (worldIn.isRemote) return;
        if (player.getCurrentEquippedItem() == null) return;
        if (player.getCurrentEquippedItem().getItem() != Items.shears) return;
        worldIn.setBlockState(pos, state.withProperty(DISARMED, true), 4);
    }

    private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing[] enumFacingArray = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST};
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            for (int i = 1; i < 42; ++i) {
                BlockPos blockpos = pos.offset(enumfacing, i);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                    if (iblockstate.getValue(BlockTripWireHook.FACING) != enumfacing.getOpposite()) break;
                    Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
                    break;
                }
                if (iblockstate.getBlock() != Blocks.tripwire) break;
            }
            ++n2;
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        if (state.getValue(POWERED) != false) return;
        this.updateState(worldIn, pos);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        if (worldIn.getBlockState(pos).getValue(POWERED) == false) return;
        this.updateState(worldIn, pos);
    }

    private void updateState(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = iblockstate.getValue(POWERED);
        boolean flag1 = false;
        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.doesEntityNotTriggerPressurePlate()) continue;
                flag1 = true;
                break;
            }
        }
        if (flag1 != flag) {
            iblockstate = iblockstate.withProperty(POWERED, flag1);
            worldIn.setBlockState(pos, iblockstate, 3);
            this.notifyHook(worldIn, pos, iblockstate);
        }
        if (!flag1) return;
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
        boolean flag1;
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.tripwire_hook) {
            EnumFacing enumfacing = direction.getOpposite();
            if (iblockstate.getValue(BlockTripWireHook.FACING) != enumfacing) return false;
            return true;
        }
        if (block != Blocks.tripwire) return false;
        boolean flag = state.getValue(SUSPENDED);
        if (flag != (flag1 = iblockstate.getValue(SUSPENDED).booleanValue())) return false;
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(POWERED, (meta & 1) > 0).withProperty(SUSPENDED, (meta & 2) > 0).withProperty(ATTACHED, (meta & 4) > 0);
        if ((meta & 8) > 0) {
            bl = true;
            return iBlockState.withProperty(DISARMED, bl);
        }
        bl = false;
        return iBlockState.withProperty(DISARMED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(POWERED).booleanValue()) {
            i |= 1;
        }
        if (state.getValue(SUSPENDED).booleanValue()) {
            i |= 2;
        }
        if (state.getValue(ATTACHED).booleanValue()) {
            i |= 4;
        }
        if (state.getValue(DISARMED) == false) return i;
        i |= 8;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH);
    }
}

