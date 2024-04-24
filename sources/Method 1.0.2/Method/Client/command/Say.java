package Method.Client.command;

import Method.Client.utils.system.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;

@Command.CommandFeatures(description = "Send message to chat.", Syntax = "say <message>")

public class Say extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        StringBuilder content = new StringBuilder();
		for (String arg : args) {
			content.append(" ").append(arg);
		}
        Wrapper.INSTANCE.sendPacket(new CPacketChatMessage(content.toString()));
    }


}