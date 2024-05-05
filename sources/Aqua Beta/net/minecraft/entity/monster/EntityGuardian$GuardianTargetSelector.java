package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

static class EntityGuardian.GuardianTargetSelector
implements Predicate<EntityLivingBase> {
    private EntityGuardian parentEntity;

    public EntityGuardian.GuardianTargetSelector(EntityGuardian guardian) {
        this.parentEntity = guardian;
    }

    public boolean apply(EntityLivingBase p_apply_1_) {
        return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSqToEntity((Entity)this.parentEntity) > 9.0;
    }
}
