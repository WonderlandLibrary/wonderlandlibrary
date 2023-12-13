/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import java.util.Objects;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class NoBlindness
extends Module {
    public NoBlindness() {
        super("NoBlindness", Category.Visuals);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        Minecraft.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(15)));
        Minecraft.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(2)));
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

