/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class AntiLevitation
extends Module {
    public AntiLevitation() {
        super("AntiLevitation", "", Category.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isPotionActive(Potion.getPotionById(25))) {
            Minecraft.player.removeActivePotionEffect(Potion.getPotionById(25));
        }
    }
}

