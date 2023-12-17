// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.math.MathUtility;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "KTLeave", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd ReallyWorld", type = Type.MOVEMENT)
public class KTLeave extends Module
{
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        int x;
        int z;
        do {
            x = MathUtility.intRandom(2000, -2000);
            z = MathUtility.intRandom(2000, -2000);
        } while (x % 100 != 0 || z % 100 != 0);
        final int foundX = x;
        final int foundZ = z;
        final int y = 250;
        for (int i = 0; i <= 24; ++i) {
            final Minecraft mc = KTLeave.mc;
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(55555.0, 180.0, 55555.0, true));
        }
        for (int i = 0; i <= 24; ++i) {
            final Minecraft mc2 = KTLeave.mc;
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(foundX, 180.0, foundZ, true));
        }
        for (int i = 0; i <= 24; ++i) {
            final Minecraft mc3 = KTLeave.mc;
            final NetHandlerPlayClient connection = Minecraft.player.connection;
            final Minecraft mc4 = KTLeave.mc;
            final double posX = Minecraft.player.posX;
            final double yIn = 180.0;
            final Minecraft mc5 = KTLeave.mc;
            connection.sendPacket(new CPacketPlayer.Position(posX, yIn, Minecraft.player.posZ, true));
        }
        final Minecraft mc6 = KTLeave.mc;
        final NetHandlerPlayClient connection2 = Minecraft.player.connection;
        final Minecraft mc7 = KTLeave.mc;
        final double posX2 = Minecraft.player.posX;
        final Minecraft mc8 = KTLeave.mc;
        final double yIn2 = Minecraft.player.posY + 0.121599998474121;
        final Minecraft mc9 = KTLeave.mc;
        connection2.sendPacket(new CPacketPlayer.Position(posX2, yIn2, Minecraft.player.posZ, false));
        final Minecraft mc10 = KTLeave.mc;
        final NetHandlerPlayClient connection3 = Minecraft.player.connection;
        final Minecraft mc11 = KTLeave.mc;
        final double posX3 = Minecraft.player.posX;
        final Minecraft mc12 = KTLeave.mc;
        final double yIn3 = Minecraft.player.posY + 0.1623679977722166;
        final Minecraft mc13 = KTLeave.mc;
        connection3.sendPacket(new CPacketPlayer.Position(posX3, yIn3, Minecraft.player.posZ, false));
        final Minecraft mc14 = KTLeave.mc;
        final NetHandlerPlayClient connection4 = Minecraft.player.connection;
        final Minecraft mc15 = KTLeave.mc;
        final double posX4 = Minecraft.player.posX;
        final Minecraft mc16 = KTLeave.mc;
        final double yIn4 = Minecraft.player.posY + 0.123920636336059;
        final Minecraft mc17 = KTLeave.mc;
        connection4.sendPacket(new CPacketPlayer.Position(posX4, yIn4, Minecraft.player.posZ, false));
        final Minecraft mc18 = KTLeave.mc;
        final NetHandlerPlayClient connection5 = Minecraft.player.connection;
        final Minecraft mc19 = KTLeave.mc;
        final double posX5 = Minecraft.player.posX;
        final Minecraft mc20 = KTLeave.mc;
        final double yIn5 = Minecraft.player.posY + 0.0078422198694215;
        final Minecraft mc21 = KTLeave.mc;
        connection5.sendPacket(new CPacketPlayer.Position(posX5, yIn5, Minecraft.player.posZ, false));
        this.toggle();
    }
}
