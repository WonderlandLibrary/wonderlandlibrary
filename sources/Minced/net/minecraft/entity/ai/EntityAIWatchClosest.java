// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityAIWatchClosest extends EntityAIBase
{
    protected EntityLiving entity;
    protected Entity closestEntity;
    protected float maxDistance;
    private int lookTime;
    private final float chance;
    protected Class<? extends Entity> watchedClass;
    
    public EntityAIWatchClosest(final EntityLiving entityIn, final Class<? extends Entity> watchTargetClass, final float maxDistance) {
        this.entity = entityIn;
        this.watchedClass = watchTargetClass;
        this.maxDistance = maxDistance;
        this.chance = 0.02f;
        this.setMutexBits(2);
    }
    
    public EntityAIWatchClosest(final EntityLiving entityIn, final Class<? extends Entity> watchTargetClass, final float maxDistance, final float chanceIn) {
        this.entity = entityIn;
        this.watchedClass = watchTargetClass;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.entity.getRNG().nextFloat() >= this.chance) {
            return false;
        }
        if (this.entity.getAttackTarget() != null) {
            this.closestEntity = this.entity.getAttackTarget();
        }
        if (this.watchedClass == EntityPlayer.class) {
            this.closestEntity = this.entity.world.getClosestPlayer(this.entity.posX, this.entity.posY, this.entity.posZ, this.maxDistance, (Predicate<Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)EntitySelectors.notRiding(this.entity)));
        }
        else {
            this.closestEntity = this.entity.world.findNearestEntityWithinAABB((Class<? extends EntityLiving>)this.watchedClass, this.entity.getEntityBoundingBox().grow(this.maxDistance, 3.0, this.maxDistance), this.entity);
        }
        return this.closestEntity != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.closestEntity.isEntityAlive() && this.entity.getDistanceSq(this.closestEntity) <= this.maxDistance * this.maxDistance && this.lookTime > 0;
    }
    
    @Override
    public void startExecuting() {
        this.lookTime = 40 + this.entity.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask() {
        this.closestEntity = null;
    }
    
    @Override
    public void updateTask() {
        this.entity.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
        --this.lookTime;
    }
}
