package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityPigZombie;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityPigZombie.AIHurtByAggressor
extends EntityAIHurtByTarget {
    public EntityPigZombie.AIHurtByAggressor(EntityPigZombie p_i45828_1_) {
        super((EntityCreature)p_i45828_1_, true, new Class[0]);
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
        super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
        if (creatureIn instanceof EntityPigZombie) {
            EntityPigZombie.access$000((EntityPigZombie)((EntityPigZombie)creatureIn), (Entity)entityLivingBaseIn);
        }
    }
}
