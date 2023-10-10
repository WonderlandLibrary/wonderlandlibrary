/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Iterator;
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
    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    private float lastProgress;
    private List<Entity> field_174933_k = Lists.newArrayList();

    public TileEntityPiston() {
    }

    public TileEntityPiston(IBlockState pistonStateIn, EnumFacing pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn) {
        this.pistonState = pistonStateIn;
        this.pistonFacing = pistonFacingIn;
        this.extending = extendingIn;
        this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
    }

    public IBlockState getPistonState() {
        return this.pistonState;
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
        if (!(ticks > 1.0f)) return this.lastProgress + (this.progress - this.lastProgress) * ticks;
        ticks = 1.0f;
        return this.lastProgress + (this.progress - this.lastProgress) * ticks;
    }

    public float getOffsetX(float ticks) {
        float f;
        if (this.extending) {
            f = (this.getProgress(ticks) - 1.0f) * (float)this.pistonFacing.getFrontOffsetX();
            return f;
        }
        f = (1.0f - this.getProgress(ticks)) * (float)this.pistonFacing.getFrontOffsetX();
        return f;
    }

    public float getOffsetY(float ticks) {
        float f;
        if (this.extending) {
            f = (this.getProgress(ticks) - 1.0f) * (float)this.pistonFacing.getFrontOffsetY();
            return f;
        }
        f = (1.0f - this.getProgress(ticks)) * (float)this.pistonFacing.getFrontOffsetY();
        return f;
    }

    public float getOffsetZ(float ticks) {
        float f;
        if (this.extending) {
            f = (this.getProgress(ticks) - 1.0f) * (float)this.pistonFacing.getFrontOffsetZ();
            return f;
        }
        f = (1.0f - this.getProgress(ticks)) * (float)this.pistonFacing.getFrontOffsetZ();
        return f;
    }

    private void launchWithSlimeBlock(float p_145863_1_, float p_145863_2_) {
        p_145863_1_ = this.extending ? 1.0f - p_145863_1_ : (p_145863_1_ -= 1.0f);
        AxisAlignedBB axisalignedbb = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, p_145863_1_, this.pistonFacing);
        if (axisalignedbb == null) return;
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
        if (list.isEmpty()) return;
        this.field_174933_k.addAll(list);
        Iterator<Entity> iterator = this.field_174933_k.iterator();
        block5: while (true) {
            if (!iterator.hasNext()) {
                this.field_174933_k.clear();
                return;
            }
            Entity entity = iterator.next();
            if (this.pistonState.getBlock() == Blocks.slime_block && this.extending) {
                switch (1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[this.pistonFacing.getAxis().ordinal()]) {
                    case 1: {
                        entity.motionX = this.pistonFacing.getFrontOffsetX();
                        break;
                    }
                    case 2: {
                        entity.motionY = this.pistonFacing.getFrontOffsetY();
                        break;
                    }
                    case 3: {
                        entity.motionZ = this.pistonFacing.getFrontOffsetZ();
                        continue block5;
                    }
                }
                continue;
            }
            entity.moveEntity(p_145863_2_ * (float)this.pistonFacing.getFrontOffsetX(), p_145863_2_ * (float)this.pistonFacing.getFrontOffsetY(), p_145863_2_ * (float)this.pistonFacing.getFrontOffsetZ());
        }
    }

    public void clearPistonTileEntity() {
        if (!(this.lastProgress < 1.0f)) return;
        if (this.worldObj == null) return;
        this.progress = 1.0f;
        this.lastProgress = 1.0f;
        this.worldObj.removeTileEntity(this.pos);
        this.invalidate();
        if (this.worldObj.getBlockState(this.pos).getBlock() != Blocks.piston_extension) return;
        this.worldObj.setBlockState(this.pos, this.pistonState, 3);
        this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
    }

    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.launchWithSlimeBlock(1.0f, 0.25f);
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() != Blocks.piston_extension) return;
            this.worldObj.setBlockState(this.pos, this.pistonState, 3);
            this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            return;
        }
        this.progress += 0.5f;
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        if (!this.extending) return;
        this.launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625f);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
        this.lastProgress = this.progress = compound.getFloat("progress");
        this.extending = compound.getBoolean("extending");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
    }
}

