package ravenweave.client.event.impl;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.weavemc.loader.api.event.CancellableEvent;

public class LivingUpdateEvent extends CancellableEvent {
    public Entity entity;
    public boolean sprinting;
    public LivingUpdateEvent(Entity entityPlayerSP, boolean sprinting) {
        this.entity = entityPlayerSP;
        this.sprinting = sprinting;
    }

    public EntityPlayerSP getEntity() {
        return (EntityPlayerSP) this.entity;
    }

    public EntityLivingBase getEntityLiving() {
        return (EntityLivingBase) entity;
    }
}
