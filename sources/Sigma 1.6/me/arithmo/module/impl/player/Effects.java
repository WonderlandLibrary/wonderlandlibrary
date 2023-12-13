/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Effects
extends Module {
    public Effects(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            if (PlayerUtil.isOnLiquid() || PlayerUtil.isInLiquid()) {
                return;
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
            }
            if (Effects.mc.thePlayer.isBurning() && Effects.mc.thePlayer.isCollidedVertically) {
                for (int i = 0; i < 12; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
            for (Potion potion : Potion.potionTypes) {
                if (potion == null || !potion.isBadEffect() || !Effects.mc.thePlayer.isPotionActive(potion)) continue;
                PotionEffect activePotionEffect = Effects.mc.thePlayer.getActivePotionEffect(potion);
                for (int k = 0; k < activePotionEffect.getDuration() / 20; ++k) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
        }
    }
}

