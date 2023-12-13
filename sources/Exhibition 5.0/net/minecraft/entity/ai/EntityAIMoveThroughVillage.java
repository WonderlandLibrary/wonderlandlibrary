// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.pathfinding.PathNavigateGround;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private EntityCreature theEntity;
    private double movementSpeed;
    private PathEntity entityPathNavigate;
    private VillageDoorInfo doorInfo;
    private boolean isNocturnal;
    private List doorList;
    private static final String __OBFID = "CL_00001597";
    
    public EntityAIMoveThroughVillage(final EntityCreature p_i1638_1_, final double p_i1638_2_, final boolean p_i1638_4_) {
        this.doorList = Lists.newArrayList();
        this.theEntity = p_i1638_1_;
        this.movementSpeed = p_i1638_2_;
        this.isNocturnal = p_i1638_4_;
        this.setMutexBits(1);
        if (!(p_i1638_1_.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        this.func_75414_f();
        if (this.isNocturnal && this.theEntity.worldObj.isDaytime()) {
            return false;
        }
        final Village var1 = this.theEntity.worldObj.getVillageCollection().func_176056_a(new BlockPos(this.theEntity), 0);
        if (var1 == null) {
            return false;
        }
        this.doorInfo = this.func_75412_a(var1);
        if (this.doorInfo == null) {
            return false;
        }
        final PathNavigateGround var2 = (PathNavigateGround)this.theEntity.getNavigator();
        final boolean var3 = var2.func_179686_g();
        var2.func_179688_b(false);
        this.entityPathNavigate = var2.func_179680_a(this.doorInfo.func_179852_d());
        var2.func_179688_b(var3);
        if (this.entityPathNavigate != null) {
            return true;
        }
        final Vec3 var4 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 10, 7, new Vec3(this.doorInfo.func_179852_d().getX(), this.doorInfo.func_179852_d().getY(), this.doorInfo.func_179852_d().getZ()));
        if (var4 == null) {
            return false;
        }
        var2.func_179688_b(false);
        this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ(var4.xCoord, var4.yCoord, var4.zCoord);
        var2.func_179688_b(var3);
        return this.entityPathNavigate != null;
    }
    
    @Override
    public boolean continueExecuting() {
        if (this.theEntity.getNavigator().noPath()) {
            return false;
        }
        final float var1 = this.theEntity.width + 4.0f;
        return this.theEntity.getDistanceSq(this.doorInfo.func_179852_d()) > var1 * var1;
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().setPath(this.entityPathNavigate, this.movementSpeed);
    }
    
    @Override
    public void resetTask() {
        if (this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq(this.doorInfo.func_179852_d()) < 16.0) {
            this.doorList.add(this.doorInfo);
        }
    }
    
    private VillageDoorInfo func_75412_a(final Village p_75412_1_) {
        VillageDoorInfo var2 = null;
        int var3 = Integer.MAX_VALUE;
        final List var4 = p_75412_1_.getVillageDoorInfoList();
        for (final VillageDoorInfo var6 : var4) {
            final int var7 = var6.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));
            if (var7 < var3 && !this.func_75413_a(var6)) {
                var2 = var6;
                var3 = var7;
            }
        }
        return var2;
    }
    
    private boolean func_75413_a(final VillageDoorInfo p_75413_1_) {
        for (final VillageDoorInfo var3 : this.doorList) {
            if (p_75413_1_.func_179852_d().equals(var3.func_179852_d())) {
                return true;
            }
        }
        return false;
    }
    
    private void func_75414_f() {
        if (this.doorList.size() > 15) {
            this.doorList.remove(0);
        }
    }
}
