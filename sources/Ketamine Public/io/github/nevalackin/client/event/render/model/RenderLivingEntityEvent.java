package io.github.nevalackin.client.event.render.model;

import io.github.nevalackin.client.event.CancellableEvent;
import io.github.nevalackin.client.event.Event;
import io.github.nevalackin.client.event.render.RenderCallback;
import net.minecraft.entity.EntityLivingBase;

public final class RenderLivingEntityEvent extends CancellableEvent {

    private final EntityLivingBase entity;
    private final float partialTicks;

    private RenderCallback preRenderCallback;
    private RenderCallback postRenderCallback;

    public RenderLivingEntityEvent(EntityLivingBase entity, float partialTicks) {
        this.entity = entity;
        this.partialTicks = partialTicks;
    }

    public void setPreRenderCallback(RenderCallback preRenderCallback) {
        this.preRenderCallback = preRenderCallback;
    }

    public void setPostRenderCallback(RenderCallback postRenderCallback) {
        this.postRenderCallback = postRenderCallback;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void onPreRender() {
        if (this.preRenderCallback != null)
            this.preRenderCallback.render();
    }

    public void onPostRender() {
        if (this.postRenderCallback != null)
            this.postRenderCallback.render();
    }
}
