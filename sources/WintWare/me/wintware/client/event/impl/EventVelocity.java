/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class EventVelocity
extends EventCancellable {
    private double motionX;
    private double motionY;
    private double motionZ;
    private Entity entity;

    public EventVelocity(Entity entity, double x, double y, double z) {
        this.entity = entity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }
}

