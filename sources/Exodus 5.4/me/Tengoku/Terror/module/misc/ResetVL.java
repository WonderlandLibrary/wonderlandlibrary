/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class ResetVL
extends Module {
    private int jumped;
    private double y;

    public ResetVL() {
        super("ResetVL", 0, Category.MISC, "Resets your position after being flagged.");
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (Minecraft.thePlayer.onGround && this.jumped <= 25) {
            Minecraft.thePlayer.motionY = 0.11;
            ++this.jumped;
        }
        if (this.jumped <= 25) {
            Minecraft.thePlayer.posY = this.y;
            ResetVL.mc.timer.timerSpeed = 2.25f;
        } else {
            ResetVL.mc.timer.timerSpeed = 1.0f;
            this.toggle();
        }
    }

    @Override
    public void onEnable() {
        this.jumped = 0;
        this.y = Minecraft.thePlayer.posY;
        super.onEnable();
    }
}

