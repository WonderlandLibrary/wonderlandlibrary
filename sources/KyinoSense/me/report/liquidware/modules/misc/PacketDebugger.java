/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 *  net.minecraft.network.play.server.S3FPacketCustomPayload
 */
package me.report.liquidware.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import obfuscator.NativeMethod;

@ModuleInfo(name="PacketDebugger", description="Debugger for detecting packets.", category=ModuleCategory.FUN)
public final class PacketDebugger
extends Module {
    private long lastKeepAlive;
    private long lastTransaction;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent event) {
        Packet<?> p = event.getPacket();
        if (p instanceof C0FPacketConfirmTransaction) {
            long lastPacket = System.currentTimeMillis() - this.lastTransaction;
            ClientUtils.displayChatMessage("[PacketDebugger] Transaction: " + ((C0FPacketConfirmTransaction)event.getPacket()).func_149532_c() + " " + ((C0FPacketConfirmTransaction)event.getPacket()).func_149533_d() + " " + lastPacket + "ms");
            this.lastTransaction = System.currentTimeMillis();
        } else if (p instanceof C00PacketKeepAlive) {
            long lastPacket = System.currentTimeMillis() - this.lastKeepAlive;
            ClientUtils.displayChatMessage("[PacketDebugger] KeepAlive: " + ((C00PacketKeepAlive)event.getPacket()).func_149460_c() + " " + lastPacket + "ms");
            this.lastKeepAlive = System.currentTimeMillis();
        } else if (p instanceof S3FPacketCustomPayload) {
            ClientUtils.displayChatMessage("[PacketDebugger] Payload: " + ((S3FPacketCustomPayload)event.getPacket()).func_149169_c());
        }
    }
}

