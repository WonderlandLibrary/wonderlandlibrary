/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.utils.Animation;
import java.awt.Color;

public class ColorAnimation {
    private final Animation red;
    private final Animation green;
    private final Animation blue;
    private final Animation alpha;

    public ColorAnimation(Color color, float time) {
        this.red = new Animation(color.getRed(), time);
        this.green = new Animation(color.getGreen(), time);
        this.blue = new Animation(color.getBlue(), time);
        this.alpha = new Animation(color.getAlpha(), time);
    }

    public void update(Color color, double deltaTime) {
        this.updateAnimations(color, deltaTime);
    }

    public void updateAnimations(Color color, double deltaTime) {
        this.red.update(color.getRed(), deltaTime);
        this.green.update(color.getGreen(), deltaTime);
        this.blue.update(color.getBlue(), deltaTime);
        this.alpha.update(color.getAlpha(), deltaTime);
    }

    public Color value() {
        return new Color(this.normalize(this.red.value() / 255.0f), this.normalize(this.green.value() / 255.0f), this.normalize(this.blue.value() / 255.0f), this.normalize(this.alpha.value() / 255.0f));
    }

    public void forceValue(Color color) {
        this.red.forceValue(color.getRed());
        this.green.forceValue(color.getGreen());
        this.blue.forceValue(color.getBlue());
        this.alpha.forceValue(color.getAlpha());
    }

    public float normalize(float input) {
        return Math.max(0.0f, Math.min(1.0f, input));
    }
}

