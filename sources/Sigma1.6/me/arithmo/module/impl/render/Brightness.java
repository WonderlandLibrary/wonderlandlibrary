/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Brightness
extends Module {
    public Brightness(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        Brightness.mc.playerController.blockHitDelay = 0;
        boolean item = Brightness.mc.thePlayer.getCurrentEquippedItem() == null;
        Brightness.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Brightness.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
    }
}

