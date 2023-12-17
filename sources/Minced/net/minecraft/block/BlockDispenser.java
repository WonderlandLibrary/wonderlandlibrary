// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.inventory.Container;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.dispenser.IPosition;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockDispenser extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyBool TRIGGERED;
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> DISPENSE_BEHAVIOR_REGISTRY;
    protected Random rand;
    
    protected BlockDispenser() {
        super(Material.ROCK);
        this.rand = new Random();
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockDispenser.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 4;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }
    
    private void setDefaultDirection(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
            final boolean flag = worldIn.getBlockState(pos.north()).isFullBlock();
            final boolean flag2 = worldIn.getBlockState(pos.south()).isFullBlock();
            if (enumfacing == EnumFacing.NORTH && flag && !flag2) {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && flag2 && !flag) {
                enumfacing = EnumFacing.NORTH;
            }
            else {
                final boolean flag3 = worldIn.getBlockState(pos.west()).isFullBlock();
                final boolean flag4 = worldIn.getBlockState(pos.east()).isFullBlock();
                if (enumfacing == EnumFacing.WEST && flag3 && !flag4) {
                    enumfacing = EnumFacing.EAST;
                }
                else if (enumfacing == EnumFacing.EAST && flag4 && !flag3) {
                    enumfacing = EnumFacing.WEST;
                }
            }
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockDispenser.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, false), 2);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityDispenser) {
            playerIn.displayGUIChest((IInventory)tileentity);
            if (tileentity instanceof TileEntityDropper) {
                playerIn.addStat(StatList.DROPPER_INSPECTED);
            }
            else {
                playerIn.addStat(StatList.DISPENSER_INSPECTED);
            }
        }
        return true;
    }
    
    protected void dispense(final World worldIn, final BlockPos pos) {
        final BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
        final TileEntityDispenser tileentitydispenser = blocksourceimpl.getBlockTileEntity();
        if (tileentitydispenser != null) {
            final int i = tileentitydispenser.getDispenseSlot();
            if (i < 0) {
                worldIn.playEvent(1001, pos, 0);
            }
            else {
                final ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                final IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
                if (ibehaviordispenseitem != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
                    tileentitydispenser.setInventorySlotContents(i, ibehaviordispenseitem.dispense(blocksourceimpl, itemstack));
                }
            }
        }
    }
    
    protected IBehaviorDispenseItem getBehavior(final ItemStack stack) {
        return BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        final boolean flag2 = state.getValue((IProperty<Boolean>)BlockDispenser.TRIGGERED);
        if (flag && !flag2) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, true), 4);
        }
        else if (!flag && flag2) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, false), 4);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            this.dispense(worldIn, pos);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDispenser();
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockDispenser.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, false);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockDispenser.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityDispenser) {
                ((TileEntityDispenser)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    public static IPosition getDispensePosition(final IBlockSource coords) {
        final EnumFacing enumfacing = coords.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
        final double d0 = coords.getX() + 0.7 * enumfacing.getXOffset();
        final double d2 = coords.getY() + 0.7 * enumfacing.getYOffset();
        final double d3 = coords.getZ() + 0.7 * enumfacing.getZOffset();
        return new PositionImpl(d0, d2, d3);
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockDispenser.FACING, EnumFacing.byIndex(meta & 0x7)).withProperty((IProperty<Comparable>)BlockDispenser.TRIGGERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockDispenser.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockDispenser.TRIGGERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockDispenser.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockDispenser.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockDispenser.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockDispenser.FACING, BlockDispenser.TRIGGERED });
    }
    
    static {
        FACING = BlockDirectional.FACING;
        TRIGGERED = PropertyBool.create("triggered");
        DISPENSE_BEHAVIOR_REGISTRY = new RegistryDefaulted<Item, IBehaviorDispenseItem>(new BehaviorDefaultDispenseItem());
    }
}
