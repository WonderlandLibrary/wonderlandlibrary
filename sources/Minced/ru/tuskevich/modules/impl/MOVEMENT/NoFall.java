// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoFall", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class NoFall extends Module
{
    @EventTarget
    public void onMotion(final EventMotion eventMotion) {
        final Minecraft mc = NoFall.mc;
        if (Minecraft.player.fallDistance < 3.0) {
            return;
        }
        eventMotion.setOnGround(true);
        final Minecraft mc2 = NoFall.mc;
        Minecraft.player.connection.sendPacket(new CPacketPlayer(true));
    }
}
