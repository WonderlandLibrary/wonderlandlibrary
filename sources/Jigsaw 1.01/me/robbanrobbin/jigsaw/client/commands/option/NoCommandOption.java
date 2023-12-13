package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class NoCommandOption extends CommandOption {

	@Override
	public void runRaw(String[] args) {
		run();
	}
	
	public abstract void run();

	@Override
	public String getSyntax() {
		return "";
	}

}
