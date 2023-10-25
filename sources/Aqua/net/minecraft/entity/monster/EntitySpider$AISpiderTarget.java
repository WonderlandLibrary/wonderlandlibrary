package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;

static class EntitySpider.AISpiderTarget<T extends EntityLivingBase>
extends EntityAINearestAttackableTarget {
    public EntitySpider.AISpiderTarget(EntitySpider spider, Class<T> classTarget) {
        super((EntityCreature)spider, classTarget, true);
    }

    public boolean shouldExecute() {
        float f = this.taskOwner.getBrightness(1.0f);
        return f >= 0.5f ? false : super.shouldExecute();
    }
}
