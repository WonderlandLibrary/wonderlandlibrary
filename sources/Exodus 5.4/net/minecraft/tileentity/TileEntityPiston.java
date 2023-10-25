/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityPiston
extends TileEntity
implements ITickable {
    private List<Entity> field_174933_k = Lists.newArrayList();
    private float progress;
    private boolean shouldHeadBeRendered;
    private boolean extending;
    private EnumFacing pistonFacing;
    private float lastProgress;
    private IBlockState pistonState;

    public float getOffsetX(float f) {
        return this.extending ? (this.getProgress(f) - 1.0f) * (float)this.pistonFacing.getFrontOffsetX() : (1.0f - this.getProgress(f)) * (float)this.pistonFacing.getFrontOffsetX();
    }

    public float getOffsetY(float f) {
        return this.extending ? (this.getProgress(f) - 1.0f) * (float)this.pistonFacing.getFrontOffsetY() : (1.0f - this.getProgress(f)) * (float)this.pistonFacing.getFrontOffsetY();
    }

    public TileEntityPiston() {
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        nBTTagCompound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        nBTTagCompound.setInteger("facing", this.pistonFacing.getIndex());
        nBTTagCompound.setFloat("progress", this.lastProgress);
        nBTTagCompound.setBoolean("extending", this.extending);
    }

    public TileEntityPiston(IBlockState iBlockState, EnumFacing enumFacing, boolean bl, boolean bl2) {
        this.pistonState = iBlockState;
        this.pistonFacing = enumFacing;
        this.extending = bl;
        this.shouldHeadBeRendered = bl2;
    }

    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0f && this.worldObj != null) {
            this.progress = 1.0f;
            this.lastProgress = 1.0f;
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            }
        }
    }

    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.launchWithSlimeBlock(1.0f, 0.25f);
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            }
        } else {
            this.progress += 0.5f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            if (this.extending) {
                this.launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625f);
            }
        }
    }

    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }

    @Override
    public int getBlockMetadata() {
        return 0;
    }

    public boolean isExtending() {
        return this.extending;
    }

    public float getOffsetZ(float f) {
        return this.extending ? (this.getProgress(f) - 1.0f) * (float)this.pistonFacing.getFrontOffsetZ() : (1.0f - this.getProgress(f)) * (float)this.pistonFacing.getFrontOffsetZ();
    }

    public IBlockState getPistonState() {
        return this.pistonState;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.pistonState = Block.getBlockById(nBTTagCompound.getInteger("blockId")).getStateFromMeta(nBTTagCompound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(nBTTagCompound.getInteger("facing"));
        this.lastProgress = this.progress = nBTTagCompound.getFloat("progress");
        this.extending = nBTTagCompound.getBoolean("extending");
    }

    public EnumFacing getFacing() {
        return this.pistonFacing;
    }

    private void launchWithSlimeBlock(float f, float f2) {
        List<Entity> list;
        f = this.extending ? 1.0f - f : (f -= 1.0f);
        AxisAlignedBB axisAlignedBB = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, f, this.pistonFacing);
        if (axisAlignedBB != null && !(list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB)).isEmpty()) {
            this.field_174933_k.addAll(list);
            for (Entity entity : this.field_174933_k) {
                if (this.pistonState.getBlock() == Blocks.slime_block && this.extending) {
                    switch (this.pistonFacing.getAxis()) {
                        case X: {
                            entity.motionX = this.pistonFacing.getFrontOffsetX();
                            break;
                        }
                        case Y: {
                            entity.motionY = this.pistonFacing.getFrontOffsetY();
                            break;
                        }
                        case Z: {
                            entity.motionZ = this.pistonFacing.getFrontOffsetZ();
                        }
                    }
                    continue;
                }
                entity.moveEntity(f2 * (float)this.pistonFacing.getFrontOffsetX(), f2 * (float)this.pistonFacing.getFrontOffsetY(), f2 * (float)this.pistonFacing.getFrontOffsetZ());
            }
            this.field_174933_k.clear();
        }
    }

    public float getProgress(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * f;
    }
}

