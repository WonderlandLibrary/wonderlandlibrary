/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.movement.speeds;

import net.dev.important.event.JumpEvent;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.utils.MinecraftInstance;

public abstract class SpeedMode
extends MinecraftInstance {
    public final String modeName;

    public SpeedMode(String modeName) {
        this.modeName = modeName;
    }

    public abstract void onMotion();

    public void onMotion(MotionEvent eventMotion) {
    }

    public abstract void onUpdate();

    public abstract void onMove(MoveEvent var1);

    public void onJump(JumpEvent event) {
    }

    public void onTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}

