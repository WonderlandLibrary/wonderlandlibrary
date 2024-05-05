/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright
extends Mod {
    public FullBright() {
        super("FullBright", Category.RENDER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.removePotionEffect(Potion.nightVision.getId());
        super.onDisable();
    }
}

