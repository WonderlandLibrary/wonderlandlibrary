package io.github.nevalackin.client.event.render.model;

import io.github.nevalackin.client.event.CancellableEvent;
import io.github.nevalackin.client.event.render.RenderCallback;
import net.minecraft.entity.player.EntityPlayer;

public final class ModelRenderEvent extends CancellableEvent {

    private final EntityPlayer entity;
    private final RenderCallback modelRenderer;
    private final RenderCallback layerRenderer;
    private final float bodyYaw;

    private boolean post;

    public ModelRenderEvent(EntityPlayer entity, RenderCallback modelRenderer, RenderCallback layerRenderer, float bodyYaw) {
        this.entity = entity;
        this.modelRenderer = modelRenderer;
        this.layerRenderer = layerRenderer;
        this.bodyYaw = bodyYaw;
    }

    public void setPost() {
        this.post = true;
    }

    public boolean isPre() {
        return !this.post;
    }

    public EntityPlayer getEntity() {
        return entity;
    }

    public void drawModel() {
        this.modelRenderer.render();
    }

    public void drawLayers() {
        this.layerRenderer.render();
    }

    public float getBodyYaw() {
        return bodyYaw;
    }
}
