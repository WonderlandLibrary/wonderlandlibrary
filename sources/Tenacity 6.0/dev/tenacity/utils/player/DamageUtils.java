// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.utils.Utils;

public class DamageUtils implements Utils
{
    public static void damage(final DamageType type) {
        if (DamageUtils.mc.thePlayer == null) {
            return;
        }
        final double x = DamageUtils.mc.thePlayer.posX;
        final double y = DamageUtils.mc.thePlayer.posY;
        final double z = DamageUtils.mc.thePlayer.posZ;
        switch (type) {
            case WATCHDOGUP: {
                for (int i = 0; i < 49; ++i) {
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY + 0.0625, DamageUtils.mc.thePlayer.posZ, false));
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY, DamageUtils.mc.thePlayer.posZ, false));
                }
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY, DamageUtils.mc.thePlayer.posZ, true));
                break;
            }
            case WATCHDOGDOWN: {
                for (int i = 0; i < 49; ++i) {
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY - 0.0625, DamageUtils.mc.thePlayer.posZ, false));
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY, DamageUtils.mc.thePlayer.posZ, false));
                }
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(DamageUtils.mc.thePlayer.posX, DamageUtils.mc.thePlayer.posY, DamageUtils.mc.thePlayer.posZ, true));
                break;
            }
            case NCP: {
                for (int i = 0; i <= MovementUtils.getMaxFallDist() / 0.0625; ++i) {
                    send(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, false));
                    send(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                send(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                break;
            }
            case VANILLA: {
                send(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.01, z, false));
                send(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                send(new C03PacketPlayer(true));
                break;
            }
            case VERUS: {
                for (int i = 0; i < 3; ++i) {
                    for (final double offset : new double[] { 0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.24918707874468, 1.1707870772188, 1.0155550727022, 0.78502770378924, 0.4807108763317, 0.10408037809304 }) {
                        send(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
                    }
                    send(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                send(new C03PacketPlayer(true));
            }
            case SUFFOCATE: {
                send(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 2.0, z, false));
                break;
            }
        }
    }
    
    private static void send(final Packet<?> packet) {
        PacketUtils.sendPacketNoEvent(packet);
    }
    
    public enum DamageType
    {
        WATCHDOGUP, 
        WATCHDOGDOWN, 
        NCP, 
        VANILLA, 
        VERUS, 
        SUFFOCATE;
    }
}
