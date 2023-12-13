package io.github.nevalackin.client.event.render.model;

import io.github.nevalackin.client.event.Event;

public final class ApplyHurtEffectEvent implements Event {

    private RenderCallbackFunc preRenderModelCallback;
    private RenderCallbackFunc preRenderLayersCallback;
    private int hurtColour;

    public ApplyHurtEffectEvent(RenderCallbackFunc preRenderModelCallback,
                                RenderCallbackFunc preRenderLayersCallback,
                                int hurtColour) {
        this.preRenderLayersCallback = preRenderLayersCallback;
        this.preRenderModelCallback = preRenderModelCallback;
        this.hurtColour = hurtColour;
    }

    public int getHurtColour() {
        return hurtColour;
    }

    public void setHurtColour(int hurtColour) {
        this.hurtColour = hurtColour;
    }

    public RenderCallbackFunc getPreRenderModelCallback() {
        return preRenderModelCallback;
    }

    public void setPreRenderModelCallback(RenderCallbackFunc preRenderModelCallback) {
        this.preRenderModelCallback = preRenderModelCallback;
    }

    public RenderCallbackFunc getPreRenderLayersCallback() {
        return preRenderLayersCallback;
    }

    public void setPreRenderLayersCallback(RenderCallbackFunc preRenderLayersCallback) {
        this.preRenderLayersCallback = preRenderLayersCallback;
    }

    public enum RenderCallbackFunc {
        SET, UNSET, NONE
    }
}
