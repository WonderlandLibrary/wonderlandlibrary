/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

public class Sprint
extends Mod {
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.thePlayer.moveForward > 0.0f && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) {
            Minecraft.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.setSprinting(false);
        super.onDisable();
    }
}

