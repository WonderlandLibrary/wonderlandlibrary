package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySlime;

static class EntitySlime.AISlimeHop
extends EntityAIBase {
    private EntitySlime slime;

    public EntitySlime.AISlimeHop(EntitySlime slimeIn) {
        this.slime = slimeIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void updateTask() {
        ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0);
    }
}
