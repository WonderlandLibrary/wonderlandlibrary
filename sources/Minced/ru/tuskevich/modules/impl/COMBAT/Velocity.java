// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Velocity", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class Velocity extends Module
{
    @EventTarget
    public void onReceive(final EventPacket e) {
        if (e.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity)e.getPacket();
            final int entityID = packet.getEntityID();
            final Minecraft mc = Velocity.mc;
            if (entityID == Minecraft.player.getEntityId()) {
                e.cancel();
            }
        }
        if (e.getPacket() instanceof SPacketExplosion) {
            e.cancel();
        }
    }
}
