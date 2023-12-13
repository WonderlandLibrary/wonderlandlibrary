package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOptionActivator;
import me.robbanrobbin.jigsaw.client.commands.option.SinglePredeterminedOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandHelp extends Command {
	
	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
			new SingleCustomOptionActivator("get", "module") {
				@Override
				public void run(String arg) {
					if (!Jigsaw.isCommand(arg) && !Jigsaw.isModuleName(arg)) {
						addResult("§cCould not find command or hack: " + arg);
						return;
					}
					if(Jigsaw.isCommand(arg)) {
						Command cmd = Jigsaw.getCommandByName(arg);
						addResult("§jName: §r" + cmd.getName());
						addResult("§jUsage: §r" + cmd.getSyntax());
						addResult("§jDescription: §r" + cmd.getDesc());
					}
				}
			},
			new SinglePredeterminedOption("list") {
				@Override
				public void run() {
					addResult("These are all commands:");
					for (Command cmd : Jigsaw.getCommandManager().commands) {
						addResult("§8[§j" + cmd.getName() + "§8]§r: " + cmd.getSyntax());
					}
				}
			}
		};
	}

	@Override
	public String getActivator() {
		return "help";
	}

	@Override
	public String getSyntax() {
		return "help, help <command>, help <hack>";
	}

	@Override
	public String getDesc() {
		return "Gets help for all commands or for a specific command or hack";
	}
}
