package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import net.minecraft.client.Minecraft;

public class Username extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "username";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "gets your username";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".username";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		String username = Minecraft.getMinecraft().thePlayer.getName();
	if(Artemis.instance.nameprotect) {
		Artemis.addChatMessage("Username: Me");
	}else {
		
		Artemis.addChatMessage("Username: " + username);
	}
	}

}
