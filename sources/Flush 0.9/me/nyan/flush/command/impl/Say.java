package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Say extends Command {
    public Say() {
        super("Say", "Sends a message in chat.", "Say <message>");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(buildStringFromArgs(args)));
            return;
        }

        sendSyntaxHelpMessage();
    }
}
