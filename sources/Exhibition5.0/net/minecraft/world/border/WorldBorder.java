// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.border;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import java.util.List;

public class WorldBorder
{
    private final List listeners;
    private double centerX;
    private double centerZ;
    private double startDiameter;
    private double endDiameter;
    private long endTime;
    private long startTime;
    private int worldSize;
    private double damageAmount;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;
    private static final String __OBFID = "CL_00002012";
    
    public WorldBorder() {
        this.listeners = Lists.newArrayList();
        this.centerX = 0.0;
        this.centerZ = 0.0;
        this.startDiameter = 6.0E7;
        this.endDiameter = this.startDiameter;
        this.worldSize = 29999984;
        this.damageAmount = 0.2;
        this.damageBuffer = 5.0;
        this.warningTime = 15;
        this.warningDistance = 5;
    }
    
    public boolean contains(final BlockPos pos) {
        return pos.getX() + 1 > this.minX() && pos.getX() < this.maxX() && pos.getZ() + 1 > this.minZ() && pos.getZ() < this.maxZ();
    }
    
    public boolean contains(final ChunkCoordIntPair range) {
        return range.getXEnd() > this.minX() && range.getXStart() < this.maxX() && range.getZEnd() > this.minZ() && range.getZStart() < this.maxZ();
    }
    
    public boolean contains(final AxisAlignedBB bb) {
        return bb.maxX > this.minX() && bb.minX < this.maxX() && bb.maxZ > this.minZ() && bb.minZ < this.maxZ();
    }
    
    public double getClosestDistance(final Entity p_177745_1_) {
        return this.getClosestDistance(p_177745_1_.posX, p_177745_1_.posZ);
    }
    
    public double getClosestDistance(final double x, final double z) {
        final double var5 = z - this.minZ();
        final double var6 = this.maxZ() - z;
        final double var7 = x - this.minX();
        final double var8 = this.maxX() - x;
        double var9 = Math.min(var7, var8);
        var9 = Math.min(var9, var5);
        return Math.min(var9, var6);
    }
    
    public EnumBorderStatus getStatus() {
        return (this.endDiameter < this.startDiameter) ? EnumBorderStatus.SHRINKING : ((this.endDiameter > this.startDiameter) ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }
    
    public double minX() {
        double var1 = this.getCenterX() - this.getDiameter() / 2.0;
        if (var1 < -this.worldSize) {
            var1 = -this.worldSize;
        }
        return var1;
    }
    
    public double minZ() {
        double var1 = this.getCenterZ() - this.getDiameter() / 2.0;
        if (var1 < -this.worldSize) {
            var1 = -this.worldSize;
        }
        return var1;
    }
    
    public double maxX() {
        double var1 = this.getCenterX() + this.getDiameter() / 2.0;
        if (var1 > this.worldSize) {
            var1 = this.worldSize;
        }
        return var1;
    }
    
    public double maxZ() {
        double var1 = this.getCenterZ() + this.getDiameter() / 2.0;
        if (var1 > this.worldSize) {
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
    
    public void setCenter(final double x, final double z) {
        this.centerX = x;
        this.centerZ = z;
        for (final IBorderListener var6 : this.getListeners()) {
            var6.onCenterChanged(this, x, z);
        }
    }
    
    public double getDiameter() {
        if (this.getStatus() != EnumBorderStatus.STATIONARY) {
            final double var1 = (System.currentTimeMillis() - this.startTime) / (this.endTime - this.startTime);
            if (var1 < 1.0) {
                return this.startDiameter + (this.endDiameter - this.startDiameter) * var1;
            }
            this.setTransition(this.endDiameter);
        }
        return this.startDiameter;
    }
    
    public long getTimeUntilTarget() {
        return (this.getStatus() != EnumBorderStatus.STATIONARY) ? (this.endTime - System.currentTimeMillis()) : 0L;
    }
    
    public double getTargetSize() {
        return this.endDiameter;
    }
    
    public void setTransition(final double newSize) {
        this.startDiameter = newSize;
        this.endDiameter = newSize;
        this.endTime = System.currentTimeMillis();
        this.startTime = this.endTime;
        for (final IBorderListener var4 : this.getListeners()) {
            var4.onSizeChanged(this, newSize);
        }
    }
    
    public void setTransition(final double p_177738_1_, final double p_177738_3_, final long p_177738_5_) {
        this.startDiameter = p_177738_1_;
        this.endDiameter = p_177738_3_;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + p_177738_5_;
        for (final IBorderListener var8 : this.getListeners()) {
            var8.func_177692_a(this, p_177738_1_, p_177738_3_, p_177738_5_);
        }
    }
    
    protected List getListeners() {
        return Lists.newArrayList((Iterable)this.listeners);
    }
    
    public void addListener(final IBorderListener listener) {
        this.listeners.add(listener);
    }
    
    public void setSize(final int size) {
        this.worldSize = size;
    }
    
    public int getSize() {
        return this.worldSize;
    }
    
    public double getDamageBuffer() {
        return this.damageBuffer;
    }
    
    public void setDamageBuffer(final double p_177724_1_) {
        this.damageBuffer = p_177724_1_;
        for (final IBorderListener var4 : this.getListeners()) {
            var4.func_177695_c(this, p_177724_1_);
        }
    }
    
    public double func_177727_n() {
        return this.damageAmount;
    }
    
    public void func_177744_c(final double p_177744_1_) {
        this.damageAmount = p_177744_1_;
        for (final IBorderListener var4 : this.getListeners()) {
            var4.func_177696_b(this, p_177744_1_);
        }
    }
    
    public double func_177749_o() {
        return (this.endTime == this.startTime) ? 0.0 : (Math.abs(this.startDiameter - this.endDiameter) / (this.endTime - this.startTime));
    }
    
    public int getWarningTime() {
        return this.warningTime;
    }
    
    public void setWarningTime(final int warningTime) {
        this.warningTime = warningTime;
        for (final IBorderListener var3 : this.getListeners()) {
            var3.onWarningTimeChanged(this, warningTime);
        }
    }
    
    public int getWarningDistance() {
        return this.warningDistance;
    }
    
    public void setWarningDistance(final int warningDistance) {
        this.warningDistance = warningDistance;
        for (final IBorderListener var3 : this.getListeners()) {
            var3.onWarningDistanceChanged(this, warningDistance);
        }
    }
}
