/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    public NoRotate(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep;
        if (event instanceof EventPacket && (ep = (EventPacket)event).isIncoming() && ep.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook)ep.getPacket();
            pac.yaw = NoRotate.mc.thePlayer.rotationYaw;
            pac.pitch = NoRotate.mc.thePlayer.rotationPitch;
        }
    }
}

