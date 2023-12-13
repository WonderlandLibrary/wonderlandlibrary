package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

@ModuleInfo(name = "Debugger", description = "Debugs packets", category = Category.OTHER)
public final class Debugger extends Module {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S32PacketConfirmTransaction) {
            final S32PacketConfirmTransaction wrapper = ((S32PacketConfirmTransaction) packet);

            Rise.addChatMessage("Transaction | id: " + wrapper.getActionNumber());
        }

        if (packet instanceof S00PacketKeepAlive) {
            final S00PacketKeepAlive wrapper = ((S00PacketKeepAlive) packet);

            Rise.addChatMessage("KeepAlive | id: " + wrapper.func_149134_c());
        }
    }
}