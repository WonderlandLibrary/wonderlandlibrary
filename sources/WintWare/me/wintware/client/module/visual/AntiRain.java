/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class AntiRain
extends Module {
    public AntiRain() {
        super("AntiRain", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (AntiRain.mc.world.isRaining()) {
            AntiRain.mc.world.setRainStrength(0.0f);
            AntiRain.mc.world.setThunderStrength(0.0f);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

