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
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyEnum<EnumPistonType> TYPE = PropertyEnum.create("type", EnumPistonType.class);
    public static final PropertyBool SHORT = PropertyBool.create("short");

    public BlockPistonExtension() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, EnumPistonType.DEFAULT).withProperty(SHORT, false));
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5f);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos blockpos;
        Block block;
        EnumFacing enumfacing;
        if (player.capabilities.isCreativeMode && (enumfacing = state.getValue(FACING)) != null && ((block = worldIn.getBlockState(blockpos = pos.offset(enumfacing.getOpposite())).getBlock()) == Blocks.piston || block == Blocks.sticky_piston)) {
            worldIn.setBlockToAir(blockpos);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        pos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != Blocks.piston) {
            if (iblockstate.getBlock() != Blocks.sticky_piston) return;
        }
        if (iblockstate.getValue(BlockPistonBase.EXTENDED) == false) return;
        iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
        worldIn.setBlockToAir(pos);
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
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.applyHeadBounds(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.applyCoreBounds(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    private void applyCoreBounds(IBlockState state) {
        float f = 0.25f;
        float f1 = 0.375f;
        float f2 = 0.625f;
        float f3 = 0.25f;
        float f4 = 0.75f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[state.getValue(FACING).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.375f, 0.25f, 0.375f, 0.625f, 1.0f, 0.625f);
                return;
            }
            case 2: {
                this.setBlockBounds(0.375f, 0.0f, 0.375f, 0.625f, 0.75f, 0.625f);
                return;
            }
            case 3: {
                this.setBlockBounds(0.25f, 0.375f, 0.25f, 0.75f, 0.625f, 1.0f);
                return;
            }
            case 4: {
                this.setBlockBounds(0.25f, 0.375f, 0.0f, 0.75f, 0.625f, 0.75f);
                return;
            }
            case 5: {
                this.setBlockBounds(0.375f, 0.25f, 0.25f, 0.625f, 0.75f, 1.0f);
                return;
            }
            case 6: {
                this.setBlockBounds(0.0f, 0.375f, 0.25f, 0.75f, 0.625f, 0.75f);
                return;
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.applyHeadBounds(worldIn.getBlockState(pos));
    }

    public void applyHeadBounds(IBlockState state) {
        float f = 0.25f;
        EnumFacing enumfacing = state.getValue(FACING);
        if (enumfacing == null) return;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
                return;
            }
            case 2: {
                this.setBlockBounds(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f);
                return;
            }
            case 3: {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.25f);
                return;
            }
            case 4: {
                this.setBlockBounds(0.0f, 0.0f, 0.75f, 1.0f, 1.0f, 1.0f);
                return;
            }
            case 5: {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.25f, 1.0f, 1.0f);
                return;
            }
            case 6: {
                this.setBlockBounds(0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                return;
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != Blocks.piston && iblockstate.getBlock() != Blocks.sticky_piston) {
            worldIn.setBlockToAir(pos);
            return;
        }
        iblockstate.getBlock().onNeighborBlockChange(worldIn, blockpos, iblockstate, neighborBlock);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    public static EnumFacing getFacing(int meta) {
        int i = meta & 7;
        if (i > 5) {
            return null;
        }
        EnumFacing enumFacing = EnumFacing.getFront(i);
        return enumFacing;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        Item item;
        if (worldIn.getBlockState(pos).getValue(TYPE) == EnumPistonType.STICKY) {
            item = Item.getItemFromBlock(Blocks.sticky_piston);
            return item;
        }
        item = Item.getItemFromBlock(Blocks.piston);
        return item;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumPistonType enumPistonType;
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(meta));
        if ((meta & 8) > 0) {
            enumPistonType = EnumPistonType.STICKY;
            return iBlockState.withProperty(TYPE, enumPistonType);
        }
        enumPistonType = EnumPistonType.DEFAULT;
        return iBlockState.withProperty(TYPE, enumPistonType);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(TYPE) != EnumPistonType.STICKY) return i |= state.getValue(FACING).getIndex();
        i |= 8;
        return i |= state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, TYPE, SHORT);
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"),
        STICKY("sticky");

        private final String VARIANT;

        private EnumPistonType(String name) {
            this.VARIANT = name;
        }

        public String toString() {
            return this.VARIANT;
        }

        @Override
        public String getName() {
            return this.VARIANT;
        }
    }
}

