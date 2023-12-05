package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:10
 */
public class EventMotion extends Event {

    private final Type TYPE;
    private float yaw,pitch;

    public EventMotion(Type type, float yaw, float pitch) {
        this.TYPE = type;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Type getType() {
        return TYPE;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public enum Type {
        PRE,POST;
    }
}
