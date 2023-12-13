/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import org.lwjgl.input.Mouse;

public class AutoShift
extends Module {
    public AutoShift() {
        super("AutoShift", Category.Combat);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (this.getState()) {
            AutoShift.mc.gameSettings.keyBindSneak.pressed = Mouse.isButtonDown(0);
        }
    }
}

