package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class SinglePredeterminedOption extends CommandOptionOnephrase {
	
	private String choice;

	public SinglePredeterminedOption(String choice) {
		this.choice = choice;
	}
	
	@Override
	public String getSyntax() {
		return "§r" + choice;
	}
	
	public String getChoice() {
		return choice;
	}

}
