package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing");
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    private final boolean wooden;

    protected BlockButton(boolean wooden) {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public int tickRate(World worldIn) {
        return this.wooden ? 30 : 20;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return BlockButton.func_181088_a(worldIn, pos, side.getOpposite());
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (!BlockButton.func_181088_a(worldIn, pos, enumfacing)) continue;
            return true;
        }
        return false;
    }

    protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_) {
        BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
        return p_181088_2_ == EnumFacing.DOWN ? World.doesBlockHaveSolidTopSurface((IBlockAccess)p_181088_0_, (BlockPos)blockpos) : p_181088_0_.getBlockState(blockpos).getBlock().isNormalCube();
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return BlockButton.func_181088_a(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)) : this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.DOWN).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false));
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (this.checkForDrop(worldIn, pos, state) && !BlockButton.func_181088_a(worldIn, pos, ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (this.canPlaceBlockAt(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.updateBlockBounds(worldIn.getBlockState(pos));
    }

    private void updateBlockBounds(IBlockState state) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        boolean flag = (Boolean)state.getValue((IProperty)POWERED);
        float f = 0.25f;
        float f1 = 0.375f;
        float f2 = (float)(flag ? 1 : 2) / 16.0f;
        float f3 = 0.125f;
        float f4 = 0.1875f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.375f, 0.3125f, f2, 0.625f, 0.6875f);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - f2, 0.375f, 0.3125f, 1.0f, 0.625f, 0.6875f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.3125f, 0.375f, 0.0f, 0.6875f, 0.625f, f2);
                break;
            }
            case 4: {
                this.setBlockBounds(0.3125f, 0.375f, 1.0f - f2, 0.6875f, 0.625f, 1.0f);
                break;
            }
            case 5: {
                this.setBlockBounds(0.3125f, 0.0f, 0.375f, 0.6875f, 0.0f + f2, 0.625f);
                break;
            }
            case 6: {
                this.setBlockBounds(0.3125f, 1.0f - f2, 0.375f, 0.6875f, 1.0f, 0.625f);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
        worldIn.scheduleUpdate(pos, (Block)this, this.tickRate(worldIn));
        return true;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
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

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            if (this.wooden) {
                this.checkForArrows(worldIn, pos, state);
            } else {
                worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)));
                this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
                worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }

    public void setBlockBoundsForItemRender() {
        float f = 0.1875f;
        float f1 = 0.125f;
        float f2 = 0.125f;
        this.setBlockBounds(0.5f - f, 0.5f - f1, 0.5f - f2, 0.5f + f, 0.5f + f1, 0.5f + f2);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && this.wooden && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            this.checkForArrows(worldIn, pos, state);
        }
    }

    private void checkForArrows(World worldIn, BlockPos pos, IBlockState state) {
        this.updateBlockBounds(state);
        List list = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
        boolean flag = !list.isEmpty();
        boolean flag1 = (Boolean)state.getValue((IProperty)POWERED);
        if (flag && !flag1) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)));
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!flag && flag1) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)));
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (flag) {
            worldIn.scheduleUpdate(pos, (Block)this, this.tickRate(worldIn));
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing) {
        worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), (Block)this);
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing;
        switch (meta & 7) {
            case 0: {
                enumfacing = EnumFacing.DOWN;
                break;
            }
            case 1: {
                enumfacing = EnumFacing.EAST;
                break;
            }
            case 2: {
                enumfacing = EnumFacing.WEST;
                break;
            }
            case 3: {
                enumfacing = EnumFacing.SOUTH;
                break;
            }
            case 4: {
                enumfacing = EnumFacing.NORTH;
                break;
            }
            default: {
                enumfacing = EnumFacing.UP;
            }
        }
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[((EnumFacing)state.getValue((IProperty)FACING)).ordinal()]) {
            case 1: {
                i = 1;
                break;
            }
            case 2: {
                i = 2;
                break;
            }
            case 3: {
                i = 3;
                break;
            }
            case 4: {
                i = 4;
                break;
            }
            default: {
                i = 5;
                break;
            }
            case 6: {
                i = 0;
            }
        }
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, POWERED});
    }
}
