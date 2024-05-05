package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    public static final PropertyBool SUSPENDED = PropertyBool.create((String)"suspended");
    public static final PropertyBool ATTACHED = PropertyBool.create((String)"attached");
    public static final PropertyBool DISARMED = PropertyBool.create((String)"disarmed");
    public static final PropertyBool NORTH = PropertyBool.create((String)"north");
    public static final PropertyBool EAST = PropertyBool.create((String)"east");
    public static final PropertyBool SOUTH = PropertyBool.create((String)"south");
    public static final PropertyBool WEST = PropertyBool.create((String)"west");

    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)DISARMED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)false)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty((IProperty)NORTH, (Comparable)Boolean.valueOf((boolean)BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty((IProperty)EAST, (Comparable)Boolean.valueOf((boolean)BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty((IProperty)SOUTH, (Comparable)Boolean.valueOf((boolean)BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty((IProperty)WEST, (Comparable)Boolean.valueOf((boolean)BlockTripWire.isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.string;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.string;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean flag1;
        boolean flag = (Boolean)state.getValue((IProperty)SUSPENDED);
        boolean bl = flag1 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
        if (flag != flag1) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = (Boolean)iblockstate.getValue((IProperty)ATTACHED);
        boolean flag1 = (Boolean)iblockstate.getValue((IProperty)SUSPENDED);
        if (!flag1) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
        } else if (!flag) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        state = state.withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf((!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) ? 1 : 0) != 0));
        worldIn.setBlockState(pos, state, 3);
        this.notifyHook(worldIn, pos, state);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.notifyHook(worldIn, pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)));
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)DISARMED, (Comparable)Boolean.valueOf((boolean)true)), 4);
        }
    }

    private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
        block0: for (EnumFacing enumfacing : new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST}) {
            for (int i = 1; i < 42; ++i) {
                BlockPos blockpos = pos.offset(enumfacing, i);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                    if (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) != enumfacing.getOpposite()) continue block0;
                    Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state);
                    continue block0;
                }
                if (iblockstate.getBlock() != Blocks.tripwire) continue block0;
            }
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            this.updateState(worldIn, pos);
        }
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue()) {
            this.updateState(worldIn, pos);
        }
    }

    private void updateState(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = (Boolean)iblockstate.getValue((IProperty)POWERED);
        boolean flag1 = false;
        List list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.doesEntityNotTriggerPressurePlate()) continue;
                flag1 = true;
                break;
            }
        }
        if (flag1 != flag) {
            iblockstate = iblockstate.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)flag1));
            worldIn.setBlockState(pos, iblockstate, 3);
            this.notifyHook(worldIn, pos, iblockstate);
        }
        if (flag1) {
            worldIn.scheduleUpdate(pos, (Block)this, this.tickRate(worldIn));
        }
    }

    public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.tripwire_hook) {
            EnumFacing enumfacing = direction.getOpposite();
            return iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing;
        }
        if (block == Blocks.tripwire) {
            boolean flag1;
            boolean flag = (Boolean)state.getValue((IProperty)SUSPENDED);
            return flag == (flag1 = ((Boolean)iblockstate.getValue((IProperty)SUSPENDED)).booleanValue());
        }
        return false;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 1) > 0 ? 1 : 0) != 0)).withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf(((meta & 2) > 0 ? 1 : 0) != 0)).withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf(((meta & 4) > 0 ? 1 : 0) != 0)).withProperty((IProperty)DISARMED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 1;
        }
        if (((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue()) {
            i |= 2;
        }
        if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue()) {
            i |= 4;
        }
        if (((Boolean)state.getValue((IProperty)DISARMED)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH});
    }
}
