package me.robbanrobbin.jigsaw.client.commands;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.CommandOptionActivator;
import me.robbanrobbin.jigsaw.client.commands.option.MultiplePredeterminedOptionsActivator;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOption;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOptionActivator;
import me.robbanrobbin.jigsaw.client.commands.option.SinglePredeterminedOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.module.Module;

public class CommandManager {

	public ArrayList<Command> commands = new ArrayList<Command>();
	
	private ArrayList<String> results = new ArrayList<String>();
	
	private boolean runningCommand = false;

	public CommandManager() {
		
		commands.add(new CommandBleach());
		commands.add(new CommandCommands());
		commands.add(new CommandCoords());
		commands.add(new CommandCrash());
		
		
		commands.add(new CommandHelp());
		
//		commands.add(new CommandSay());
//		commands.add(new CommandToggle());
//		commands.add(new CommandVclip());
//		commands.add(new CommandDamage());
//		commands.add(new CommandFakehacker());
//		commands.add(new CommandNameprotect());
//		commands.add(new CommandDos());
//		commands.add(new CommandFriend());
//		commands.add(new CommandServerVersion());
//		commands.add(new CommandFakeMessage());
//		commands.add(new CommandServerIp());
		
		
		for(Module mod : Jigsaw.getModulesByCategories(Jigsaw.getModules(), Jigsaw.defaultCategoriesWithTarget)) {
			commands.add(new Command() {
				
				@Override
				public String getDesc() {
					return mod.description;
				}
				
				@Override
				public String getActivator() {
					return mod.getName().replaceAll(" ", "");
				}

				@Override
				public CommandOption[] setOptions() {
					ArrayList<CommandOption> optionList = new ArrayList<CommandOption>();
					
					if(mod.getCommandOptions() != null) {
						for(CommandOption option : mod.getCommandOptions()) {
							optionList.add(option);
						}
					}
					
					optionList.add(new SinglePredeterminedOption("toggle") {
						@Override
						public void run() {
							mod.toggle();
						}
					});
					
					optionList.add(new SingleCustomOptionActivator("bind", "key") {
						@Override
						public void run(String arg) {
							mod.setKeyCode(Keyboard.getKeyIndex(arg.toUpperCase()));
						}
					});
					
					if(mod.getModes().length > 1) {
						optionList.add(new MultiplePredeterminedOptionsActivator("mode", mod.getModes()) {
							@Override
							public void run(String arg) {
								mod.setMode(arg);
							}
						});
					}
					
					return optionList.toArray(new CommandOption[optionList.size()]);
				}
			});
		}
		
	}
	
	public ArrayList<CommandSuggestion> getSuggestions(String typed) {
		ArrayList<CommandSuggestion> suggestions = new ArrayList<CommandSuggestion>();
		
		String[] splittedTyped = typed.split(" ");
		if(splittedTyped.length == 0) {
			return suggestions;
		}
		String cmdActivator = splittedTyped[0];
		String optionActivator = null;
		String optionPhrase = null;
		if(splittedTyped.length >= 2) {
			optionActivator = typed.split(" ")[1];
		}
		if(splittedTyped.length >= 3) {
			optionPhrase = typed.split(" ")[2];
		}
		
		if(!typed.isEmpty()) {
			for(Command command : commands) {
				if(command.getActivator().toUpperCase().startsWith(typed.toUpperCase())) {
					suggestions.add(new CommandSuggestion(command.getActivator() + "§8 - §r" + command.getDesc(), typed));
				}
			}
			for(Command command : commands) {
				if(command.getActivator().equalsIgnoreCase(cmdActivator)) {
					suggestions.clear();
					for(CommandOption option : command.getOptions()) {
						if(optionActivator == null) {
							suggestions.add(new CommandSuggestion(command.getActivator() + " §7" + option.getSyntax(), typed));
						}
						else {
							if(option instanceof CommandOptionActivator) {
								if(((CommandOptionActivator) option).getActivator().toUpperCase().startsWith(optionActivator.toUpperCase())) {
									suggestions.add(new CommandSuggestion(command.getActivator() + " §7" + option.getSyntax(), typed));
								}
							}
							if(option instanceof SingleCustomOption) {
								if(((SingleCustomOption) option).getChoiceName().toUpperCase().startsWith(optionActivator.toUpperCase())) {
									suggestions.add(new CommandSuggestion(command.getActivator() + " §7" + option.getSyntax(), typed));
								}
							}
							if(option instanceof SinglePredeterminedOption) {
								if(((SinglePredeterminedOption) option).getChoice().toUpperCase().startsWith(optionActivator.toUpperCase())) {
									suggestions.add(new CommandSuggestion(command.getActivator() + " §7" + option.getSyntax(), typed));
								}
							}
						}
					}
				}
			}
		}
		
		return suggestions;
	}

	private boolean onCommand(String activator, String[] args) {
		for (Command cmd : this.commands) {
			if (cmd.getActivator().equalsIgnoreCase(activator)) {
				cmd.run(args);
				return true;
			}
		}
		return false;
	}
	
	public void tryRunCommand(String typed) {
		String[] commands = Jigsaw.getConsoleManager().typed.trim().split("\\s++");
		String[] args = new String[commands.length - 1];
		for(int i = 1; i < commands.length; i++) {
			args[i - 1] = commands[i];
		}
		runningCommand = true;
		boolean success = Jigsaw.getCommandManager().onCommand(commands[0], args);
		runningCommand = false;
		if (!success) {
			addResult("§cCould not find command!");
		}
	}
	
	public boolean isRunningCommand() {
		return runningCommand;
	}
	
	public ArrayList<String> getResults() {
		return results;
	}
	
	public void addResult(String result) {
		if(isRunningCommand()) {
			this.results.add(result);
		}
	}

	public void clearResults() {
		results.clear();
	}

}
