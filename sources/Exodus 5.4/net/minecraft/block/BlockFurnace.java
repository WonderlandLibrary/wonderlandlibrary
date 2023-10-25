/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockFurnace
extends BlockContainer {
    private static boolean keepInventory;
    private final boolean isBurning;
    public static final PropertyDirection FACING;

    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing enumFacing = EnumFacing.getFront(n);
        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.setDefaultFacing(world, blockPos, iBlockState);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return Container.calcRedstone(world.getTileEntity(blockPos));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityFurnace) {
            entityPlayer.displayGUIChest((TileEntityFurnace)tileEntity);
            entityPlayer.triggerAchievement(StatList.field_181741_Y);
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        TileEntity tileEntity;
        if (!keepInventory && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityFurnace) {
            InventoryHelper.dropInventoryItems(world, blockPos, (TileEntityFurnace)tileEntity);
            world.updateComparatorOutputLevel(blockPos, this);
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite());
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this.isBurning) {
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            double d = (double)blockPos.getX() + 0.5;
            double d2 = (double)blockPos.getY() + random.nextDouble() * 6.0 / 16.0;
            double d3 = (double)blockPos.getZ() + 0.5;
            double d4 = 0.52;
            double d5 = random.nextDouble() * 0.6 - 0.3;
            switch (enumFacing) {
                case WEST: {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d - d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d - d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case EAST: {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d + d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d + d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case NORTH: {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d + d5, d2, d3 - d4, 0.0, 0.0, 0.0, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d + d5, d2, d3 - d4, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case SOUTH: {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d + d5, d2, d3 + d4, 0.0, 0.0, 0.0, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d + d5, d2, d3 + d4, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
    }

    protected BlockFurnace(boolean bl) {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.isBurning = bl;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(FACING).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity;
        world.setBlockState(blockPos, iBlockState.withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite()), 2);
        if (itemStack.hasDisplayName() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityFurnace) {
            ((TileEntityFurnace)tileEntity).setCustomInventoryName(itemStack.getDisplayName());
        }
    }

    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }

    @Override
    public IBlockState getStateForEntityRender(IBlockState iBlockState) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityFurnace();
    }

    private void setDefaultFacing(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            Block block = world.getBlockState(blockPos.north()).getBlock();
            Block block2 = world.getBlockState(blockPos.south()).getBlock();
            Block block3 = world.getBlockState(blockPos.west()).getBlock();
            Block block4 = world.getBlockState(blockPos.east()).getBlock();
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            if (enumFacing == EnumFacing.NORTH && block.isFullBlock() && !block2.isFullBlock()) {
                enumFacing = EnumFacing.SOUTH;
            } else if (enumFacing == EnumFacing.SOUTH && block2.isFullBlock() && !block.isFullBlock()) {
                enumFacing = EnumFacing.NORTH;
            } else if (enumFacing == EnumFacing.WEST && block3.isFullBlock() && !block4.isFullBlock()) {
                enumFacing = EnumFacing.EAST;
            } else if (enumFacing == EnumFacing.EAST && block4.isFullBlock() && !block3.isFullBlock()) {
                enumFacing = EnumFacing.WEST;
            }
            world.setBlockState(blockPos, iBlockState.withProperty(FACING, enumFacing), 2);
        }
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(Blocks.furnace);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.furnace);
    }

    public static void setState(boolean bl, World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        keepInventory = true;
        if (bl) {
            world.setBlockState(blockPos, Blocks.lit_furnace.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
            world.setBlockState(blockPos, Blocks.lit_furnace.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
        } else {
            world.setBlockState(blockPos, Blocks.furnace.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
            world.setBlockState(blockPos, Blocks.furnace.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
        }
        keepInventory = false;
        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(blockPos, tileEntity);
        }
    }
}

