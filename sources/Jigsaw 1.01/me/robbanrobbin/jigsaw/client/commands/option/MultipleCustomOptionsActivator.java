package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class MultipleCustomOptionsActivator extends CommandOptionActivator {
	
	private String[] choiceNames;

	public MultipleCustomOptionsActivator(String activator, String... choiceNames) {
		super(activator);
		this.choiceNames = choiceNames;
	}
	
	@Override
	public void runRaw(String[] args) {
		if(args.length != choiceNames.length) {
			addResult("§cNot enough arguments!");
			return;
		}
		run(args);
	}
	
	public abstract void run(String[] args);
	
	@Override
	public String getSyntax() {
		String syntax = getActivator();
		for(String choice : choiceNames) {
			syntax += " §r<" + choice + ">";
		}
		return syntax;
	}

}
