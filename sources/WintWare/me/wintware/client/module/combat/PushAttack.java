/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class PushAttack
extends Module {
    public PushAttack() {
        super("AutoClicker", Category.Combat);
    }

    @EventTarget
    public void onPreMotion(EventUpdate event) {
        if (Minecraft.player.getCooledAttackStrength(0.0f) == 1.0f && PushAttack.mc.gameSettings.keyBindAttack.pressed) {
            mc.clickMouse();
        }
    }
}

