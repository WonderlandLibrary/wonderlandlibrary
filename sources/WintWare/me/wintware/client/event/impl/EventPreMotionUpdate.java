/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class EventPreMotionUpdate
extends EventCancellable {
    private float yaw;
    private float pitch;
    private boolean ground;
    public double x;
    public double y;
    public double z;

    public EventPreMotionUpdate(float yaw, float pitch, boolean ground, double x, double y, double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft();
        Minecraft.player.rotationYawHead = yaw;
        Minecraft.getMinecraft();
        Minecraft.player.renderYawOffset = yaw;
    }

    public void setYaw1(float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft();
        Minecraft.player.rotationYaw = yaw;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isGround() {
        return this.ground;
    }

    public void setGround(boolean isGround) {
        this.ground = isGround;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        Minecraft.getMinecraft();
        Minecraft.player.rotationPitchHead = pitch;
    }

    public boolean onGround() {
        return this.ground;
    }

    public static class EventSpawnPlayer
    extends EventCancellable {
        private EntityPlayer player;

        public EventSpawnPlayer(EntityPlayer player) {
            this.player = player;
        }

        public EntityPlayer getPlayer() {
            return this.player;
        }

        public void setPlayer(EntityPlayer player) {
            this.player = player;
        }
    }
}

