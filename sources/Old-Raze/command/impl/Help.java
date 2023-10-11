package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.command.CommandManager;
import markgg.modules.Module;

public class Help extends Command {

	public Help() {
		super("Help", "Shows basic helpful information", "help", "h");
	}

	@Override
	public void onCommand(String[] args, String command) {
		RazeClient.addChatMessage();
		RazeClient.addChatMessage("Version " + RazeClient.INSTANCE.getVersion());
		RazeClient.addChatMessage();
		for(Command command1 : RazeClient.INSTANCE.getCmdmanager().commands) {
			RazeClient.addChatMessage(RazeClient.INSTANCE.getCmdmanager().prefix + command1.syntax + " - " + command1.desc);
		}
		RazeClient.addChatMessage();
	}

}
