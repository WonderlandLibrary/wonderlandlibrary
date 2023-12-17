// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoServerRotations", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class NoRotation extends Module
{
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook sPacketPlayerPosLook;
            final SPacketPlayerPosLook packet = sPacketPlayerPosLook = (SPacketPlayerPosLook)eventPacket.getPacket();
            final Minecraft mc = NoRotation.mc;
            sPacketPlayerPosLook.yaw = Minecraft.player.rotationYaw;
            final SPacketPlayerPosLook sPacketPlayerPosLook2 = packet;
            final Minecraft mc2 = NoRotation.mc;
            sPacketPlayerPosLook2.pitch = Minecraft.player.rotationPitch;
        }
    }
}
