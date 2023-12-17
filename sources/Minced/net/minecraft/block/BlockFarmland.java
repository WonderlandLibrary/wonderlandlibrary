// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFarmland extends Block
{
    public static final PropertyInteger MOISTURE;
    protected static final AxisAlignedBB FARMLAND_AABB;
    protected static final AxisAlignedBB field_194405_c;
    
    protected BlockFarmland() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, 0));
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockFarmland.FARMLAND_AABB;
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
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final int i = state.getValue((IProperty<Integer>)BlockFarmland.MOISTURE);
        if (!this.hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, i - 1), 2);
            }
            else if (!this.hasCrops(worldIn, pos)) {
                turnToDirt(worldIn, pos);
            }
        }
        else if (i < 7) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, 7), 2);
        }
    }
    
    @Override
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f && entityIn instanceof EntityLivingBase && (entityIn instanceof EntityPlayer || worldIn.getGameRules().getBoolean("mobGriefing")) && entityIn.width * entityIn.width * entityIn.height > 0.512f) {
            turnToDirt(worldIn, pos);
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }
    
    protected static void turnToDirt(final World p_190970_0_, final BlockPos worldIn) {
        p_190970_0_.setBlockState(worldIn, Blocks.DIRT.getDefaultState());
        final AxisAlignedBB axisalignedbb = BlockFarmland.field_194405_c.offset(worldIn);
        for (final Entity entity : p_190970_0_.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb)) {
            final double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getEntityBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001, entity.posZ);
        }
    }
    
    private boolean hasCrops(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof BlockCrops || block instanceof BlockStem;
    }
    
    private boolean hasWater(final World worldIn, final BlockPos pos) {
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToDirt(worldIn, pos);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToDirt(worldIn, pos);
        }
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        switch (side) {
            case UP: {
                return true;
            }
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST: {
                final IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                final Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH;
            }
            default: {
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, meta & 0x7);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFarmland.MOISTURE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFarmland.MOISTURE });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        MOISTURE = PropertyInteger.create("moisture", 0, 7);
        FARMLAND_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0);
        field_194405_c = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    }
}
