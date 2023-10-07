/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Damage
extends Command {
    static Minecraft mc = Minecraft.getMinecraft();

    public Damage(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        Damage.damagePlayer();
    }

    public static void damagePlayer() {
        for (int index = 0; index < 70; ++index) {
            Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY + 0.06, Damage.mc.thePlayer.posZ, false));
            Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY, Damage.mc.thePlayer.posZ, false));
        }
        Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY + 0.1, Damage.mc.thePlayer.posZ, false));
    }

    @Override
    public String getUsage() {
        return null;
    }

    public void onEvent(Event event) {
    }
}

