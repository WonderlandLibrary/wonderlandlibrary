/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

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

public class SpeedMine
extends Module {
    public SpeedMine(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        SpeedMine.mc.playerController.blockHitDelay = 0;
        boolean item = SpeedMine.mc.thePlayer.getCurrentEquippedItem() == null;
        SpeedMine.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, item ? 1 : 0));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        SpeedMine.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }
}

