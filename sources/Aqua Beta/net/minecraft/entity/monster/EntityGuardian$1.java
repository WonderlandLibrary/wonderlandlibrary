package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

class EntityGuardian.1
implements Predicate<EntityPlayerMP> {
    EntityGuardian.1() {
    }

    public boolean apply(EntityPlayerMP p_apply_1_) {
        return EntityGuardian.this.getDistanceSqToEntity((Entity)p_apply_1_) < 2500.0 && p_apply_1_.theItemInWorldManager.survivalOrAdventure();
    }
}
