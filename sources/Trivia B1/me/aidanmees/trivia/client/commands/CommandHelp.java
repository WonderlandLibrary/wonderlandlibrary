package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.module.Module;

public class CommandHelp extends Command {

	@Override
	public void run(String[] commands) {
		boolean allMods = commands.length == 1;
		if (allMods) {
			trivia.chatMessage("These are all commands:");
			for (Command cmd : trivia.getCommandManager().commands) {
				trivia.chatMessage("§7" + cmd.getName() + "§6 §f" + cmd.getSyntax());
			}
		} else {
			if (!trivia.isCommand(commands[1]) && !trivia.isModuleName(commands[1])) {
				trivia.chatMessage("§cCould not find command or hack: " + commands[1]);
				return;
			}
			if(trivia.isCommand(commands[1])) {
				Command cmd = trivia.getCommandByName(commands[1]);
				trivia.chatMessage("§eName: §7" + cmd.getName());
				trivia.chatMessage("§eUsage: §7" + cmd.getSyntax());
				trivia.chatMessage("§eDescription: §7" + cmd.getDesc());
			}
			if(trivia.isModuleName(commands[1])) {
				Module mod = trivia.getModuleByName(commands[1]);
				String modes = "";
				for(String s : mod.getModes()) {
					modes += s + ", ";
				}
				if(mod.getModes().length == 1) {
					modes = "None, ";
				}
				trivia.chatMessage("§eName: §7" + mod.getName());
				trivia.chatMessage("§eModes: §7" + modes);
				trivia.chatMessage("§eDesc: §7" + mod.description);
			}
		}
	}

	@Override
	public String getActivator() {
		return ".help";
	}

	@Override
	public String getSyntax() {
		return ".help, .help <command>, .help <hack>";
	}

	@Override
	public String getDesc() {
		return "Gets help for all commands or for a specific command or hack";
	}
}
