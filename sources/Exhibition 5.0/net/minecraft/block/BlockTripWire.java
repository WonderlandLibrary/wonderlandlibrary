// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockTripWire extends Block
{
    public static final PropertyBool field_176293_a;
    public static final PropertyBool field_176290_b;
    public static final PropertyBool field_176294_M;
    public static final PropertyBool field_176295_N;
    public static final PropertyBool field_176296_O;
    public static final PropertyBool field_176291_P;
    public static final PropertyBool field_176289_Q;
    public static final PropertyBool field_176292_R;
    private static final String __OBFID = "CL_00000328";
    
    public BlockTripWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTripWire.field_176293_a, false).withProperty(BlockTripWire.field_176290_b, false).withProperty(BlockTripWire.field_176294_M, false).withProperty(BlockTripWire.field_176295_N, false).withProperty(BlockTripWire.field_176296_O, false).withProperty(BlockTripWire.field_176291_P, false).withProperty(BlockTripWire.field_176289_Q, false).withProperty(BlockTripWire.field_176292_R, false));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.15625f, 1.0f);
        this.setTickRandomly(true);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockTripWire.field_176296_O, func_176287_c(worldIn, pos, state, EnumFacing.NORTH)).withProperty(BlockTripWire.field_176291_P, func_176287_c(worldIn, pos, state, EnumFacing.EAST)).withProperty(BlockTripWire.field_176289_Q, func_176287_c(worldIn, pos, state, EnumFacing.SOUTH)).withProperty(BlockTripWire.field_176292_R, func_176287_c(worldIn, pos, state, EnumFacing.WEST));
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
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
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.string;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.string;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final boolean var5 = (boolean)state.getValue(BlockTripWire.field_176290_b);
        final boolean var6 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
        if (var5 != var6) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        final boolean var4 = (boolean)var3.getValue(BlockTripWire.field_176294_M);
        final boolean var5 = (boolean)var3.getValue(BlockTripWire.field_176290_b);
        if (!var5) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.09375f, 1.0f);
        }
        else if (!var4) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0625f, 0.0f, 1.0f, 0.15625f, 1.0f);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, IBlockState state) {
        state = state.withProperty(BlockTripWire.field_176290_b, !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()));
        worldIn.setBlockState(pos, state, 3);
        this.func_176286_e(worldIn, pos, state);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_176286_e(worldIn, pos, state.withProperty(BlockTripWire.field_176293_a, true));
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            worldIn.setBlockState(pos, state.withProperty(BlockTripWire.field_176295_N, true), 4);
        }
    }
    
    private void func_176286_e(final World worldIn, final BlockPos p_176286_2_, final IBlockState p_176286_3_) {
        for (final EnumFacing var7 : new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }) {
            int var8 = 1;
            while (var8 < 42) {
                final BlockPos var9 = p_176286_2_.offset(var7, var8);
                final IBlockState var10 = worldIn.getBlockState(var9);
                if (var10.getBlock() == Blocks.tripwire_hook) {
                    if (var10.getValue(BlockTripWireHook.field_176264_a) == var7.getOpposite()) {
                        Blocks.tripwire_hook.func_176260_a(worldIn, var9, var10, false, true, var8, p_176286_3_);
                        break;
                    }
                    break;
                }
                else {
                    if (var10.getBlock() != Blocks.tripwire) {
                        break;
                    }
                    ++var8;
                }
            }
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !(boolean)state.getValue(BlockTripWire.field_176293_a)) {
            this.func_176288_d(worldIn, pos);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && (boolean)worldIn.getBlockState(pos).getValue(BlockTripWire.field_176293_a)) {
            this.func_176288_d(worldIn, pos);
        }
    }
    
    private void func_176288_d(final World worldIn, final BlockPos p_176288_2_) {
        IBlockState var3 = worldIn.getBlockState(p_176288_2_);
        final boolean var4 = (boolean)var3.getValue(BlockTripWire.field_176293_a);
        boolean var5 = false;
        final List var6 = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(p_176288_2_.getX() + this.minX, p_176288_2_.getY() + this.minY, p_176288_2_.getZ() + this.minZ, p_176288_2_.getX() + this.maxX, p_176288_2_.getY() + this.maxY, p_176288_2_.getZ() + this.maxZ));
        if (!var6.isEmpty()) {
            for (final Entity var8 : var6) {
                if (!var8.doesEntityNotTriggerPressurePlate()) {
                    var5 = true;
                    break;
                }
            }
        }
        if (var5 != var4) {
            var3 = var3.withProperty(BlockTripWire.field_176293_a, var5);
            worldIn.setBlockState(p_176288_2_, var3, 3);
            this.func_176286_e(worldIn, p_176288_2_, var3);
        }
        if (var5) {
            worldIn.scheduleUpdate(p_176288_2_, this, this.tickRate(worldIn));
        }
    }
    
    public static boolean func_176287_c(final IBlockAccess p_176287_0_, final BlockPos p_176287_1_, final IBlockState p_176287_2_, final EnumFacing p_176287_3_) {
        final BlockPos var4 = p_176287_1_.offset(p_176287_3_);
        final IBlockState var5 = p_176287_0_.getBlockState(var4);
        final Block var6 = var5.getBlock();
        if (var6 == Blocks.tripwire_hook) {
            final EnumFacing var7 = p_176287_3_.getOpposite();
            return var5.getValue(BlockTripWireHook.field_176264_a) == var7;
        }
        if (var6 == Blocks.tripwire) {
            final boolean var8 = (boolean)p_176287_2_.getValue(BlockTripWire.field_176290_b);
            final boolean var9 = (boolean)var5.getValue(BlockTripWire.field_176290_b);
            return var8 == var9;
        }
        return false;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTripWire.field_176293_a, (meta & 0x1) > 0).withProperty(BlockTripWire.field_176290_b, (meta & 0x2) > 0).withProperty(BlockTripWire.field_176294_M, (meta & 0x4) > 0).withProperty(BlockTripWire.field_176295_N, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        if (state.getValue(BlockTripWire.field_176293_a)) {
            var2 |= 0x1;
        }
        if (state.getValue(BlockTripWire.field_176290_b)) {
            var2 |= 0x2;
        }
        if (state.getValue(BlockTripWire.field_176294_M)) {
            var2 |= 0x4;
        }
        if (state.getValue(BlockTripWire.field_176295_N)) {
            var2 |= 0x8;
        }
        return var2;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTripWire.field_176293_a, BlockTripWire.field_176290_b, BlockTripWire.field_176294_M, BlockTripWire.field_176295_N, BlockTripWire.field_176296_O, BlockTripWire.field_176291_P, BlockTripWire.field_176292_R, BlockTripWire.field_176289_Q });
    }
    
    static {
        field_176293_a = PropertyBool.create("powered");
        field_176290_b = PropertyBool.create("suspended");
        field_176294_M = PropertyBool.create("attached");
        field_176295_N = PropertyBool.create("disarmed");
        field_176296_O = PropertyBool.create("north");
        field_176291_P = PropertyBool.create("east");
        field_176289_Q = PropertyBool.create("south");
        field_176292_R = PropertyBool.create("west");
    }
}
