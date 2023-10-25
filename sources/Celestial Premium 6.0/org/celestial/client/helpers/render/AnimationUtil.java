/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render;

import net.minecraft.client.Minecraft;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.render.Animation;

public class AnimationUtil {
    private final Animation animation;
    public float velocity;
    public float oppositeVelocity;
    public float min;
    public float max;
    public float step;
    public float time;
    public boolean reversed;
    public String type;

    public AnimationUtil(Animation animation, float velocity, float min, float max, float step, boolean reversed) {
        this.velocity = velocity;
        this.oppositeVelocity = MathematicHelper.nabs(velocity);
        this.min = min;
        this.max = max;
        this.reversed = reversed;
        this.animation = animation;
        this.step = step;
        this.time = reversed ? max : min;
    }

    public void animate() {
        if (this.reversed) {
            if (this.time > this.min) {
                this.time = (float)((double)this.time - (double)this.step * (Minecraft.frameTime / 10.0));
            }
        } else if (this.time < this.max) {
            this.time = (float)((double)this.time + (double)this.step * (Minecraft.frameTime / 10.0));
        }
        this.velocity = this.animation.easeInOut(this.time, this.min, this.max, this.max);
        this.oppositeVelocity = MathematicHelper.nabs(this.velocity);
    }

    public void reset(boolean animated) {
        if (animated) {
            if (this.reversed) {
                if (this.time < this.max) {
                    this.time = (float)((double)this.time + (double)this.step * (Minecraft.frameTime / 10.0));
                }
            } else if (this.time > this.min) {
                this.time = (float)((double)this.time - (double)this.step * (Minecraft.frameTime / 10.0));
            }
            this.velocity = this.animation.easeInOut(this.time, this.min, this.max, this.max);
            this.oppositeVelocity = MathematicHelper.nabs(this.velocity);
        } else {
            this.time = this.reversed ? this.max : this.min;
            this.velocity = this.min;
            this.oppositeVelocity = MathematicHelper.nabs(this.velocity);
        }
    }

    public int getInt() {
        return (int)this.velocity;
    }

    public float getFloat() {
        return this.velocity;
    }

    public double getDouble() {
        return this.velocity;
    }
}

