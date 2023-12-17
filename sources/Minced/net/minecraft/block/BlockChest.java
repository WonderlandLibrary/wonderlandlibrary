// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.inventory.Container;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.inventory.InventoryLargeChest;
import javax.annotation.Nullable;
import net.minecraft.world.ILockableContainer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;

public class BlockChest extends BlockContainer
{
    public static final PropertyDirection FACING;
    protected static final AxisAlignedBB NORTH_CHEST_AABB;
    protected static final AxisAlignedBB SOUTH_CHEST_AABB;
    protected static final AxisAlignedBB WEST_CHEST_AABB;
    protected static final AxisAlignedBB EAST_CHEST_AABB;
    protected static final AxisAlignedBB NOT_CONNECTED_AABB;
    public final Type chestType;
    
    protected BlockChest(final Type chestTypeIn) {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockChest.FACING, EnumFacing.NORTH));
        this.chestType = chestTypeIn;
        this.setCreativeTab((chestTypeIn == Type.TRAP) ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS);
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean hasCustomBreakingProgress(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        if (source.getBlockState(pos.north()).getBlock() == this) {
            return BlockChest.NORTH_CHEST_AABB;
        }
        if (source.getBlockState(pos.south()).getBlock() == this) {
            return BlockChest.SOUTH_CHEST_AABB;
        }
        if (source.getBlockState(pos.west()).getBlock() == this) {
            return BlockChest.WEST_CHEST_AABB;
        }
        return (source.getBlockState(pos.east()).getBlock() == this) ? BlockChest.EAST_CHEST_AABB : BlockChest.NOT_CONNECTED_AABB;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForSurroundingChests(worldIn, pos, state);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset(enumfacing);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == this) {
                this.checkForSurroundingChests(worldIn, blockpos, iblockstate);
            }
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, placer.getHorizontalFacing());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final EnumFacing enumfacing = EnumFacing.byHorizontalIndex(MathHelper.floor(placer.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3).getOpposite();
        state = state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
        final BlockPos blockpos = pos.north();
        final BlockPos blockpos2 = pos.south();
        final BlockPos blockpos3 = pos.west();
        final BlockPos blockpos4 = pos.east();
        final boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
        final boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
        final boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();
        final boolean flag4 = this == worldIn.getBlockState(blockpos4).getBlock();
        if (!flag && !flag2 && !flag3 && !flag4) {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || (!flag && !flag2)) {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag3 || flag4)) {
                if (flag3) {
                    worldIn.setBlockState(blockpos3, state, 3);
                }
                else {
                    worldIn.setBlockState(blockpos4, state, 3);
                }
                worldIn.setBlockState(pos, state, 3);
            }
        }
        else {
            if (flag) {
                worldIn.setBlockState(blockpos, state, 3);
            }
            else {
                worldIn.setBlockState(blockpos2, state, 3);
            }
            worldIn.setBlockState(pos, state, 3);
        }
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    public IBlockState checkForSurroundingChests(final World worldIn, final BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) {
            return state;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos.north());
        final IBlockState iblockstate2 = worldIn.getBlockState(pos.south());
        final IBlockState iblockstate3 = worldIn.getBlockState(pos.west());
        final IBlockState iblockstate4 = worldIn.getBlockState(pos.east());
        EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockChest.FACING);
        if (iblockstate.getBlock() != this && iblockstate2.getBlock() != this) {
            final boolean flag = iblockstate.isFullBlock();
            final boolean flag2 = iblockstate2.isFullBlock();
            if (iblockstate3.getBlock() == this || iblockstate4.getBlock() == this) {
                final BlockPos blockpos1 = (iblockstate3.getBlock() == this) ? pos.west() : pos.east();
                final IBlockState iblockstate5 = worldIn.getBlockState(blockpos1.north());
                final IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.south());
                enumfacing = EnumFacing.SOUTH;
                EnumFacing enumfacing2;
                if (iblockstate3.getBlock() == this) {
                    enumfacing2 = iblockstate3.getValue((IProperty<EnumFacing>)BlockChest.FACING);
                }
                else {
                    enumfacing2 = iblockstate4.getValue((IProperty<EnumFacing>)BlockChest.FACING);
                }
                if (enumfacing2 == EnumFacing.NORTH) {
                    enumfacing = EnumFacing.NORTH;
                }
                if ((flag || iblockstate5.isFullBlock()) && !flag2 && !iblockstate6.isFullBlock()) {
                    enumfacing = EnumFacing.SOUTH;
                }
                if ((flag2 || iblockstate6.isFullBlock()) && !flag && !iblockstate5.isFullBlock()) {
                    enumfacing = EnumFacing.NORTH;
                }
            }
        }
        else {
            final BlockPos blockpos2 = (iblockstate.getBlock() == this) ? pos.north() : pos.south();
            final IBlockState iblockstate7 = worldIn.getBlockState(blockpos2.west());
            final IBlockState iblockstate8 = worldIn.getBlockState(blockpos2.east());
            enumfacing = EnumFacing.EAST;
            EnumFacing enumfacing3;
            if (iblockstate.getBlock() == this) {
                enumfacing3 = iblockstate.getValue((IProperty<EnumFacing>)BlockChest.FACING);
            }
            else {
                enumfacing3 = iblockstate2.getValue((IProperty<EnumFacing>)BlockChest.FACING);
            }
            if (enumfacing3 == EnumFacing.WEST) {
                enumfacing = EnumFacing.WEST;
            }
            if ((iblockstate3.isFullBlock() || iblockstate7.isFullBlock()) && !iblockstate4.isFullBlock() && !iblockstate8.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            }
            if ((iblockstate4.isFullBlock() || iblockstate8.isFullBlock()) && !iblockstate3.isFullBlock() && !iblockstate7.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
        }
        state = state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
        worldIn.setBlockState(pos, state, 3);
        return state;
    }
    
    public IBlockState correctFacing(final World worldIn, final BlockPos pos, final IBlockState state) {
        EnumFacing enumfacing = null;
        for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing2));
            if (iblockstate.getBlock() == this) {
                return state;
            }
            if (!iblockstate.isFullBlock()) {
                continue;
            }
            if (enumfacing != null) {
                enumfacing = null;
                break;
            }
            enumfacing = enumfacing2;
        }
        if (enumfacing != null) {
            return state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing.getOpposite());
        }
        EnumFacing enumfacing3 = state.getValue((IProperty<EnumFacing>)BlockChest.FACING);
        if (worldIn.getBlockState(pos.offset(enumfacing3)).isFullBlock()) {
            enumfacing3 = enumfacing3.getOpposite();
        }
        if (worldIn.getBlockState(pos.offset(enumfacing3)).isFullBlock()) {
            enumfacing3 = enumfacing3.rotateY();
        }
        if (worldIn.getBlockState(pos.offset(enumfacing3)).isFullBlock()) {
            enumfacing3 = enumfacing3.getOpposite();
        }
        return state.withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing3);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        int i = 0;
        final BlockPos blockpos = pos.west();
        final BlockPos blockpos2 = pos.east();
        final BlockPos blockpos3 = pos.north();
        final BlockPos blockpos4 = pos.south();
        if (worldIn.getBlockState(blockpos).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos2).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos2)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos3).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos3)) {
                return false;
            }
            ++i;
        }
        if (worldIn.getBlockState(blockpos4).getBlock() == this) {
            if (this.isDoubleChest(worldIn, blockpos4)) {
                return false;
            }
            ++i;
        }
        return i <= 1;
    }
    
    private boolean isDoubleChest(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return false;
        }
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityChest) {
            tileentity.updateContainingBlockInfo();
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);
        if (ilockablecontainer != null) {
            playerIn.displayGUIChest(ilockablecontainer);
            if (this.chestType == Type.BASIC) {
                playerIn.addStat(StatList.CHEST_OPENED);
            }
            else if (this.chestType == Type.TRAP) {
                playerIn.addStat(StatList.TRAPPED_CHEST_TRIGGERED);
            }
        }
        return true;
    }
    
    @Nullable
    public ILockableContainer getLockableContainer(final World worldIn, final BlockPos pos) {
        return this.getContainer(worldIn, pos, false);
    }
    
    @Nullable
    public ILockableContainer getContainer(final World worldIn, final BlockPos pos, final boolean allowBlocking) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityChest)) {
            return null;
        }
        ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
        if (!allowBlocking && this.isBlocked(worldIn, pos)) {
            return null;
        }
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset(enumfacing);
            final Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block == this) {
                if (this.isBlocked(worldIn, blockpos)) {
                    return null;
                }
                final TileEntity tileentity2 = worldIn.getTileEntity(blockpos);
                if (!(tileentity2 instanceof TileEntityChest)) {
                    continue;
                }
                if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
                    ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (ILockableContainer)tileentity2);
                }
                else {
                    ilockablecontainer = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileentity2, ilockablecontainer);
                }
            }
        }
        return ilockablecontainer;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityChest();
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return this.chestType == Type.TRAP;
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!blockState.canProvidePower()) {
            return 0;
        }
        int i = 0;
        final TileEntity tileentity = blockAccess.getTileEntity(pos);
        if (tileentity instanceof TileEntityChest) {
            i = ((TileEntityChest)tileentity).numPlayersUsing;
        }
        return MathHelper.clamp(i, 0, 15);
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return (side == EnumFacing.UP) ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
    
    private boolean isBlocked(final World worldIn, final BlockPos pos) {
        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }
    
    private boolean isBelowSolidBlock(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.up()).isNormalCube();
    }
    
    private boolean isOcelotSittingOnChest(final World worldIn, final BlockPos pos) {
        for (final Entity entity : worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            final EntityOcelot entityocelot = (EntityOcelot)entity;
            if (entityocelot.isSitting()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockChest.FACING).getIndex();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockChest.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockChest.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockChest.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockChest.FACING });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        NORTH_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0, 0.9375, 0.875, 0.9375);
        SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 1.0);
        WEST_CHEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
        EAST_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 1.0, 0.875, 0.9375);
        NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
    }
    
    public enum Type
    {
        BASIC, 
        TRAP;
    }
}
