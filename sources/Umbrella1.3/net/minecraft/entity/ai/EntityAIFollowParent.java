/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent
extends EntityAIBase {
    EntityAnimal childAnimal;
    EntityAnimal parentAnimal;
    double field_75347_c;
    private int field_75345_d;
    private static final String __OBFID = "CL_00001586";

    public EntityAIFollowParent(EntityAnimal p_i1626_1_, double p_i1626_2_) {
        this.childAnimal = p_i1626_1_;
        this.field_75347_c = p_i1626_2_;
    }

    @Override
    public boolean shouldExecute() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        List var1 = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0, 4.0, 8.0));
        EntityAnimal var2 = null;
        double var3 = Double.MAX_VALUE;
        for (EntityAnimal var6 : var1) {
            double var7;
            if (var6.getGrowingAge() < 0 || !((var7 = this.childAnimal.getDistanceSqToEntity(var6)) <= var3)) continue;
            var3 = var7;
            var2 = var6;
        }
        if (var2 == null) {
            return false;
        }
        if (var3 < 9.0) {
            return false;
        }
        this.parentAnimal = var2;
        return true;
    }

    @Override
    public boolean continueExecuting() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.parentAnimal.isEntityAlive()) {
            return false;
        }
        double var1 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
        return var1 >= 9.0 && var1 <= 256.0;
    }

    @Override
    public void startExecuting() {
        this.field_75345_d = 0;
    }

    @Override
    public void resetTask() {
        this.parentAnimal = null;
    }

    @Override
    public void updateTask() {
        if (--this.field_75345_d <= 0) {
            this.field_75345_d = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.field_75347_c);
        }
    }
}

