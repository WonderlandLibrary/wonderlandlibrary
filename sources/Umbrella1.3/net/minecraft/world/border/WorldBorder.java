/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.border.EnumBorderStatus;
import net.minecraft.world.border.IBorderListener;

public class WorldBorder {
    private final List listeners = Lists.newArrayList();
    private double centerX = 0.0;
    private double centerZ = 0.0;
    private double startDiameter;
    private double endDiameter = this.startDiameter = 6.0E7;
    private long endTime;
    private long startTime;
    private int worldSize = 29999984;
    private double damageAmount = 0.2;
    private double damageBuffer = 5.0;
    private int warningTime = 15;
    private int warningDistance = 5;
    private static final String __OBFID = "CL_00002012";

    public boolean contains(BlockPos pos) {
        return (double)(pos.getX() + 1) > this.minX() && (double)pos.getX() < this.maxX() && (double)(pos.getZ() + 1) > this.minZ() && (double)pos.getZ() < this.maxZ();
    }

    public boolean contains(ChunkCoordIntPair range) {
        return (double)range.getXEnd() > this.minX() && (double)range.getXStart() < this.maxX() && (double)range.getZEnd() > this.minZ() && (double)range.getZStart() < this.maxZ();
    }

    public boolean contains(AxisAlignedBB bb) {
        return bb.maxX > this.minX() && bb.minX < this.maxX() && bb.maxZ > this.minZ() && bb.minZ < this.maxZ();
    }

    public double getClosestDistance(Entity p_177745_1_) {
        return this.getClosestDistance(p_177745_1_.posX, p_177745_1_.posZ);
    }

    public double getClosestDistance(double x, double z) {
        double var5 = z - this.minZ();
        double var7 = this.maxZ() - z;
        double var9 = x - this.minX();
        double var11 = this.maxX() - x;
        double var13 = Math.min(var9, var11);
        var13 = Math.min(var13, var5);
        return Math.min(var13, var7);
    }

    public EnumBorderStatus getStatus() {
        return this.endDiameter < this.startDiameter ? EnumBorderStatus.SHRINKING : (this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }

    public double minX() {
        double var1 = this.getCenterX() - this.getDiameter() / 2.0;
        if (var1 < (double)(-this.worldSize)) {
            var1 = -this.worldSize;
        }
        return var1;
    }

    public double minZ() {
        double var1 = this.getCenterZ() - this.getDiameter() / 2.0;
        if (var1 < (double)(-this.worldSize)) {
            var1 = -this.worldSize;
        }
        return var1;
    }

    public double maxX() {
        double var1 = this.getCenterX() + this.getDiameter() / 2.0;
        if (var1 > (double)this.worldSize) {
            var1 = this.worldSize;
        }
        return var1;
    }

    public double maxZ() {
        double var1 = this.getCenterZ() + this.getDiameter() / 2.0;
        if (var1 > (double)this.worldSize) {
            var1 = this.worldSize;
        }
        return var1;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public void setCenter(double x, double z) {
        this.centerX = x;
        this.centerZ = z;
        for (IBorderListener var6 : this.getListeners()) {
            var6.onCenterChanged(this, x, z);
        }
    }

    public double getDiameter() {
        if (this.getStatus() != EnumBorderStatus.STATIONARY) {
            double var1 = (float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);
            if (var1 < 1.0) {
                return this.startDiameter + (this.endDiameter - this.startDiameter) * var1;
            }
            this.setTransition(this.endDiameter);
        }
        return this.startDiameter;
    }

    public long getTimeUntilTarget() {
        return this.getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - System.currentTimeMillis() : 0L;
    }

    public double getTargetSize() {
        return this.endDiameter;
    }

    public void setTransition(double newSize) {
        this.startDiameter = newSize;
        this.endDiameter = newSize;
        this.startTime = this.endTime = System.currentTimeMillis();
        for (IBorderListener var4 : this.getListeners()) {
            var4.onSizeChanged(this, newSize);
        }
    }

    public void setTransition(double p_177738_1_, double p_177738_3_, long p_177738_5_) {
        this.startDiameter = p_177738_1_;
        this.endDiameter = p_177738_3_;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + p_177738_5_;
        for (IBorderListener var8 : this.getListeners()) {
            var8.func_177692_a(this, p_177738_1_, p_177738_3_, p_177738_5_);
        }
    }

    protected List getListeners() {
        return Lists.newArrayList((Iterable)this.listeners);
    }

    public void addListener(IBorderListener listener) {
        this.listeners.add(listener);
    }

    public void setSize(int size) {
        this.worldSize = size;
    }

    public int getSize() {
        return this.worldSize;
    }

    public double getDamageBuffer() {
        return this.damageBuffer;
    }

    public void setDamageBuffer(double p_177724_1_) {
        this.damageBuffer = p_177724_1_;
        for (IBorderListener var4 : this.getListeners()) {
            var4.func_177695_c(this, p_177724_1_);
        }
    }

    public double func_177727_n() {
        return this.damageAmount;
    }

    public void func_177744_c(double p_177744_1_) {
        this.damageAmount = p_177744_1_;
        for (IBorderListener var4 : this.getListeners()) {
            var4.func_177696_b(this, p_177744_1_);
        }
    }

    public double func_177749_o() {
        return this.endTime == this.startTime ? 0.0 : Math.abs(this.startDiameter - this.endDiameter) / (double)(this.endTime - this.startTime);
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public void setWarningTime(int warningTime) {
        this.warningTime = warningTime;
        for (IBorderListener var3 : this.getListeners()) {
            var3.onWarningTimeChanged(this, warningTime);
        }
    }

    public int getWarningDistance() {
        return this.warningDistance;
    }

    public void setWarningDistance(int warningDistance) {
        this.warningDistance = warningDistance;
        for (IBorderListener var3 : this.getListeners()) {
            var3.onWarningDistanceChanged(this, warningDistance);
        }
    }
}

