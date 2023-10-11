package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import net.minecraft.client.Minecraft;

public class Say extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "say";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "says something in chat";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".say <word>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		Minecraft.getMinecraft().thePlayer.sendChatMessage(args[0]);
		Artemis.addChatMessage("Sent chat message!");
	}

}
