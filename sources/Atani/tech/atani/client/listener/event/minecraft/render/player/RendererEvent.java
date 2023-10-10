package tech.atani.client.listener.event.minecraft.render.player;

import tech.atani.client.listener.event.Event;

public class RendererEvent extends Event {
    float yaw, pitch;

    public RendererEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}