/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.modules.command.other;

import io.netty.buffer.Unpooled;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class AntiForge
extends MinecraftInstance
implements Listenable {
    public static boolean enabled = true;
    public static boolean blockFML = true;
    public static boolean blockProxyPacket = true;
    public static boolean blockPayloadPackets = true;

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (enabled && !mc.func_71387_A()) {
            try {
                if (blockProxyPacket && packet.getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")) {
                    event.cancelEvent();
                }
                if (blockPayloadPackets && packet instanceof C17PacketCustomPayload) {
                    C17PacketCustomPayload customPayload = (C17PacketCustomPayload)packet;
                    if (!customPayload.func_149559_c().startsWith("MC|")) {
                        event.cancelEvent();
                    } else if (customPayload.func_149559_c().equalsIgnoreCase("MC|Brand")) {
                        customPayload.field_149561_c = new PacketBuffer(Unpooled.buffer()).func_180714_a("vanilla");
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

