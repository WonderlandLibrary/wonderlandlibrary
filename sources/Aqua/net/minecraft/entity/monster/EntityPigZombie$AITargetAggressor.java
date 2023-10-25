package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;

static class EntityPigZombie.AITargetAggressor
extends EntityAINearestAttackableTarget<EntityPlayer> {
    public EntityPigZombie.AITargetAggressor(EntityPigZombie p_i45829_1_) {
        super((EntityCreature)p_i45829_1_, EntityPlayer.class, true);
    }

    public boolean shouldExecute() {
        return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
    }
}
