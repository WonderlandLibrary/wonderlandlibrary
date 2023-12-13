/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import java.awt.Color;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRenderModel;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class SkyColor
extends Module {
    public SkyColor() {
        super("SkyColor", Category.Visuals);
    }

    @EventTarget
    public void onFogColorRender(EventRenderModel.EventColorFov event) {
        double rainbowState = Math.ceil(System.currentTimeMillis() + 300L + 300L) / 15.0;
        Color color = Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.4f, 1.0f);
        event.setRed((float)color.getRed() / 255.0f);
        event.setGreen((float)color.getGreen() / 255.0f);
        event.setBlue((float)color.getBlue() / 255.0f);
    }
}

