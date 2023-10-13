/*
 * Decompiled with CFR 0.150.
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
    public static final PropertyBool field_176293_a = PropertyBool.create("powered");
    public static final PropertyBool field_176290_b = PropertyBool.create("suspended");
    public static final PropertyBool field_176294_M = PropertyBool.create("attached");
    public static final PropertyBool field_176295_N = PropertyBool.create("disarmed");
    public static final PropertyBool field_176296_O = PropertyBool.create("north");
    public static final PropertyBool field_176291_P = PropertyBool.create("east");
    public static final PropertyBool field_176289_Q = PropertyBool.create("south");
    public static final PropertyBool field_176292_R = PropertyBool.create("west");
    private static final String __OBFID = "CL_00000328";

    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176293_a, Boolean.valueOf(false)).withProperty(field_176290_b, Boolean.valueOf(false)).withProperty(field_176294_M, Boolean.valueOf(false)).withProperty(field_176295_N, Boolean.valueOf(false)).withProperty(field_176296_O, Boolean.valueOf(false)).withProperty(field_176291_P, Boolean.valueOf(false)).withProperty(field_176289_Q, Boolean.valueOf(false)).withProperty(field_176292_R, Boolean.valueOf(false)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176296_O, Boolean.valueOf(BlockTripWire.func_176287_c(worldIn, pos, state, EnumFacing.NORTH))).withProperty(field_176291_P, Boolean.valueOf(BlockTripWire.func_176287_c(worldIn, pos, state, EnumFacing.EAST))).withProperty(field_176289_Q, Boolean.valueOf(BlockTripWire.func_176287_c(worldIn, pos, state, EnumFacing.SOUTH))).withProperty(field_176292_R, Boolean.valueOf(BlockTripWire.func_176287_c(worldIn, pos, state, EnumFacing.WEST)));
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
        boolean var6;
        boolean var5 = (Boolean)state.getValue(field_176290_b);
        boolean bl = var6 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
        if (var5 != var6) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        IBlockState var3 = access.getBlockState(pos);
        boolean var4 = (Boolean)var3.getValue(field_176294_M);
        boolean var5 = (Boolean)var3.getValue(field_176290_b);
        if (!var5) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
        } else if (!var4) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        state = state.withProperty(field_176290_b, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())));
        worldIn.setBlockState(pos, state, 3);
        this.func_176286_e(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.func_176286_e(worldIn, pos, state.withProperty(field_176293_a, Boolean.valueOf(true)));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            worldIn.setBlockState(pos, state.withProperty(field_176295_N, Boolean.valueOf(true)), 4);
        }
    }

    private void func_176286_e(World worldIn, BlockPos p_176286_2_, IBlockState p_176286_3_) {
        block0: for (EnumFacing var7 : new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST}) {
            for (int var8 = 1; var8 < 42; ++var8) {
                BlockPos var9 = p_176286_2_.offset(var7, var8);
                IBlockState var10 = worldIn.getBlockState(var9);
                if (var10.getBlock() == Blocks.tripwire_hook) {
                    if (var10.getValue(BlockTripWireHook.field_176264_a) != var7.getOpposite()) continue block0;
                    Blocks.tripwire_hook.func_176260_a(worldIn, var9, var10, false, true, var8, p_176286_3_);
                    continue block0;
                }
                if (var10.getBlock() != Blocks.tripwire) continue block0;
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && !((Boolean)state.getValue(field_176293_a)).booleanValue()) {
            this.func_176288_d(worldIn, pos);
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)worldIn.getBlockState(pos).getValue(field_176293_a)).booleanValue()) {
            this.func_176288_d(worldIn, pos);
        }
    }

    private void func_176288_d(World worldIn, BlockPos p_176288_2_) {
        IBlockState var3 = worldIn.getBlockState(p_176288_2_);
        boolean var4 = (Boolean)var3.getValue(field_176293_a);
        boolean var5 = false;
        List var6 = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((double)p_176288_2_.getX() + this.minX, (double)p_176288_2_.getY() + this.minY, (double)p_176288_2_.getZ() + this.minZ, (double)p_176288_2_.getX() + this.maxX, (double)p_176288_2_.getY() + this.maxY, (double)p_176288_2_.getZ() + this.maxZ));
        if (!var6.isEmpty()) {
            for (Entity var8 : var6) {
                if (var8.doesEntityNotTriggerPressurePlate()) continue;
                var5 = true;
                break;
            }
        }
        if (var5 != var4) {
            var3 = var3.withProperty(field_176293_a, Boolean.valueOf(var5));
            worldIn.setBlockState(p_176288_2_, var3, 3);
            this.func_176286_e(worldIn, p_176288_2_, var3);
        }
        if (var5) {
            worldIn.scheduleUpdate(p_176288_2_, this, this.tickRate(worldIn));
        }
    }

    public static boolean func_176287_c(IBlockAccess p_176287_0_, BlockPos p_176287_1_, IBlockState p_176287_2_, EnumFacing p_176287_3_) {
        BlockPos var4 = p_176287_1_.offset(p_176287_3_);
        IBlockState var5 = p_176287_0_.getBlockState(var4);
        Block var6 = var5.getBlock();
        if (var6 == Blocks.tripwire_hook) {
            EnumFacing var9 = p_176287_3_.getOpposite();
            return var5.getValue(BlockTripWireHook.field_176264_a) == var9;
        }
        if (var6 == Blocks.tripwire) {
            boolean var8;
            boolean var7 = (Boolean)p_176287_2_.getValue(field_176290_b);
            return var7 == (var8 = ((Boolean)var5.getValue(field_176290_b)).booleanValue());
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176293_a, Boolean.valueOf((meta & 1) > 0)).withProperty(field_176290_b, Boolean.valueOf((meta & 2) > 0)).withProperty(field_176294_M, Boolean.valueOf((meta & 4) > 0)).withProperty(field_176295_N, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        if (((Boolean)state.getValue(field_176293_a)).booleanValue()) {
            var2 |= 1;
        }
        if (((Boolean)state.getValue(field_176290_b)).booleanValue()) {
            var2 |= 2;
        }
        if (((Boolean)state.getValue(field_176294_M)).booleanValue()) {
            var2 |= 4;
        }
        if (((Boolean)state.getValue(field_176295_N)).booleanValue()) {
            var2 |= 8;
        }
        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176293_a, field_176290_b, field_176294_M, field_176295_N, field_176296_O, field_176291_P, field_176292_R, field_176289_Q);
    }
}

