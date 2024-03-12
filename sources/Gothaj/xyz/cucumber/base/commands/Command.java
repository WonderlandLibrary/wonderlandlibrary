package xyz.cucumber.base.commands;

import xyz.cucumber.base.Client;

public class Command {
	
	private String name = this.getClass().getAnnotation(CommandInfo.class).name();
	
	private String[] aliases = this.getClass().getAnnotation(CommandInfo.class).aliases();
	
	private String usage = this.getClass().getAnnotation(CommandInfo.class).usage();
	
	public void onSendCommand(String[] args) {
		
	}
	
	public void sendUsage() {
		Client.INSTANCE.getCommandManager().sendChatMessage("§cUsage: "+usage);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}
}
