package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class SayCommand extends ACommand {

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "Say a message";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s).append(' ');
        }
        if (builder.toString().endsWith(" ")) builder = new StringBuilder(builder.substring(0, builder.length() - 1));
        if (!builder.toString().isEmpty()) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C01PacketChatMessage(builder.toString()));
        }
    }
}