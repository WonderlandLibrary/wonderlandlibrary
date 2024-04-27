/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.utils;

import cc.paimon.utils.SimpleAnimation;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class ClickEffect {
    private float x;
    private SimpleAnimation animation = new SimpleAnimation(0.0f);
    private float y;

    public void draw() {
        this.animation.setAnimation(100.0f, 12.0);
        double d = 8.0f * this.animation.getValue() / 100.0f;
        int n = (int)(255.0f - 255.0f * this.animation.getValue() / 100.0f);
        int n2 = new Color(255, 255, 255, n).getRGB();
        RenderUtils.drawArc(this.x, this.y, d, n2, 0, 360.0, 5);
    }

    public boolean canRemove() {
        return this.animation.getValue() > 99.0f;
    }

    public ClickEffect(float f, float f2) {
        this.x = f;
        this.y = f2;
        this.animation.setValue(0.0f);
    }
}

