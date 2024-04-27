/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render;

import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.SmoothStepAnimation;
import org.lwjgl.input.Mouse;

public class Scroll {
    private float minScroll = 0.0f;
    private float scroll;
    private Animation scrollAnimation;
    private float rawScroll;
    private float maxScroll = Float.MAX_VALUE;

    public Scroll() {
        this.scrollAnimation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);
    }

    public float getMaxScroll() {
        return this.maxScroll;
    }

    public void setMaxScroll(float f) {
        this.maxScroll = f;
    }

    public void onScroll(int n) {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        this.rawScroll += (float)Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min(this.minScroll, this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new SmoothStepAnimation(n, this.rawScroll - this.scroll, Direction.BACKWARDS);
    }

    public boolean isScrollAnimationDone() {
        return this.scrollAnimation.isDone();
    }

    public float getScroll() {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        return this.scroll;
    }
}

