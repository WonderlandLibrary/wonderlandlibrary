/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoPotionDebug
extends Module {
    public NoPotionDebug() {
        super("NoPotionDebug", Category.Visuals);
    }

    @EventTarget
    public void onRender2D(Event2D event) {
    }
}

