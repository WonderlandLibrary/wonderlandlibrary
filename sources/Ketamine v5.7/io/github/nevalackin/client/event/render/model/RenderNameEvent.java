package io.github.nevalackin.client.event.render.model;

import io.github.nevalackin.client.event.CancellableEvent;
import net.minecraft.entity.EntityLivingBase;

public final class RenderNameEvent extends CancellableEvent {

    private final EntityLivingBase entity;

    public RenderNameEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
