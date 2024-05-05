package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.EntitySpider;

static class EntitySpider.AISpiderAttack
extends EntityAIAttackOnCollide {
    public EntitySpider.AISpiderAttack(EntitySpider spider, Class<? extends Entity> targetClass) {
        super((EntityCreature)spider, targetClass, 1.0, true);
    }

    public boolean continueExecuting() {
        float f = this.attacker.getBrightness(1.0f);
        if (f >= 0.5f && this.attacker.getRNG().nextInt(100) == 0) {
            this.attacker.setAttackTarget((EntityLivingBase)null);
            return false;
        }
        return super.continueExecuting();
    }

    protected double func_179512_a(EntityLivingBase attackTarget) {
        return 4.0f + attackTarget.width;
    }
}
