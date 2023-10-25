/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser
extends BlockContainer {
    protected Random rand = new Random();
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> dispenseBehaviorRegistry;
    public static final PropertyBool TRIGGERED;

    private void setDefaultDirection(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            boolean bl = world.getBlockState(blockPos.north()).getBlock().isFullBlock();
            boolean bl2 = world.getBlockState(blockPos.south()).getBlock().isFullBlock();
            if (enumFacing == EnumFacing.NORTH && bl && !bl2) {
                enumFacing = EnumFacing.SOUTH;
            } else if (enumFacing == EnumFacing.SOUTH && bl2 && !bl) {
                enumFacing = EnumFacing.NORTH;
            } else {
                boolean bl3 = world.getBlockState(blockPos.west()).getBlock().isFullBlock();
                boolean bl4 = world.getBlockState(blockPos.east()).getBlock().isFullBlock();
                if (enumFacing == EnumFacing.WEST && bl3 && !bl4) {
                    enumFacing = EnumFacing.EAST;
                } else if (enumFacing == EnumFacing.EAST && bl4 && !bl3) {
                    enumFacing = EnumFacing.WEST;
                }
            }
            world.setBlockState(blockPos, iBlockState.withProperty(FACING, enumFacing).withProperty(TRIGGERED, false), 2);
        }
    }

    protected BlockDispenser() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    protected IBehaviorDispenseItem getBehavior(ItemStack itemStack) {
        return dispenseBehaviorRegistry.getObject(itemStack == null ? null : itemStack.getItem());
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.onBlockAdded(world, blockPos, iBlockState);
        this.setDefaultDirection(world, blockPos, iBlockState);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity;
        world.setBlockState(blockPos, iBlockState.withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, blockPos, entityLivingBase)), 2);
        if (itemStack.hasDisplayName() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityDispenser) {
            ((TileEntityDispenser)tileEntity).setCustomName(itemStack.getDisplayName());
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, TRIGGERED);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getIndex();
        if (iBlockState.getValue(TRIGGERED).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(world, blockPos, (TileEntityDispenser)tileEntity);
            world.updateComparatorOutputLevel(blockPos, this);
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityDispenser();
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityDispenser) {
            entityPlayer.displayGUIChest((TileEntityDispenser)tileEntity);
            if (tileEntity instanceof TileEntityDropper) {
                entityPlayer.triggerAchievement(StatList.field_181731_O);
            } else {
                entityPlayer.triggerAchievement(StatList.field_181733_Q);
            }
        }
        return true;
    }

    @Override
    public int tickRate(World world) {
        return 4;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return Container.calcRedstone(world.getTileEntity(blockPos));
    }

    @Override
    public IBlockState getStateForEntityRender(IBlockState iBlockState) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    protected void dispense(World world, BlockPos blockPos) {
        BlockSourceImpl blockSourceImpl = new BlockSourceImpl(world, blockPos);
        TileEntityDispenser tileEntityDispenser = (TileEntityDispenser)blockSourceImpl.getBlockTileEntity();
        if (tileEntityDispenser != null) {
            int n = tileEntityDispenser.getDispenseSlot();
            if (n < 0) {
                world.playAuxSFX(1001, blockPos, 0);
            } else {
                ItemStack itemStack = tileEntityDispenser.getStackInSlot(n);
                IBehaviorDispenseItem iBehaviorDispenseItem = this.getBehavior(itemStack);
                if (iBehaviorDispenseItem != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
                    ItemStack itemStack2 = iBehaviorDispenseItem.dispense(blockSourceImpl, itemStack);
                    tileEntityDispenser.setInventorySlotContents(n, itemStack2.stackSize <= 0 ? null : itemStack2);
                }
            }
        }
    }

    public static EnumFacing getFacing(int n) {
        return EnumFacing.getFront(n & 7);
    }

    public static IPosition getDispensePosition(IBlockSource iBlockSource) {
        EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
        double d = iBlockSource.getX() + 0.7 * (double)enumFacing.getFrontOffsetX();
        double d2 = iBlockSource.getY() + 0.7 * (double)enumFacing.getFrontOffsetY();
        double d3 = iBlockSource.getZ() + 0.7 * (double)enumFacing.getFrontOffsetZ();
        return new PositionImpl(d, d2, d3);
    }

    static {
        TRIGGERED = PropertyBool.create("triggered");
        dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote) {
            this.dispense(world, blockPos);
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, blockPos, entityLivingBase)).withProperty(TRIGGERED, false);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        boolean bl = world.isBlockPowered(blockPos) || world.isBlockPowered(blockPos.up());
        boolean bl2 = iBlockState.getValue(TRIGGERED);
        if (bl && !bl2) {
            world.scheduleUpdate(blockPos, this, this.tickRate(world));
            world.setBlockState(blockPos, iBlockState.withProperty(TRIGGERED, true), 4);
        } else if (!bl && bl2) {
            world.setBlockState(blockPos, iBlockState.withProperty(TRIGGERED, false), 4);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, BlockDispenser.getFacing(n)).withProperty(TRIGGERED, (n & 8) > 0);
    }
}

