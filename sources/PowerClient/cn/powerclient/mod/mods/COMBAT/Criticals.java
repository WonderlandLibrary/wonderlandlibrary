/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals
extends Mod {
    private Value<String> mode = new Value("Criticals", "Mode", 0);

    public Criticals() {
        super("Criticals", Category.COMBAT);
        this.mode.mode.add("Packet");
        this.mode.mode.add("Hypixel");
    }

    @EventTarget
    public void onMove(EventMove event) {
        C02PacketUseEntity packet = event.getPacket();
        if (this.mode.isCurrentMode("Packet")) {
            this.setDisplayName("Packet");
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1625, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 4.0E-6, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-6, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
        }
    }

    public void onCriticalHit2() {
        if (Minecraft.thePlayer.onGround) {
            Minecraft.thePlayer.motionY = 0.0622;
        }
    }
}

