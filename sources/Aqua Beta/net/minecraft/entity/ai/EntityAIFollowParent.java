package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent
extends EntityAIBase {
    EntityAnimal childAnimal;
    EntityAnimal parentAnimal;
    double moveSpeed;
    private int delayCounter;

    public EntityAIFollowParent(EntityAnimal animal, double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    public boolean shouldExecute() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        List list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0, 4.0, 8.0));
        EntityAnimal entityanimal = null;
        double d0 = Double.MAX_VALUE;
        for (EntityAnimal entityanimal1 : list) {
            double d1;
            if (entityanimal1.getGrowingAge() < 0 || !((d1 = this.childAnimal.getDistanceSqToEntity((Entity)entityanimal1)) <= d0)) continue;
            d0 = d1;
            entityanimal = entityanimal1;
        }
        if (entityanimal == null) {
            return false;
        }
        if (d0 < 9.0) {
            return false;
        }
        this.parentAnimal = entityanimal;
        return true;
    }

    public boolean continueExecuting() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.parentAnimal.isEntityAlive()) {
            return false;
        }
        double d0 = this.childAnimal.getDistanceSqToEntity((Entity)this.parentAnimal);
        return d0 >= 9.0 && d0 <= 256.0;
    }

    public void startExecuting() {
        this.delayCounter = 0;
    }

    public void resetTask() {
        this.parentAnimal = null;
    }

    public void updateTask() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving((Entity)this.parentAnimal, this.moveSpeed);
        }
    }
}
