package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class MultiplePredeterminedOptionsActivator extends CommandOptionActivator {
	
	private String[] choices;

	public MultiplePredeterminedOptionsActivator(String activator, String[] choices) {
		super(activator);
		this.choices = choices;
	}
	
	@Override
	public void runRaw(String[] args) {
		this.run(args[0]);
	}
	
	public abstract void run(String arg);

	@Override
	public String getSyntax() {
		String choicesString = "[";
		for(String choice : choices) {
			choicesString += choice + "/";
		}
		choicesString = choicesString.substring(0, choicesString.length() - 1);
		choicesString += "]";
		return getActivator() + " §r" + choicesString;
	}

}
