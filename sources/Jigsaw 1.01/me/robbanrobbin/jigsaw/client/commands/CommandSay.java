package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;

public class CommandSay extends Command {

	@Override
	public String getActivator() {
		return "say";
	}

	@Override
	public String getDesc() {
		return "Enables you to type \".legit\" or anything in chat: \"say .legit\"";
	}

	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
				
		};
	}
}
