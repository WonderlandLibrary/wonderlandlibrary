/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

public class TileEntityPiston
extends TileEntity
implements IUpdatePlayerListBox {
    private IBlockState field_174932_a;
    private EnumFacing field_174931_f;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    private float lastProgress;
    private List field_174933_k = Lists.newArrayList();
    private static final String __OBFID = "CL_00000369";

    public TileEntityPiston() {
    }

    public TileEntityPiston(IBlockState p_i45665_1_, EnumFacing p_i45665_2_, boolean p_i45665_3_, boolean p_i45665_4_) {
        this.field_174932_a = p_i45665_1_;
        this.field_174931_f = p_i45665_2_;
        this.extending = p_i45665_3_;
        this.shouldHeadBeRendered = p_i45665_4_;
    }

    public IBlockState func_174927_b() {
        return this.field_174932_a;
    }

    @Override
    public int getBlockMetadata() {
        return 0;
    }

    public boolean isExtending() {
        return this.extending;
    }

    public EnumFacing func_174930_e() {
        return this.field_174931_f;
    }

    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }

    public float func_145860_a(float p_145860_1_) {
        if (p_145860_1_ > 1.0f) {
            p_145860_1_ = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * p_145860_1_;
    }

    public float func_174929_b(float p_174929_1_) {
        return this.extending ? (this.func_145860_a(p_174929_1_) - 1.0f) * (float)this.field_174931_f.getFrontOffsetX() : (1.0f - this.func_145860_a(p_174929_1_)) * (float)this.field_174931_f.getFrontOffsetX();
    }

    public float func_174928_c(float p_174928_1_) {
        return this.extending ? (this.func_145860_a(p_174928_1_) - 1.0f) * (float)this.field_174931_f.getFrontOffsetY() : (1.0f - this.func_145860_a(p_174928_1_)) * (float)this.field_174931_f.getFrontOffsetY();
    }

    public float func_174926_d(float p_174926_1_) {
        return this.extending ? (this.func_145860_a(p_174926_1_) - 1.0f) * (float)this.field_174931_f.getFrontOffsetZ() : (1.0f - this.func_145860_a(p_174926_1_)) * (float)this.field_174931_f.getFrontOffsetZ();
    }

    private void func_145863_a(float p_145863_1_, float p_145863_2_) {
        List var4;
        p_145863_1_ = this.extending ? 1.0f - p_145863_1_ : (p_145863_1_ -= 1.0f);
        AxisAlignedBB var3 = Blocks.piston_extension.func_176424_a(this.worldObj, this.pos, this.field_174932_a, p_145863_1_, this.field_174931_f);
        if (var3 != null && !(var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, var3)).isEmpty()) {
            this.field_174933_k.addAll(var4);
            for (Entity var6 : this.field_174933_k) {
                if (this.field_174932_a.getBlock() == Blocks.slime_block && this.extending) {
                    switch (SwitchAxis.field_177248_a[this.field_174931_f.getAxis().ordinal()]) {
                        case 1: {
                            var6.motionX = this.field_174931_f.getFrontOffsetX();
                            break;
                        }
                        case 2: {
                            var6.motionY = this.field_174931_f.getFrontOffsetY();
                            break;
                        }
                        case 3: {
                            var6.motionZ = this.field_174931_f.getFrontOffsetZ();
                        }
                    }
                    continue;
                }
                var6.moveEntity(p_145863_2_ * (float)this.field_174931_f.getFrontOffsetX(), p_145863_2_ * (float)this.field_174931_f.getFrontOffsetY(), p_145863_2_ * (float)this.field_174931_f.getFrontOffsetZ());
            }
            this.field_174933_k.clear();
        }
    }

    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0f && this.worldObj != null) {
            this.progress = 1.0f;
            this.lastProgress = 1.0f;
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.field_174932_a, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.field_174932_a.getBlock());
            }
        }
    }

    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.func_145863_a(1.0f, 0.25f);
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.field_174932_a, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.field_174932_a.getBlock());
            }
        } else {
            this.progress += 0.5f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            if (this.extending) {
                this.func_145863_a(this.progress, this.progress - this.lastProgress + 0.0625f);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.field_174932_a = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.field_174931_f = EnumFacing.getFront(compound.getInteger("facing"));
        this.lastProgress = this.progress = compound.getFloat("progress");
        this.extending = compound.getBoolean("extending");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.field_174932_a.getBlock()));
        compound.setInteger("blockData", this.field_174932_a.getBlock().getMetaFromState(this.field_174932_a));
        compound.setInteger("facing", this.field_174931_f.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
    }

    static final class SwitchAxis {
        static final int[] field_177248_a = new int[EnumFacing.Axis.values().length];
        private static final String __OBFID = "CL_00002034";

        static {
            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAxis.field_177248_a[EnumFacing.Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchAxis() {
        }
    }
}

