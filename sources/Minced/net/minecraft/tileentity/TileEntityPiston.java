// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

public class TileEntityPiston extends TileEntity implements ITickable
{
    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private static final ThreadLocal<EnumFacing> MOVING_ENTITY;
    private float progress;
    private float lastProgress;
    
    public TileEntityPiston() {
    }
    
    public TileEntityPiston(final IBlockState pistonStateIn, final EnumFacing pistonFacingIn, final boolean extendingIn, final boolean shouldHeadBeRenderedIn) {
        this.pistonState = pistonStateIn;
        this.pistonFacing = pistonFacingIn;
        this.extending = extendingIn;
        this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
    }
    
    public IBlockState getPistonState() {
        return this.pistonState;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public int getBlockMetadata() {
        return 0;
    }
    
    public boolean isExtending() {
        return this.extending;
    }
    
    public EnumFacing getFacing() {
        return this.pistonFacing;
    }
    
    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }
    
    public float getProgress(float ticks) {
        if (ticks > 1.0f) {
            ticks = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * ticks;
    }
    
    public float getOffsetX(final float ticks) {
        return this.pistonFacing.getXOffset() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    public float getOffsetY(final float ticks) {
        return this.pistonFacing.getYOffset() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    public float getOffsetZ(final float ticks) {
        return this.pistonFacing.getZOffset() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    private float getExtendedProgress(final float p_184320_1_) {
        return this.extending ? (p_184320_1_ - 1.0f) : (1.0f - p_184320_1_);
    }
    
    public AxisAlignedBB getAABB(final IBlockAccess p_184321_1_, final BlockPos p_184321_2_) {
        return this.getAABB(p_184321_1_, p_184321_2_, this.progress).union(this.getAABB(p_184321_1_, p_184321_2_, this.lastProgress));
    }
    
    public AxisAlignedBB getAABB(final IBlockAccess p_184319_1_, final BlockPos p_184319_2_, float p_184319_3_) {
        p_184319_3_ = this.getExtendedProgress(p_184319_3_);
        final IBlockState iblockstate = this.getCollisionRelatedBlockState();
        return iblockstate.getBoundingBox(p_184319_1_, p_184319_2_).offset(p_184319_3_ * this.pistonFacing.getXOffset(), p_184319_3_ * this.pistonFacing.getYOffset(), p_184319_3_ * this.pistonFacing.getZOffset());
    }
    
    private IBlockState getCollisionRelatedBlockState() {
        return (!this.isExtending() && this.shouldPistonHeadBeRendered()) ? Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, (this.pistonState.getBlock() == Blocks.STICKY_PISTON) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT).withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, (Comparable)this.pistonState.getValue((IProperty<V>)BlockPistonBase.FACING)) : this.pistonState;
    }
    
    private void moveCollidedEntities(final float p_184322_1_) {
        final EnumFacing enumfacing = this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();
        final double d0 = p_184322_1_ - this.progress;
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        this.getCollisionRelatedBlockState().addCollisionBoxToList(this.world, BlockPos.ORIGIN, new AxisAlignedBB(BlockPos.ORIGIN), list, null, true);
        if (!list.isEmpty()) {
            final AxisAlignedBB axisalignedbb = this.moveByPositionAndProgress(this.getMinMaxPiecesAABB(list));
            final List<Entity> list2 = this.world.getEntitiesWithinAABBExcludingEntity(null, this.getMovementArea(axisalignedbb, enumfacing, d0).union(axisalignedbb));
            if (!list2.isEmpty()) {
                final boolean flag = this.pistonState.getBlock() == Blocks.SLIME_BLOCK;
                for (int i = 0; i < list2.size(); ++i) {
                    final Entity entity = list2.get(i);
                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        if (flag) {
                            switch (enumfacing.getAxis()) {
                                case X: {
                                    entity.motionX = enumfacing.getXOffset();
                                    break;
                                }
                                case Y: {
                                    entity.motionY = enumfacing.getYOffset();
                                    break;
                                }
                                case Z: {
                                    entity.motionZ = enumfacing.getZOffset();
                                    break;
                                }
                            }
                        }
                        double d2 = 0.0;
                        for (int j = 0; j < list.size(); ++j) {
                            final AxisAlignedBB axisalignedbb2 = this.getMovementArea(this.moveByPositionAndProgress(list.get(j)), enumfacing, d0);
                            final AxisAlignedBB axisalignedbb3 = entity.getEntityBoundingBox();
                            if (axisalignedbb2.intersects(axisalignedbb3)) {
                                d2 = Math.max(d2, this.getMovement(axisalignedbb2, enumfacing, axisalignedbb3));
                                if (d2 >= d0) {
                                    break;
                                }
                            }
                        }
                        if (d2 > 0.0) {
                            d2 = Math.min(d2, d0) + 0.01;
                            TileEntityPiston.MOVING_ENTITY.set(enumfacing);
                            entity.move(MoverType.PISTON, d2 * enumfacing.getXOffset(), d2 * enumfacing.getYOffset(), d2 * enumfacing.getZOffset());
                            TileEntityPiston.MOVING_ENTITY.set(null);
                            if (!this.extending && this.shouldHeadBeRendered) {
                                this.fixEntityWithinPistonBase(entity, enumfacing, d0);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private AxisAlignedBB getMinMaxPiecesAABB(final List<AxisAlignedBB> p_191515_1_) {
        double d0 = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 1.0;
        double d5 = 1.0;
        double d6 = 1.0;
        for (final AxisAlignedBB axisalignedbb : p_191515_1_) {
            d0 = Math.min(axisalignedbb.minX, d0);
            d2 = Math.min(axisalignedbb.minY, d2);
            d3 = Math.min(axisalignedbb.minZ, d3);
            d4 = Math.max(axisalignedbb.maxX, d4);
            d5 = Math.max(axisalignedbb.maxY, d5);
            d6 = Math.max(axisalignedbb.maxZ, d6);
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    private double getMovement(final AxisAlignedBB p_190612_1_, final EnumFacing facing, final AxisAlignedBB p_190612_3_) {
        switch (facing.getAxis()) {
            case X: {
                return getDeltaX(p_190612_1_, facing, p_190612_3_);
            }
            default: {
                return getDeltaY(p_190612_1_, facing, p_190612_3_);
            }
            case Z: {
                return getDeltaZ(p_190612_1_, facing, p_190612_3_);
            }
        }
    }
    
    private AxisAlignedBB moveByPositionAndProgress(final AxisAlignedBB p_190607_1_) {
        final double d0 = this.getExtendedProgress(this.progress);
        return p_190607_1_.offset(this.pos.getX() + d0 * this.pistonFacing.getXOffset(), this.pos.getY() + d0 * this.pistonFacing.getYOffset(), this.pos.getZ() + d0 * this.pistonFacing.getZOffset());
    }
    
    private AxisAlignedBB getMovementArea(final AxisAlignedBB p_190610_1_, final EnumFacing p_190610_2_, final double p_190610_3_) {
        final double d0 = p_190610_3_ * p_190610_2_.getAxisDirection().getOffset();
        final double d2 = Math.min(d0, 0.0);
        final double d3 = Math.max(d0, 0.0);
        switch (p_190610_2_) {
            case WEST: {
                return new AxisAlignedBB(p_190610_1_.minX + d2, p_190610_1_.minY, p_190610_1_.minZ, p_190610_1_.minX + d3, p_190610_1_.maxY, p_190610_1_.maxZ);
            }
            case EAST: {
                return new AxisAlignedBB(p_190610_1_.maxX + d2, p_190610_1_.minY, p_190610_1_.minZ, p_190610_1_.maxX + d3, p_190610_1_.maxY, p_190610_1_.maxZ);
            }
            case DOWN: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY + d2, p_190610_1_.minZ, p_190610_1_.maxX, p_190610_1_.minY + d3, p_190610_1_.maxZ);
            }
            default: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.maxY + d2, p_190610_1_.minZ, p_190610_1_.maxX, p_190610_1_.maxY + d3, p_190610_1_.maxZ);
            }
            case NORTH: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY, p_190610_1_.minZ + d2, p_190610_1_.maxX, p_190610_1_.maxY, p_190610_1_.minZ + d3);
            }
            case SOUTH: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY, p_190610_1_.maxZ + d2, p_190610_1_.maxX, p_190610_1_.maxY, p_190610_1_.maxZ + d3);
            }
        }
    }
    
    private void fixEntityWithinPistonBase(final Entity p_190605_1_, final EnumFacing p_190605_2_, final double p_190605_3_) {
        final AxisAlignedBB axisalignedbb = p_190605_1_.getEntityBoundingBox();
        final AxisAlignedBB axisalignedbb2 = Block.FULL_BLOCK_AABB.offset(this.pos);
        if (axisalignedbb.intersects(axisalignedbb2)) {
            final EnumFacing enumfacing = p_190605_2_.getOpposite();
            double d0 = this.getMovement(axisalignedbb2, enumfacing, axisalignedbb) + 0.01;
            final double d2 = this.getMovement(axisalignedbb2, enumfacing, axisalignedbb.intersect(axisalignedbb2)) + 0.01;
            if (Math.abs(d0 - d2) < 0.01) {
                d0 = Math.min(d0, p_190605_3_) + 0.01;
                TileEntityPiston.MOVING_ENTITY.set(p_190605_2_);
                p_190605_1_.move(MoverType.PISTON, d0 * enumfacing.getXOffset(), d0 * enumfacing.getYOffset(), d0 * enumfacing.getZOffset());
                TileEntityPiston.MOVING_ENTITY.set(null);
            }
        }
    }
    
    private static double getDeltaX(final AxisAlignedBB p_190611_0_, final EnumFacing facing, final AxisAlignedBB p_190611_2_) {
        return (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190611_0_.maxX - p_190611_2_.minX) : (p_190611_2_.maxX - p_190611_0_.minX);
    }
    
    private static double getDeltaY(final AxisAlignedBB p_190608_0_, final EnumFacing facing, final AxisAlignedBB p_190608_2_) {
        return (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190608_0_.maxY - p_190608_2_.minY) : (p_190608_2_.maxY - p_190608_0_.minY);
    }
    
    private static double getDeltaZ(final AxisAlignedBB p_190604_0_, final EnumFacing facing, final AxisAlignedBB p_190604_2_) {
        return (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190604_0_.maxZ - p_190604_2_.minZ) : (p_190604_2_.maxZ - p_190604_0_.minZ);
    }
    
    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0f && this.world != null) {
            this.progress = 1.0f;
            this.lastProgress = this.progress;
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
            }
        }
    }
    
    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
            }
        }
        else {
            final float f = this.progress + 0.5f;
            this.moveCollidedEntities(f);
            this.progress = f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
        }
    }
    
    public static void registerFixesPiston(final DataFixer fixer) {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.byIndex(compound.getInteger("facing"));
        this.progress = compound.getFloat("progress");
        this.lastProgress = this.progress;
        this.extending = compound.getBoolean("extending");
        this.shouldHeadBeRendered = compound.getBoolean("source");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
        compound.setBoolean("source", this.shouldHeadBeRendered);
        return compound;
    }
    
    public void addCollissionAABBs(final World p_190609_1_, final BlockPos p_190609_2_, final AxisAlignedBB p_190609_3_, final List<AxisAlignedBB> p_190609_4_, @Nullable final Entity p_190609_5_) {
        if (!this.extending && this.shouldHeadBeRendered) {
            this.pistonState.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true).addCollisionBoxToList(p_190609_1_, p_190609_2_, p_190609_3_, p_190609_4_, p_190609_5_, false);
        }
        final EnumFacing enumfacing = TileEntityPiston.MOVING_ENTITY.get();
        if (this.progress >= 1.0 || enumfacing != (this.extending ? this.pistonFacing : this.pistonFacing.getOpposite())) {
            final int i = p_190609_4_.size();
            IBlockState iblockstate;
            if (this.shouldPistonHeadBeRendered()) {
                iblockstate = Blocks.PISTON_HEAD.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, this.pistonFacing).withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, this.extending != 1.0f - this.progress < 0.25f);
            }
            else {
                iblockstate = this.pistonState;
            }
            final float f = this.getExtendedProgress(this.progress);
            final double d0 = this.pistonFacing.getXOffset() * f;
            final double d2 = this.pistonFacing.getYOffset() * f;
            final double d3 = this.pistonFacing.getZOffset() * f;
            iblockstate.addCollisionBoxToList(p_190609_1_, p_190609_2_, p_190609_3_.offset(-d0, -d2, -d3), p_190609_4_, p_190609_5_, true);
            for (int j = i; j < p_190609_4_.size(); ++j) {
                p_190609_4_.set(j, p_190609_4_.get(j).offset(d0, d2, d3));
            }
        }
    }
    
    static {
        MOVING_ENTITY = new ThreadLocal<EnumFacing>() {
            @Override
            protected EnumFacing initialValue() {
                return null;
            }
        };
    }
}
