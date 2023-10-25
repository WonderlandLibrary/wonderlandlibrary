/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;

public class Spam
extends Module {
    Timer timer = new Timer();

    public Spam() {
        super("Spam", 0, Category.MISC, "Spams an ad for the client constantly in the chat.");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (this.timer.hasTimeElapsed(1000L, true)) {
            Minecraft.thePlayer.sendChatMessage("Try Exodus! private . exodus " + Math.random());
        }
    }
}

