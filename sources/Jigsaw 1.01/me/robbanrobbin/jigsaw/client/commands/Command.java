package me.robbanrobbin.jigsaw.client.commands;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.CommandOptionActivator;
import me.robbanrobbin.jigsaw.client.commands.option.NoCommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.SinglePredeterminedOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public abstract class Command {
	
	public Command() {
		CommandOption[] optionArray = setOptions();
		if(optionArray != null) {
			for(int i = 0; i < optionArray.length; i++) {
				optionArray[i].setParent(this);
				options.add(optionArray[i]);
			}
		}
	}
	
	private ArrayList<CommandOption> options = new ArrayList<CommandOption>();

	protected Minecraft mc = Minecraft.getMinecraft();

	public abstract String getActivator();

	public abstract String getDesc();

	public String getName() {
		return getActivator();
	}

	public String[] getSyntaxes() {
		String[] syntaxes = new String[options.size()];
		int i = 0;
		for(CommandOption option : options) {
			
			syntaxes[i] = getActivator() + " " + option.getSyntax();
			
			i++;
		}
		return syntaxes;
	}

	public void run(String[] args) {
		if(args.length != 0) {
			if(args.length != 1) {
				for(CommandOption option : options) { //Run through all activators
					if(option instanceof CommandOptionActivator) {
						if(((CommandOptionActivator)option).getActivator().equals(args[0])) {
							String[] strippedArgs = new String[args.length - 1];
							for(int i = 1; i < args.length; i++) {
								strippedArgs[i - 1] = args[i];
							}
							((CommandOptionActivator)option).runRaw(strippedArgs);
							return;
						}
					}
				}
			}
			for(CommandOption option : options) { //If it isn't an activator, treat it as a one-phrase argument
				if(option instanceof SinglePredeterminedOption) {
					if(((SinglePredeterminedOption) option).getChoice().equals(args[0])) {
						option.runRaw(args);
						return;
					}
				}
			}
		}
		for(CommandOption option : options) { //Run through all activators
			if(option instanceof NoCommandOption) {
				option.runRaw(args);
				return;
			}
		}
		addResult("§cSyntax error!");
	}
	
	public ArrayList<CommandOption> getOptions() {
		return options;
	}

	public void sendPacketFinal(Packet packet) {
		mc.myNetworkManager.sendPacketFinal(packet);
	}

	public void sendPacket(Packet packet) {
		mc.myNetworkManager.sendPacket(packet);
	}
	
	public Command addOption(CommandOption commandOption) {
		options.add(commandOption);
		return this;
	}
	
	public String getSyntax() {
		String syntax = "";
		for(int i = 0; i < getSyntaxes().length; i++) {
			syntax += getSyntaxes()[i];
			if(i != getSyntaxes().length - 1) {
				syntax += ", ";
			}
		}
		return syntax;
	}
	
	public abstract CommandOption[] setOptions();
	
	public void addResult(String result) {
		Jigsaw.getCommandManager().addResult(result);
	}
	
}
