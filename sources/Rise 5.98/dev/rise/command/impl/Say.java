package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@CommandInfo(name = "Say", description = "Says the given message in chat", syntax = ".say <message>", aliases = "say")
public final class Say extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final String message = String.join(" ", args);

        if (mc.getNetHandler() != null) {
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(message));
            Rise.INSTANCE.getNotificationManager().registerNotification("Sent the given message.");
        } else {
            Rise.INSTANCE.getNotificationManager().registerNotification("Failed to send message.");
        }
    }
}
