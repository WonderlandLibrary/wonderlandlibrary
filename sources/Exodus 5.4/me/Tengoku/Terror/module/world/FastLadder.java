/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.world;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;

public class FastLadder
extends Module {
    private TimerUtils timer = new TimerUtils();

    public FastLadder() {
        super("FastLadder", 0, Category.WORLD, "");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Minecraft.thePlayer.isOnLadder()) {
            if (this.timer.hasReached(10000.0)) {
                this.timer.reset();
                FastLadder.mc.timer.timerSpeed = 1.5f;
            }
        } else {
            FastLadder.mc.timer.timerSpeed = 1.0f;
        }
    }
}

