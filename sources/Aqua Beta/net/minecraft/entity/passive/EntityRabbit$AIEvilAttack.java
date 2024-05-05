package net.minecraft.entity.passive;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.passive.EntityRabbit;

static class EntityRabbit.AIEvilAttack
extends EntityAIAttackOnCollide {
    public EntityRabbit.AIEvilAttack(EntityRabbit rabbit) {
        super((EntityCreature)rabbit, EntityLivingBase.class, 1.4, true);
    }

    protected double func_179512_a(EntityLivingBase attackTarget) {
        return 4.0f + attackTarget.width;
    }
}
