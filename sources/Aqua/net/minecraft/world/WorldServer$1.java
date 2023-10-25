package net.minecraft.world;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;

class WorldServer.1
implements Predicate<EntityLivingBase> {
    WorldServer.1() {
    }

    public boolean apply(EntityLivingBase p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.isEntityAlive() && WorldServer.this.canSeeSky(p_apply_1_.getPosition());
    }
}
