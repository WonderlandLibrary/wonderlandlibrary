/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class FullBright
extends Module {
    @EventTarget
    public void onPre(EventUpdate eventUpdate) {
        Minecraft.gameSettings.gammaSetting = 2000.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.gameSettings.gammaSetting = 1.0f;
    }

    public FullBright() {
        super("Full Bright", 0, Category.RENDER, "Makes your game bright at all times.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

