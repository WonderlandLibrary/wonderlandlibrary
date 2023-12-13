package vip.radium.event.impl.entity;

import net.minecraft.entity.EntityLivingBase;
import vip.radium.event.Event;

public final class EntityHealthUpdateEvent implements Event {

    private final EntityLivingBase entity;
    private final double damage;

    public EntityHealthUpdateEvent(EntityLivingBase entity, double damage) {
        this.entity = entity;
        this.damage = damage;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public double getDamage() {
        return damage;
    }

}
