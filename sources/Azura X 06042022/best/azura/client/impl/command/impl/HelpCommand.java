package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ICommand;
import best.azura.client.util.other.ChatUtil;

import java.util.Arrays;

public class HelpCommand implements ICommand {
	
	@Override
	public String getName() {
		return "help";
	}
	
	@Override
	public String getDescription() {
		return "Shows this";
	}
	
	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public void handleCommand(String[] args) {
		for (ICommand iCommand : Client.INSTANCE.getCommandManager().getCommands())
			ChatUtil.sendChat("§l" + iCommand.getName() + "§r | §a" + iCommand.getDescription() + "§r | " + Arrays.toString(iCommand.getAliases()));
	}
}
