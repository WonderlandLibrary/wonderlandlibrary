package net.minecraft.block;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    public static final PropertyBool ATTACHED = PropertyBool.create((String)"attached");
    public static final PropertyBool SUSPENDED = PropertyBool.create((String)"suspended");

    public BlockTripWireHook() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf((!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) ? 1 : 0) != 0));
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

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!worldIn.getBlockState(pos.offset(enumfacing)).getBlock().isNormalCube()) continue;
            return true;
        }
        return false;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)SUSPENDED, (Comparable)Boolean.valueOf((boolean)false));
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing);
        }
        return iblockstate;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.func_176260_a(worldIn, pos, state, false, false, -1, null);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing;
        if (neighborBlock != this && this.checkForDrop(worldIn, pos, state) && !worldIn.getBlockState(pos.offset((enumfacing = (EnumFacing)state.getValue((IProperty)FACING)).getOpposite())).getBlock().isNormalCube()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
        EnumFacing enumfacing = (EnumFacing)hookState.getValue((IProperty)FACING);
        boolean flag = (Boolean)hookState.getValue((IProperty)ATTACHED);
        boolean flag1 = (Boolean)hookState.getValue((IProperty)POWERED);
        boolean flag2 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
        boolean flag3 = !p_176260_4_;
        boolean flag4 = false;
        int i = 0;
        IBlockState[] aiblockstate = new IBlockState[42];
        for (int j = 1; j < 42; ++j) {
            BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                if (iblockstate.getValue((IProperty)FACING) != enumfacing.getOpposite()) break;
                i = j;
                break;
            }
            if (iblockstate.getBlock() != Blocks.tripwire && j != p_176260_6_) {
                aiblockstate[j] = null;
                flag3 = false;
                continue;
            }
            if (j == p_176260_6_) {
                iblockstate = (IBlockState)Objects.firstNonNull((Object)p_176260_7_, (Object)iblockstate);
            }
            boolean flag5 = (Boolean)iblockstate.getValue((IProperty)BlockTripWire.DISARMED) == false;
            boolean flag6 = (Boolean)iblockstate.getValue((IProperty)BlockTripWire.POWERED);
            boolean flag7 = (Boolean)iblockstate.getValue((IProperty)BlockTripWire.SUSPENDED);
            flag3 &= flag7 == flag2;
            flag4 |= flag5 && flag6;
            aiblockstate[j] = iblockstate;
            if (j != p_176260_6_) continue;
            worldIn.scheduleUpdate(pos, (Block)this, this.tickRate(worldIn));
            flag3 &= flag5;
        }
        IBlockState iblockstate1 = this.getDefaultState().withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf((boolean)flag3)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)(flag4 &= (flag3 &= i > 1))));
        if (i > 0) {
            BlockPos blockpos1 = pos.offset(enumfacing, i);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos1, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing1), 3);
            this.func_176262_b(worldIn, blockpos1, enumfacing1);
            this.func_180694_a(worldIn, blockpos1, flag3, flag4, flag, flag1);
        }
        this.func_180694_a(worldIn, pos, flag3, flag4, flag, flag1);
        if (!p_176260_4_) {
            worldIn.setBlockState(pos, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing), 3);
            if (p_176260_5_) {
                this.func_176262_b(worldIn, pos, enumfacing);
            }
        }
        if (flag != flag3) {
            for (int k = 1; k < i; ++k) {
                BlockPos blockpos2 = pos.offset(enumfacing, k);
                IBlockState iblockstate2 = aiblockstate[k];
                if (iblockstate2 == null || worldIn.getBlockState(blockpos2).getBlock() == Blocks.air) continue;
                worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf((boolean)flag3)), 3);
            }
        }
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.func_176260_a(worldIn, pos, state, false, true, -1, null);
    }

    private void func_180694_a(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.6f);
        } else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.5f);
        } else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.7f);
        } else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.bowhit", 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }

    private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, (Block)this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), (Block)this);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        float f = 0.1875f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = (Boolean)state.getValue((IProperty)ATTACHED);
        boolean flag1 = (Boolean)state.getValue((IProperty)POWERED);
        if (flag || flag1) {
            this.func_176260_a(worldIn, pos, state, true, false, -1, null);
        }
        if (flag1) {
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite()), (Block)this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) != false ? 15 : 0;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) == false ? 0 : (state.getValue((IProperty)FACING) == side ? 15 : 0);
    }

    public boolean canProvidePower() {
        return true;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)(meta & 3))).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0)).withProperty((IProperty)ATTACHED, (Comparable)Boolean.valueOf(((meta & 4) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue()) {
            i |= 4;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, POWERED, ATTACHED, SUSPENDED});
    }
}
