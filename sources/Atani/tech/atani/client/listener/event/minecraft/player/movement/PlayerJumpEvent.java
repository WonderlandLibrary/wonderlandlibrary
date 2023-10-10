package tech.atani.client.listener.event.minecraft.player.movement;

import tech.atani.client.listener.event.Event;

public final class PlayerJumpEvent
extends Event {
    private float yaw;

    public PlayerJumpEvent(float f) {
        this.yaw = f;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

}
