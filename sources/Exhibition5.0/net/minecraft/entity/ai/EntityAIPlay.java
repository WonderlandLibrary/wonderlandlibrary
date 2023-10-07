// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIPlay extends EntityAIBase
{
    private EntityVillager villagerObj;
    private EntityLivingBase targetVillager;
    private double field_75261_c;
    private int playTime;
    private static final String __OBFID = "CL_00001605";
    
    public EntityAIPlay(final EntityVillager p_i1646_1_, final double p_i1646_2_) {
        this.villagerObj = p_i1646_1_;
        this.field_75261_c = p_i1646_2_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villagerObj.getGrowingAge() >= 0) {
            return false;
        }
        if (this.villagerObj.getRNG().nextInt(400) != 0) {
            return false;
        }
        final List var1 = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0, 3.0, 6.0));
        double var2 = Double.MAX_VALUE;
        for (final EntityVillager var4 : var1) {
            if (var4 != this.villagerObj && !var4.isPlaying() && var4.getGrowingAge() < 0) {
                final double var5 = var4.getDistanceSqToEntity(this.villagerObj);
                if (var5 > var2) {
                    continue;
                }
                var2 = var5;
                this.targetVillager = var4;
            }
        }
        if (this.targetVillager == null) {
            final Vec3 var6 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (var6 == null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.playTime > 0;
    }
    
    @Override
    public void startExecuting() {
        if (this.targetVillager != null) {
            this.villagerObj.setPlaying(true);
        }
        this.playTime = 1000;
    }
    
    @Override
    public void resetTask() {
        this.villagerObj.setPlaying(false);
        this.targetVillager = null;
    }
    
    @Override
    public void updateTask() {
        --this.playTime;
        if (this.targetVillager != null) {
            if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0) {
                this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.field_75261_c);
            }
        }
        else if (this.villagerObj.getNavigator().noPath()) {
            final Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (var1 == null) {
                return;
            }
            this.villagerObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, this.field_75261_c);
        }
    }
}
