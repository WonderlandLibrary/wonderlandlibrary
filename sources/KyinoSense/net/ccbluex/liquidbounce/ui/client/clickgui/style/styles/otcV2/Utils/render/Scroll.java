/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.render;

import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.impl.SmoothStepAnimation;
import org.lwjgl.input.Mouse;

public class Scroll {
    private float maxScroll = Float.MAX_VALUE;
    private float minScroll = 0.0f;
    private float rawScroll;
    private float scroll;
    private Animation scrollAnimation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);

    public void onScroll(int ms) {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        this.rawScroll += (float)Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min(this.minScroll, this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new SmoothStepAnimation(ms, this.rawScroll - this.scroll, Direction.BACKWARDS);
    }

    public boolean isScrollAnimationDone() {
        return this.scrollAnimation.isDone();
    }

    public float getScroll() {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        return this.scroll;
    }
}

