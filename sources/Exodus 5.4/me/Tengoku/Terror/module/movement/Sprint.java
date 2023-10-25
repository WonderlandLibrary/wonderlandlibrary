/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class Sprint
extends Module {
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Minecraft.thePlayer != null) {
            if (!Minecraft.gameSettings.keyBindBack.pressed) {
                if (!Minecraft.thePlayer.isCollidedHorizontally) {
                    if (Minecraft.thePlayer.motionX != 0.0) {
                        if (Minecraft.thePlayer.motionY != 0.0) {
                            if (Minecraft.thePlayer.motionZ != 0.0) {
                                Minecraft.thePlayer.setSprinting(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT, "Automatically sprints for you.");
    }
}

