/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.features.special;

import java.text.MessageFormat;
import java.util.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BungeeCordSpoof
extends MinecraftInstance
implements Listenable {
    public static boolean enabled = false;

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C00Handshake && enabled && ((C00Handshake)packet).func_149594_c() == EnumConnectionState.LOGIN) {
            C00Handshake handshake = (C00Handshake)packet;
            handshake.field_149598_b = handshake.field_149598_b + "\u0000" + MessageFormat.format("{0}.{1}.{2}.{3}", this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart(), this.getRandomIpPart()) + "\u0000" + mc.func_110432_I().func_148255_b().replace("-", "");
        }
    }

    private String getRandomIpPart() {
        return new Random().nextInt(2) + "" + new Random().nextInt(5) + "" + new Random().nextInt(5);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

