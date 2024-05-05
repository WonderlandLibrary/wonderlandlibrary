/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MISC;

import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpeedMine
extends Mod {
    public static Value mode = new Value("SpeedMine", "Mode", 0);

    public SpeedMine() {
        super("SpeedMine", Category.MISC);
        SpeedMine.mode.mode.add("Potion");
    }

    @Override
    public void onEnable() {
        if (mode.isCurrentMode("Potion")) {
            boolean item;
            Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, (item = false) ? 2 : 0));
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }
}

