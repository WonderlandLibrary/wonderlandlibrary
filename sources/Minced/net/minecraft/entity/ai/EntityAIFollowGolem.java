// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase
{
    private final EntityVillager villager;
    private EntityIronGolem ironGolem;
    private int takeGolemRoseTick;
    private boolean tookGolemRose;
    
    public EntityAIFollowGolem(final EntityVillager villagerIn) {
        this.villager = villagerIn;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villager.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.villager.world.isDaytime()) {
            return false;
        }
        final List<EntityIronGolem> list = this.villager.world.getEntitiesWithinAABB((Class<? extends EntityIronGolem>)EntityIronGolem.class, this.villager.getEntityBoundingBox().grow(6.0, 2.0, 6.0));
        if (list.isEmpty()) {
            return false;
        }
        for (final EntityIronGolem entityirongolem : list) {
            if (entityirongolem.getHoldRoseTick() > 0) {
                this.ironGolem = entityirongolem;
                break;
            }
        }
        return this.ironGolem != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.ironGolem.getHoldRoseTick() > 0;
    }
    
    @Override
    public void startExecuting() {
        this.takeGolemRoseTick = this.villager.getRNG().nextInt(320);
        this.tookGolemRose = false;
        this.ironGolem.getNavigator().clearPath();
    }
    
    @Override
    public void resetTask() {
        this.ironGolem = null;
        this.villager.getNavigator().clearPath();
    }
    
    @Override
    public void updateTask() {
        this.villager.getLookHelper().setLookPositionWithEntity(this.ironGolem, 30.0f, 30.0f);
        if (this.ironGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
            this.villager.getNavigator().tryMoveToEntityLiving(this.ironGolem, 0.5);
            this.tookGolemRose = true;
        }
        if (this.tookGolemRose && this.villager.getDistanceSq(this.ironGolem) < 4.0) {
            this.ironGolem.setHoldingRose(false);
            this.villager.getNavigator().clearPath();
        }
    }
}
