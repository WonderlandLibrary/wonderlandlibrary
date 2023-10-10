package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;

public class WorldBorder {
   private double damageBuffer;
   private double damageAmount;
   private int warningTime;
   private int worldSize;
   private long startTime;
   private final List listeners = Lists.newArrayList();
   private long endTime;
   private double startDiameter = 6.0E7D;
   private int warningDistance;
   private double centerX = 0.0D;
   private double endDiameter;
   private double centerZ = 0.0D;

   public double getResizeSpeed() {
      return this.endTime == this.startTime ? 0.0D : Math.abs(this.startDiameter - this.endDiameter) / (double)(this.endTime - this.startTime);
   }

   public int getSize() {
      return this.worldSize;
   }

   public double maxZ() {
      double var1 = this.getCenterZ() + this.getDiameter() / 2.0D;
      if (var1 > (double)this.worldSize) {
         var1 = (double)this.worldSize;
      }

      return var1;
   }

   public long getTimeUntilTarget() {
      return this.getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - System.currentTimeMillis() : 0L;
   }

   public double getDamageAmount() {
      return this.damageAmount;
   }

   protected List getListeners() {
      return Lists.newArrayList((Iterable)this.listeners);
   }

   public double minZ() {
      double var1 = this.getCenterZ() - this.getDiameter() / 2.0D;
      if (var1 < (double)(-this.worldSize)) {
         var1 = (double)(-this.worldSize);
      }

      return var1;
   }

   public EnumBorderStatus getStatus() {
      return this.endDiameter < this.startDiameter ? EnumBorderStatus.SHRINKING : (this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
   }

   public double getDiameter() {
      if (this.getStatus() != EnumBorderStatus.STATIONARY) {
         double var1 = (double)((float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime));
         if (var1 < 1.0D) {
            return this.startDiameter + (this.endDiameter - this.startDiameter) * var1;
         }

         this.setTransition(this.endDiameter);
      }

      return this.startDiameter;
   }

   public void setSize(int var1) {
      this.worldSize = var1;
   }

   public int getWarningDistance() {
      return this.warningDistance;
   }

   public boolean contains(BlockPos var1) {
      return (double)(var1.getX() + 1) > this.minX() && (double)var1.getX() < this.maxX() && (double)(var1.getZ() + 1) > this.minZ() && (double)var1.getZ() < this.maxZ();
   }

   public void setTransition(double var1) {
      this.startDiameter = var1;
      this.endDiameter = var1;
      this.endTime = System.currentTimeMillis();
      this.startTime = this.endTime;
      Iterator var4 = this.getListeners().iterator();

      while(var4.hasNext()) {
         IBorderListener var3 = (IBorderListener)var4.next();
         var3.onSizeChanged(this, var1);
      }

   }

   public void setCenter(double var1, double var3) {
      this.centerX = var1;
      this.centerZ = var3;
      Iterator var6 = this.getListeners().iterator();

      while(var6.hasNext()) {
         IBorderListener var5 = (IBorderListener)var6.next();
         var5.onCenterChanged(this, var1, var3);
      }

   }

   public WorldBorder() {
      this.endDiameter = this.startDiameter;
      this.worldSize = 29999984;
      this.damageAmount = 0.2D;
      this.damageBuffer = 5.0D;
      this.warningTime = 15;
      this.warningDistance = 5;
   }

   public double maxX() {
      double var1 = this.getCenterX() + this.getDiameter() / 2.0D;
      if (var1 > (double)this.worldSize) {
         var1 = (double)this.worldSize;
      }

      return var1;
   }

   public void setTransition(double var1, double var3, long var5) {
      this.startDiameter = var1;
      this.endDiameter = var3;
      this.startTime = System.currentTimeMillis();
      this.endTime = this.startTime + var5;
      Iterator var8 = this.getListeners().iterator();

      while(var8.hasNext()) {
         IBorderListener var7 = (IBorderListener)var8.next();
         var7.onTransitionStarted(this, var1, var3, var5);
      }

   }

   public double getCenterZ() {
      return this.centerZ;
   }

   public void setDamageAmount(double var1) {
      this.damageAmount = var1;
      Iterator var4 = this.getListeners().iterator();

      while(var4.hasNext()) {
         IBorderListener var3 = (IBorderListener)var4.next();
         var3.onDamageAmountChanged(this, var1);
      }

   }

   public void setWarningTime(int var1) {
      this.warningTime = var1;
      Iterator var3 = this.getListeners().iterator();

      while(var3.hasNext()) {
         IBorderListener var2 = (IBorderListener)var3.next();
         var2.onWarningTimeChanged(this, var1);
      }

   }

   public double getDamageBuffer() {
      return this.damageBuffer;
   }

   public double getCenterX() {
      return this.centerX;
   }

   public boolean contains(AxisAlignedBB var1) {
      return var1.maxX > this.minX() && var1.minX < this.maxX() && var1.maxZ > this.minZ() && var1.minZ < this.maxZ();
   }

   public int getWarningTime() {
      return this.warningTime;
   }

   public double getClosestDistance(double var1, double var3) {
      double var5 = var3 - this.minZ();
      double var7 = this.maxZ() - var3;
      double var9 = var1 - this.minX();
      double var11 = this.maxX() - var1;
      double var13 = Math.min(var9, var11);
      var13 = Math.min(var13, var5);
      return Math.min(var13, var7);
   }

   public double getClosestDistance(Entity var1) {
      return this.getClosestDistance(var1.posX, var1.posZ);
   }

   public boolean contains(ChunkCoordIntPair var1) {
      return (double)var1.getXEnd() > this.minX() && (double)var1.getXStart() < this.maxX() && (double)var1.getZEnd() > this.minZ() && (double)var1.getZStart() < this.maxZ();
   }

   public void setDamageBuffer(double var1) {
      this.damageBuffer = var1;
      Iterator var4 = this.getListeners().iterator();

      while(var4.hasNext()) {
         IBorderListener var3 = (IBorderListener)var4.next();
         var3.onDamageBufferChanged(this, var1);
      }

   }

   public double minX() {
      double var1 = this.getCenterX() - this.getDiameter() / 2.0D;
      if (var1 < (double)(-this.worldSize)) {
         var1 = (double)(-this.worldSize);
      }

      return var1;
   }

   public void addListener(IBorderListener var1) {
      this.listeners.add(var1);
   }

   public double getTargetSize() {
      return this.endDiameter;
   }

   public void setWarningDistance(int var1) {
      this.warningDistance = var1;
      Iterator var3 = this.getListeners().iterator();

      while(var3.hasNext()) {
         IBorderListener var2 = (IBorderListener)var3.next();
         var2.onWarningDistanceChanged(this, var1);
      }

   }
}
