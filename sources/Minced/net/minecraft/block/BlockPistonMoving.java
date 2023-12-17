// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;
    
    public BlockPistonMoving() {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, EnumFacing.NORTH).withProperty(BlockPistonMoving.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0f);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return null;
    }
    
    public static TileEntity createTilePiston(final IBlockState blockStateIn, final EnumFacing facingIn, final boolean extendingIn, final boolean shouldHeadBeRenderedIn) {
        return new TileEntityPiston(blockStateIn, facingIn, extendingIn, shouldHeadBeRenderedIn);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityPiston) {
            ((TileEntityPiston)tileentity).clearPistonTileEntity();
        }
        else {
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public void onPlayerDestroy(final World worldIn, final BlockPos pos, final IBlockState state) {
        final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING).getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() instanceof BlockPistonBase && iblockstate.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            worldIn.setBlockToAir(blockpos);
        }
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
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            final TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);
            if (tileentitypiston != null) {
                final IBlockState iblockstate = tileentitypiston.getPistonState();
                iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            }
        }
    }
    
    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(final IBlockState blockState, final World worldIn, final BlockPos pos, final Vec3d start, final Vec3d end) {
        return null;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            worldIn.getTileEntity(pos);
        }
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        final TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);
        return (tileentitypiston == null) ? null : tileentitypiston.getAABB(worldIn, pos);
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        final TileEntityPiston tileentitypiston = this.getTilePistonAt(worldIn, pos);
        if (tileentitypiston != null) {
            tileentitypiston.addCollissionAABBs(worldIn, pos, entityBox, collidingBoxes, entityIn);
        }
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final TileEntityPiston tileentitypiston = this.getTilePistonAt(source, pos);
        return (tileentitypiston != null) ? tileentitypiston.getAABB(source, pos) : BlockPistonMoving.FULL_BLOCK_AABB;
    }
    
    @Nullable
    private TileEntityPiston getTilePistonAt(final IBlockAccess iBlockAccessIn, final BlockPos blockPosIn) {
        final TileEntity tileentity = iBlockAccessIn.getTileEntity(blockPosIn);
        return (tileentity instanceof TileEntityPiston) ? ((TileEntityPiston)tileentity) : null;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, BlockPistonExtension.getFacing(meta)).withProperty(BlockPistonMoving.TYPE, ((meta & 0x8) > 0) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING)));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonMoving.FACING).getIndex();
        if (state.getValue(BlockPistonMoving.TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPistonMoving.FACING, BlockPistonMoving.TYPE });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockPistonExtension.FACING;
        TYPE = BlockPistonExtension.TYPE;
    }
}
