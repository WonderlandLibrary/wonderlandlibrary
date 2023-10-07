/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(name = "ClientSpoofer", description = "Makes servers think you're on the specified client", category = Category.OTHER)
public final class ClientSpoofer extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Forge", "Forge", "Lunar", "LabyMod", "PvP Lounge", "CheatBreaker", "Geyser");

    @Override
    protected void onEnable() {
        this.registerNotification("Rejoin for " + this.getModuleInfo().name() + " to work.");
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            switch (mode.getMode()) {
                case "Forge": {
                    packet.setData(createPacketBuffer("FML", true));
                    break;
                }

                case "Lunar": {
                    packet.setChannel("REGISTER");
                    packet.setData(createPacketBuffer("Lunar-Client", false));
                    break;
                }

                case "LabyMod": {
                    packet.setData(createPacketBuffer("LMC", true));
                    break;
                }

                case "PvP Lounge": {
                    packet.setData(createPacketBuffer("PLC18", false));
                    break;
                }

                case "CheatBreaker": {
                    packet.setData(createPacketBuffer("CB", true));
                    break;
                }

                case "Geyser": {
                    packet.setData(createPacketBuffer("Geyser", false));
                    break;
                }
            }
        }
    }

    private PacketBuffer createPacketBuffer(final String data, final boolean string) {
        if (string)
            return new PacketBuffer(Unpooled.buffer()).writeString(data);
        else
            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
    }
}
