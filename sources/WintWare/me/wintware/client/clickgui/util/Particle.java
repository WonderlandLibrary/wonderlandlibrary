/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.clickgui.util;

import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;

public class Particle {
    public float x;
    public float y;
    public float radius;
    public float speed;
    public float ticks;
    public float opacity;

    public Particle(ScaledResolution sr, float radius, float speed) {
        this.x = new Random().nextFloat() * (float)sr.getScaledWidth();
        this.y = new Random().nextFloat() * (float)sr.getScaledHeight();
        this.ticks = new Random().nextFloat() * (float)sr.getScaledHeight() / 2.0f;
        this.radius = radius;
        this.speed = speed;
    }
}

