package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.util.ChatComponentText;

public class KMSCommand extends ACommand {
	@Override
	public String getName() {
		return "killmyself";
	}

	@Override
	public String getDescription() {
		return "Instantly die";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"kms"};
	}


	@Override
	public void handleCommand(String[] args) {
		MovementUtil.damagePlayerHypixel(7);
		mc.thePlayer.addChatMessage(new ChatComponentText(Client.PREFIX + "You died."));
	}
}
