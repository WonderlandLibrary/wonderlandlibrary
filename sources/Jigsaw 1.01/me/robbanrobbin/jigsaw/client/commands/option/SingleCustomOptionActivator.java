package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class SingleCustomOptionActivator extends CommandOptionActivator {
	
	private String choiceName;

	public SingleCustomOptionActivator(String activator, String choiceName) {
		super(activator);
		this.choiceName = choiceName;
	}

	@Override
	public void runRaw(String[] args) {
		run(args[0]);
	}
	
	public abstract void run(String arg);
	
	@Override
	public String getSyntax() {
		return getActivator() + " §r<" + choiceName + ">";
	}

}
