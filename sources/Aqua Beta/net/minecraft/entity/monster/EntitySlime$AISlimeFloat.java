package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.pathfinding.PathNavigateGround;

static class EntitySlime.AISlimeFloat
extends EntityAIBase {
    private EntitySlime slime;

    public EntitySlime.AISlimeFloat(EntitySlime slimeIn) {
        this.slime = slimeIn;
        this.setMutexBits(5);
        ((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
    }

    public boolean shouldExecute() {
        return this.slime.isInWater() || this.slime.isInLava();
    }

    public void updateTask() {
        if (this.slime.getRNG().nextFloat() < 0.8f) {
            this.slime.getJumpHelper().setJumping();
        }
        ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2);
    }
}
