/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension
extends Block {
    public static final PropertyBool SHORT;
    public static final PropertyEnum<EnumPistonType> TYPE;
    public static final PropertyDirection FACING;

    public static EnumFacing getFacing(int n) {
        int n2 = n & 7;
        return n2 > 5 ? null : EnumFacing.getFront(n2);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getIndex();
        if (iBlockState.getValue(TYPE) == EnumPistonType.STICKY) {
            n |= 8;
        }
        return n;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, TYPE, SHORT);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(n)).withProperty(TYPE, (n & 8) > 0 ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
    }

    static {
        FACING = PropertyDirection.create("facing");
        TYPE = PropertyEnum.create("type", EnumPistonType.class);
        SHORT = PropertyBool.create("short");
    }

    public void applyHeadBounds(IBlockState iBlockState) {
        float f = 0.25f;
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        if (enumFacing != null) {
            switch (enumFacing) {
                case DOWN: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
                    break;
                }
                case UP: {
                    this.setBlockBounds(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case NORTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.25f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.75f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.25f, 1.0f, 1.0f);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getValue(TYPE) == EnumPistonType.STICKY ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        BlockPos blockPos2;
        Block block;
        EnumFacing enumFacing;
        if (entityPlayer.capabilities.isCreativeMode && (enumFacing = iBlockState.getValue(FACING)) != null && ((block = world.getBlockState(blockPos2 = blockPos.offset(enumFacing.getOpposite())).getBlock()) == Blocks.piston || block == Blocks.sticky_piston)) {
            world.setBlockToAir(blockPos2);
        }
        super.onBlockHarvested(world, blockPos, iBlockState, entityPlayer);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.breakBlock(world, blockPos, iBlockState);
        EnumFacing enumFacing = iBlockState.getValue(FACING).getOpposite();
        blockPos = blockPos.offset(enumFacing);
        IBlockState iBlockState2 = world.getBlockState(blockPos);
        if ((iBlockState2.getBlock() == Blocks.piston || iBlockState2.getBlock() == Blocks.sticky_piston) && iBlockState2.getValue(BlockPistonBase.EXTENDED).booleanValue()) {
            iBlockState2.getBlock().dropBlockAsItem(world, blockPos, iBlockState2, 0);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.offset(enumFacing.getOpposite());
        IBlockState iBlockState2 = world.getBlockState(blockPos2);
        if (iBlockState2.getBlock() != Blocks.piston && iBlockState2.getBlock() != Blocks.sticky_piston) {
            world.setBlockToAir(blockPos);
        } else {
            iBlockState2.getBlock().onNeighborBlockChange(world, blockPos2, iBlockState2, block);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.applyHeadBounds(iBlockAccess.getBlockState(blockPos));
    }

    private void applyCoreBounds(IBlockState iBlockState) {
        float f = 0.25f;
        float f2 = 0.375f;
        float f3 = 0.625f;
        float f4 = 0.25f;
        float f5 = 0.75f;
        switch (iBlockState.getValue(FACING)) {
            case DOWN: {
                this.setBlockBounds(0.375f, 0.25f, 0.375f, 0.625f, 1.0f, 0.625f);
                break;
            }
            case UP: {
                this.setBlockBounds(0.375f, 0.0f, 0.375f, 0.625f, 0.75f, 0.625f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.25f, 0.375f, 0.25f, 0.75f, 0.625f, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.25f, 0.375f, 0.0f, 0.75f, 0.625f, 0.75f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.375f, 0.25f, 0.25f, 0.625f, 0.75f, 1.0f);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, 0.375f, 0.25f, 0.75f, 0.625f, 0.75f);
            }
        }
    }

    public BlockPistonExtension() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, EnumPistonType.DEFAULT).withProperty(SHORT, false));
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5f);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        this.applyHeadBounds(iBlockState);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.applyCoreBounds(iBlockState);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"),
        STICKY("sticky");

        private final String VARIANT;

        public String toString() {
            return this.VARIANT;
        }

        @Override
        public String getName() {
            return this.VARIANT;
        }

        private EnumPistonType(String string2) {
            this.VARIANT = string2;
        }
    }
}

