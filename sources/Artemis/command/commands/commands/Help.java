package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;

public class Help extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "help";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Gives out all comands";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".help";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		    Artemis.addChatMessage("--------------------------------------------------");
			Artemis.addChatMessage(".toggle <module> to toggle a module");
			Artemis.addChatMessage(".bind set <module> <key> to bind a module");
			Artemis.addChatMessage(".bind del <module> to delete a modules keybind");
			Artemis.addChatMessage(".bind clear to clear keybinds");
			Artemis.addChatMessage(".ping to show ping");
			Artemis.addChatMessage(".help to show list of commands");
			Artemis.addChatMessage(".hclip to clip horizontally");
			Artemis.addChatMessage(".vclip to clip vertically");
			Artemis.addChatMessage(".serverinfo to get info about the current server");
			Artemis.addChatMessage(".username to get your username");
			Artemis.addChatMessage(".say to say something in chat");
			Artemis.addChatMessage(".login <email/username> <password> to login to diffrent account");
			Artemis.addChatMessage(".damage to use the ncp damage exploit");
			Artemis.addChatMessage(".friend add [Name] [Alias]  to add a friend | .friend del [Name] to remove a friend | .friend clear to clear list of friends");
			Artemis.addChatMessage("--------------------------------------------------");
	}

}
