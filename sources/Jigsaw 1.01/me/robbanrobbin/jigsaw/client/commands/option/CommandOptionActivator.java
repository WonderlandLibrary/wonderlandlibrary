package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class CommandOptionActivator extends CommandOption {
	
	private String activator;
	
	public CommandOptionActivator(String activator) {
		this.activator = activator;
	}

	public String getActivator() {
		return activator;
	}
	
}
