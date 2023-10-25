package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;

static final class EntityWither.1
implements Predicate<Entity> {
    EntityWither.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
    }
}
