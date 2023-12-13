package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.NoCommandOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandCommands extends Command {

	@Override
	public String getActivator() {
		return "commands";
	}

	@Override
	public String getDesc() {
		return "Lists all commands";
	}

	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
				new NoCommandOption() {
					@Override
					public void run() {
						addResult("§6These are all available commands:");
						for (Command cmd : Jigsaw.getCommandManager().commands) {
							addResult("§b§l" + cmd.getActivator());
						}
					}
				}
		};
	}
}
