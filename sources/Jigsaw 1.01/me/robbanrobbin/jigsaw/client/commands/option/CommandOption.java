package me.robbanrobbin.jigsaw.client.commands.option;

import me.robbanrobbin.jigsaw.client.commands.Command;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public abstract class CommandOption {
	
	private Command parent;
	
	public void setParent(Command parent) {
		this.parent = parent;
	}
	
	public abstract void runRaw(String[] args);
	
	public Command getParent() {
		return parent;
	}
	
	public abstract String getSyntax();
	
	public String getDescription() {
		return null;
	}
	
	protected void addResult(String result) {
		Jigsaw.getCommandManager().addResult(result);
	}
	
}
