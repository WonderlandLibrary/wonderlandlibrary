/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest
extends BlockContainer {
    public final int chestType;
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing());
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, blockPos, (IInventory)((Object)tileEntity));
            world.updateComparatorOutputLevel(blockPos, this);
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    private boolean isBlocked(World world, BlockPos blockPos) {
        return this.isBelowSolidBlock(world, blockPos) || this.isOcelotSittingOnChest(world, blockPos);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        if (!this.canProvidePower()) {
            return 0;
        }
        int n = 0;
        TileEntity tileEntity = iBlockAccess.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityChest) {
            n = ((TileEntityChest)tileEntity).numPlayersUsing;
        }
        return MathHelper.clamp_int(n, 0, 15);
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(world, blockPos));
    }

    public IBlockState checkForSurroundingChests(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (world.isRemote) {
            return iBlockState;
        }
        IBlockState iBlockState2 = world.getBlockState(blockPos.north());
        IBlockState iBlockState3 = world.getBlockState(blockPos.south());
        IBlockState iBlockState4 = world.getBlockState(blockPos.west());
        IBlockState iBlockState5 = world.getBlockState(blockPos.east());
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        Block block = iBlockState2.getBlock();
        Block block2 = iBlockState3.getBlock();
        Block block3 = iBlockState4.getBlock();
        Block block4 = iBlockState5.getBlock();
        if (block != this && block2 != this) {
            boolean bl = block.isFullBlock();
            boolean bl2 = block2.isFullBlock();
            if (block3 == this || block4 == this) {
                BlockPos blockPos2 = block3 == this ? blockPos.west() : blockPos.east();
                IBlockState iBlockState6 = world.getBlockState(blockPos2.north());
                IBlockState iBlockState7 = world.getBlockState(blockPos2.south());
                enumFacing = EnumFacing.SOUTH;
                EnumFacing enumFacing2 = block3 == this ? iBlockState4.getValue(FACING) : iBlockState5.getValue(FACING);
                if (enumFacing2 == EnumFacing.NORTH) {
                    enumFacing = EnumFacing.NORTH;
                }
                Block block5 = iBlockState6.getBlock();
                Block block6 = iBlockState7.getBlock();
                if ((bl || block5.isFullBlock()) && !bl2 && !block6.isFullBlock()) {
                    enumFacing = EnumFacing.SOUTH;
                }
                if ((bl2 || block6.isFullBlock()) && !bl && !block5.isFullBlock()) {
                    enumFacing = EnumFacing.NORTH;
                }
            }
        } else {
            BlockPos blockPos3 = block == this ? blockPos.north() : blockPos.south();
            IBlockState iBlockState8 = world.getBlockState(blockPos3.west());
            IBlockState iBlockState9 = world.getBlockState(blockPos3.east());
            enumFacing = EnumFacing.EAST;
            EnumFacing enumFacing3 = block == this ? iBlockState2.getValue(FACING) : iBlockState3.getValue(FACING);
            if (enumFacing3 == EnumFacing.WEST) {
                enumFacing = EnumFacing.WEST;
            }
            Block block7 = iBlockState8.getBlock();
            Block block8 = iBlockState9.getBlock();
            if ((block3.isFullBlock() || block7.isFullBlock()) && !block4.isFullBlock() && !block8.isFullBlock()) {
                enumFacing = EnumFacing.EAST;
            }
            if ((block4.isFullBlock() || block8.isFullBlock()) && !block3.isFullBlock() && !block7.isFullBlock()) {
                enumFacing = EnumFacing.WEST;
            }
        }
        iBlockState = iBlockState.withProperty(FACING, enumFacing);
        world.setBlockState(blockPos, iBlockState, 3);
        return iBlockState;
    }

    private boolean isOcelotSittingOnChest(World world, BlockPos blockPos) {
        for (Entity entity : world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 2, blockPos.getZ() + 1))) {
            EntityOcelot entityOcelot = (EntityOcelot)entity;
            if (!entityOcelot.isSitting()) continue;
            return true;
        }
        return false;
    }

    protected BlockChest(int n) {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.chestType = n;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityChest();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        int n = 0;
        BlockPos blockPos2 = blockPos.west();
        BlockPos blockPos3 = blockPos.east();
        BlockPos blockPos4 = blockPos.north();
        BlockPos blockPos5 = blockPos.south();
        if (world.getBlockState(blockPos2).getBlock() == this) {
            if (this.isDoubleChest(world, blockPos2)) {
                return false;
            }
            ++n;
        }
        if (world.getBlockState(blockPos3).getBlock() == this) {
            if (this.isDoubleChest(world, blockPos3)) {
                return false;
            }
            ++n;
        }
        if (world.getBlockState(blockPos4).getBlock() == this) {
            if (this.isDoubleChest(world, blockPos4)) {
                return false;
            }
            ++n;
        }
        if (world.getBlockState(blockPos5).getBlock() == this) {
            if (this.isDoubleChest(world, blockPos5)) {
                return false;
            }
            ++n;
        }
        return n <= 1;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (iBlockAccess.getBlockState(blockPos.north()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0f, 0.9375f, 0.875f, 0.9375f);
        } else if (iBlockAccess.getBlockState(blockPos.south()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 1.0f);
        } else if (iBlockAccess.getBlockState(blockPos.west()).getBlock() == this) {
            this.setBlockBounds(0.0f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        } else if (iBlockAccess.getBlockState(blockPos.east()).getBlock() == this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 1.0f, 0.875f, 0.9375f);
        } else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        ILockableContainer iLockableContainer = this.getLockableContainer(world, blockPos);
        if (iLockableContainer != null) {
            entityPlayer.displayGUIChest(iLockableContainer);
            if (this.chestType == 0) {
                entityPlayer.triggerAchievement(StatList.field_181723_aa);
            } else if (this.chestType == 1) {
                entityPlayer.triggerAchievement(StatList.field_181737_U);
            }
        }
        return true;
    }

    private boolean isDoubleChest(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock() != this) {
            return false;
        }
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            if (world.getBlockState(blockPos.offset(enumFacing)).getBlock() != this) continue;
            return true;
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    private boolean isBelowSolidBlock(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos.up()).getBlock().isNormalCube();
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        super.onNeighborBlockChange(world, blockPos, iBlockState, block);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityChest) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canProvidePower() {
        return this.chestType == 1;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity;
        boolean bl;
        EnumFacing enumFacing = EnumFacing.getHorizontal(MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0f / 360.0f) + 0.5) & 3).getOpposite();
        iBlockState = iBlockState.withProperty(FACING, enumFacing);
        BlockPos blockPos2 = blockPos.north();
        BlockPos blockPos3 = blockPos.south();
        BlockPos blockPos4 = blockPos.west();
        BlockPos blockPos5 = blockPos.east();
        boolean bl2 = this == world.getBlockState(blockPos2).getBlock();
        boolean bl3 = this == world.getBlockState(blockPos3).getBlock();
        boolean bl4 = this == world.getBlockState(blockPos4).getBlock();
        boolean bl5 = bl = this == world.getBlockState(blockPos5).getBlock();
        if (!(bl2 || bl3 || bl4 || bl)) {
            world.setBlockState(blockPos, iBlockState, 3);
        } else if (enumFacing.getAxis() != EnumFacing.Axis.X || !bl2 && !bl3) {
            if (enumFacing.getAxis() == EnumFacing.Axis.Z && (bl4 || bl)) {
                if (bl4) {
                    world.setBlockState(blockPos4, iBlockState, 3);
                } else {
                    world.setBlockState(blockPos5, iBlockState, 3);
                }
                world.setBlockState(blockPos, iBlockState, 3);
            }
        } else {
            if (bl2) {
                world.setBlockState(blockPos2, iBlockState, 3);
            } else {
                world.setBlockState(blockPos3, iBlockState, 3);
            }
            world.setBlockState(blockPos, iBlockState, 3);
        }
        if (itemStack.hasDisplayName() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityChest) {
            ((TileEntityChest)tileEntity).setCustomName(itemStack.getDisplayName());
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing enumFacing = EnumFacing.getFront(n);
        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.checkForSurroundingChests(world, blockPos, iBlockState);
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            IBlockState iBlockState2 = world.getBlockState(blockPos2);
            if (iBlockState2.getBlock() != this) continue;
            this.checkForSurroundingChests(world, blockPos2, iBlockState2);
        }
    }

    public IBlockState correctFacing(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing3;
        EnumFacing enumFacing2 = null;
        for (EnumFacing enumFacing3 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iBlockState2 = world.getBlockState(blockPos.offset(enumFacing3));
            if (iBlockState2.getBlock() == this) {
                return iBlockState;
            }
            if (!iBlockState2.getBlock().isFullBlock()) continue;
            if (enumFacing2 != null) {
                enumFacing2 = null;
                break;
            }
            enumFacing2 = enumFacing3;
        }
        if (enumFacing2 != null) {
            return iBlockState.withProperty(FACING, enumFacing2.getOpposite());
        }
        enumFacing3 = iBlockState.getValue(FACING);
        if (world.getBlockState(blockPos.offset(enumFacing3)).getBlock().isFullBlock()) {
            enumFacing3 = enumFacing3.getOpposite();
        }
        if (world.getBlockState(blockPos.offset(enumFacing3)).getBlock().isFullBlock()) {
            enumFacing3 = enumFacing3.rotateY();
        }
        if (world.getBlockState(blockPos.offset(enumFacing3)).getBlock().isFullBlock()) {
            enumFacing3 = enumFacing3.getOpposite();
        }
        return iBlockState.withProperty(FACING, enumFacing3);
    }

    public ILockableContainer getLockableContainer(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (!(tileEntity instanceof TileEntityChest)) {
            return null;
        }
        ILockableContainer iLockableContainer = (TileEntityChest)tileEntity;
        if (this.isBlocked(world, blockPos)) {
            return null;
        }
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            Block block = world.getBlockState(blockPos2).getBlock();
            if (block != this) continue;
            if (this.isBlocked(world, blockPos2)) {
                return null;
            }
            TileEntity tileEntity2 = world.getTileEntity(blockPos2);
            if (!(tileEntity2 instanceof TileEntityChest)) continue;
            iLockableContainer = enumFacing != EnumFacing.WEST && enumFacing != EnumFacing.NORTH ? new InventoryLargeChest("container.chestDouble", iLockableContainer, (TileEntityChest)tileEntity2) : new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileEntity2, iLockableContainer);
        }
        return iLockableContainer;
    }

    @Override
    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.UP ? this.getWeakPower(iBlockAccess, blockPos, iBlockState, enumFacing) : 0;
    }
}

