/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MoveEvent
implements Event {
    private double x;
    private double y;
    private double z;
    public float strafe;
    public float forward;
    public float friction;
    public float yaw;
    public boolean canceled;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMovementSpeed() {
        double baseSpeed = 0.2873;
        Minecraft.getMinecraft();
        if (Minecraft.player.isPotionActive(Potion.getPotionById(1))) {
            Minecraft.getMinecraft();
            int amplifier = Minecraft.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public double getMovementSpeed(double baseSpeed) {
        double speed = baseSpeed;
        Minecraft.getMinecraft();
        if (Minecraft.player.isPotionActive(Potion.getPotionById(1))) {
            Minecraft.getMinecraft();
            int amplifier = Minecraft.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            return speed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return speed;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getLegitMotion() {
        return 0.42f;
    }

    public double getMotionY(double mY) {
        Minecraft.getMinecraft();
        if (Minecraft.player.isPotionActive(Potion.getPotionById(8))) {
            Minecraft.getMinecraft();
            mY += (double)(Minecraft.player.getActivePotionEffect(Potion.getPotionById(8)).getAmplifier() + 1) * 0.1;
        }
        return mY;
    }
}

