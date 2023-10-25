package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;

static class EntityGhast.AIRandomFly
extends EntityAIBase {
    private EntityGhast parentEntity;

    public EntityGhast.AIRandomFly(EntityGhast ghast) {
        this.parentEntity = ghast;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        double d2;
        double d1;
        EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
        if (!entitymovehelper.isUpdating()) {
            return true;
        }
        double d0 = entitymovehelper.getX() - this.parentEntity.posX;
        double d3 = d0 * d0 + (d1 = entitymovehelper.getY() - this.parentEntity.posY) * d1 + (d2 = entitymovehelper.getZ() - this.parentEntity.posZ) * d2;
        return d3 < 1.0 || d3 > 3600.0;
    }

    public boolean continueExecuting() {
        return false;
    }

    public void startExecuting() {
        Random random = this.parentEntity.getRNG();
        double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
        double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
        double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
        this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0);
    }
}
