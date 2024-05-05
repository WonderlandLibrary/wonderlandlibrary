package net.minecraft.entity.ai;

import java.util.Comparator;
import net.minecraft.entity.Entity;

public static class EntityAINearestAttackableTarget.Sorter
implements Comparator<Entity> {
    private final Entity theEntity;

    public EntityAINearestAttackableTarget.Sorter(Entity theEntityIn) {
        this.theEntity = theEntityIn;
    }

    public int compare(Entity p_compare_1_, Entity p_compare_2_) {
        double d1;
        double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
        return d0 < (d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_)) ? -1 : (d0 > d1 ? 1 : 0);
    }
}
