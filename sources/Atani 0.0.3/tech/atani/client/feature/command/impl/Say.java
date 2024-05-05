package tech.atani.client.feature.command.impl;

import net.minecraft.network.play.client.C01PacketChatMessage;
import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

@CommandInfo(name = "say", description = "say stuff")
public class Say extends Command {
    @Override
    public boolean execute(String[] args) {
        if (args.length == 1) {
            String msg = args[0];
            getPlayer().sendQueue.addToSendQueue(new C01PacketChatMessage(msg));
            return true;
        } else if (args.length == 0) {
            sendHelp(this, "[Message]");
        } else {
            return false;
        }

        return true;
    }
}