/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import net.minecraft.entity.Entity;

public class EventUseEntity
extends Event<EventUseEntity> {
    private Entity target;
    private static double z;
    private static double x;
    public boolean onGround;
    private static float yaw;
    private static double y;
    private static float pitch;

    public boolean isOnGround() {
        return this.onGround;
    }

    public static void setPitch(float f) {
        pitch = f;
    }

    public static float getYaw() {
        return yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public static double getZ() {
        return z;
    }

    public static void setX(double d) {
        x = d;
    }

    public static void setZ(double d) {
        z = d;
    }

    public static void setYaw(float f) {
        yaw = f;
    }

    public Entity getEntity() {
        return this.target;
    }

    public void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public static double getX() {
        return x;
    }

    public void setEntity(Entity entity) {
        this.target = entity;
    }

    public EventUseEntity(Entity entity, double d, double d2, double d3, float f, float f2, boolean bl) {
        this.target = entity;
        x = d;
        y = d2;
        z = d3;
        yaw = f;
        pitch = f2;
        this.onGround = bl;
        this.setEntity(entity);
    }

    public static double getY() {
        return y;
    }

    public static void setY(double d) {
        y = d;
    }
}

