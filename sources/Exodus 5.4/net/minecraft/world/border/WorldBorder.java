/*
 * Decompiled with CFR 0.152.
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
    private double damageBuffer = 5.0;
    private int worldSize = 29999984;
    private int warningDistance = 5;
    private double startDiameter;
    private int warningTime = 15;
    private double centerX = 0.0;
    private long startTime;
    private final List<IBorderListener> listeners = Lists.newArrayList();
    private double centerZ = 0.0;
    private double endDiameter = this.startDiameter = 6.0E7;
    private double damageAmount = 0.2;
    private long endTime;

    public double minZ() {
        double d = this.getCenterZ() - this.getDiameter() / 2.0;
        if (d < (double)(-this.worldSize)) {
            d = -this.worldSize;
        }
        return d;
    }

    public boolean contains(AxisAlignedBB axisAlignedBB) {
        return axisAlignedBB.maxX > this.minX() && axisAlignedBB.minX < this.maxX() && axisAlignedBB.maxZ > this.minZ() && axisAlignedBB.minZ < this.maxZ();
    }

    public void setWarningDistance(int n) {
        this.warningDistance = n;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onWarningDistanceChanged(this, n);
        }
    }

    public int getSize() {
        return this.worldSize;
    }

    public double getResizeSpeed() {
        return this.endTime == this.startTime ? 0.0 : Math.abs(this.startDiameter - this.endDiameter) / (double)(this.endTime - this.startTime);
    }

    public long getTimeUntilTarget() {
        return this.getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - System.currentTimeMillis() : 0L;
    }

    public int getWarningDistance() {
        return this.warningDistance;
    }

    protected List<IBorderListener> getListeners() {
        return Lists.newArrayList(this.listeners);
    }

    public boolean contains(BlockPos blockPos) {
        return (double)(blockPos.getX() + 1) > this.minX() && (double)blockPos.getX() < this.maxX() && (double)(blockPos.getZ() + 1) > this.minZ() && (double)blockPos.getZ() < this.maxZ();
    }

    public double maxX() {
        double d = this.getCenterX() + this.getDiameter() / 2.0;
        if (d > (double)this.worldSize) {
            d = this.worldSize;
        }
        return d;
    }

    public double getClosestDistance(double d, double d2) {
        double d3 = d2 - this.minZ();
        double d4 = this.maxZ() - d2;
        double d5 = d - this.minX();
        double d6 = this.maxX() - d;
        double d7 = Math.min(d5, d6);
        d7 = Math.min(d7, d3);
        return Math.min(d7, d4);
    }

    public double minX() {
        double d = this.getCenterX() - this.getDiameter() / 2.0;
        if (d < (double)(-this.worldSize)) {
            d = -this.worldSize;
        }
        return d;
    }

    public double getDamageAmount() {
        return this.damageAmount;
    }

    public void setTransition(double d) {
        this.startDiameter = d;
        this.endDiameter = d;
        this.startTime = this.endTime = System.currentTimeMillis();
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onSizeChanged(this, d);
        }
    }

    public void setDamageBuffer(double d) {
        this.damageBuffer = d;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onDamageBufferChanged(this, d);
        }
    }

    public void setTransition(double d, double d2, long l) {
        this.startDiameter = d;
        this.endDiameter = d2;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + l;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onTransitionStarted(this, d, d2, l);
        }
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public void setDamageAmount(double d) {
        this.damageAmount = d;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onDamageAmountChanged(this, d);
        }
    }

    public double getDiameter() {
        if (this.getStatus() != EnumBorderStatus.STATIONARY) {
            double d = (float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);
            if (d < 1.0) {
                return this.startDiameter + (this.endDiameter - this.startDiameter) * d;
            }
            this.setTransition(this.endDiameter);
        }
        return this.startDiameter;
    }

    public double maxZ() {
        double d = this.getCenterZ() + this.getDiameter() / 2.0;
        if (d > (double)this.worldSize) {
            d = this.worldSize;
        }
        return d;
    }

    public void addListener(IBorderListener iBorderListener) {
        this.listeners.add(iBorderListener);
    }

    public double getDamageBuffer() {
        return this.damageBuffer;
    }

    public double getCenterZ() {
        return this.centerZ;
    }

    public double getTargetSize() {
        return this.endDiameter;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public void setSize(int n) {
        this.worldSize = n;
    }

    public boolean contains(ChunkCoordIntPair chunkCoordIntPair) {
        return (double)chunkCoordIntPair.getXEnd() > this.minX() && (double)chunkCoordIntPair.getXStart() < this.maxX() && (double)chunkCoordIntPair.getZEnd() > this.minZ() && (double)chunkCoordIntPair.getZStart() < this.maxZ();
    }

    public double getClosestDistance(Entity entity) {
        return this.getClosestDistance(entity.posX, entity.posZ);
    }

    public void setCenter(double d, double d2) {
        this.centerX = d;
        this.centerZ = d2;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onCenterChanged(this, d, d2);
        }
    }

    public void setWarningTime(int n) {
        this.warningTime = n;
        for (IBorderListener iBorderListener : this.getListeners()) {
            iBorderListener.onWarningTimeChanged(this, n);
        }
    }

    public EnumBorderStatus getStatus() {
        return this.endDiameter < this.startDiameter ? EnumBorderStatus.SHRINKING : (this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }
}

