/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

@ModuleInfo(name = "PacketDebugger", description = "Debugger for detecting anticheats", category = Category.OTHER)
public final class PacketDebugger extends Module {
    private long lastKeepAlive;
    private long lastTransaction;

    @Override
    public void onPacketSend(PacketSendEvent event) {

        Packet p = event.getPacket();

        if (p instanceof C0FPacketConfirmTransaction) {
            long lastPacket = System.currentTimeMillis() - lastTransaction;
            Rise.addChatMessage("Transaction: " + ((C0FPacketConfirmTransaction) event.getPacket()).getWindowId()
                    + " " + ((C0FPacketConfirmTransaction) event.getPacket()).getUid() + " " + lastPacket + "ms");
            this.lastTransaction = System.currentTimeMillis();
        } else if (p instanceof C00PacketKeepAlive) {
            long lastPacket = System.currentTimeMillis() - lastKeepAlive;
            Rise.addChatMessage("KeepAlive: " + ((C00PacketKeepAlive) event.getPacket()).getKey() + " " + lastPacket + "ms");
            this.lastKeepAlive = System.currentTimeMillis();
        } else if (p instanceof S3FPacketCustomPayload) {
            Rise.addChatMessage("Payload: " + ((S3FPacketCustomPayload) event.getPacket()).getChannelName());
        }

    }
}
