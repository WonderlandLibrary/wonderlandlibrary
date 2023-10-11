package artemis.command.commands;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import net.minecraft.client.Minecraft;

public class Ping extends Command{
	  public final Minecraft mc = Minecraft.getMinecraft();
	@Override
	public String getAlias() {
		return "ping";
	}

	@Override
	public String getDescription() {
		return "Shows ping of user";
	}

	@Override
	public String getSyntax() {
		return ".ping";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		int Ping;
		if(mc.isSingleplayer()) {
    		Ping = 0;
    	}else {
    		Ping = (int) mc.getCurrentServerData().pingToServer;
    	}
			Artemis.addChatMessage("Your ping is: " + Ping);
		
	}

}
