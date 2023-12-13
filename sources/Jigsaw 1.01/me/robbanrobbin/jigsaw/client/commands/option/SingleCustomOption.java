package me.robbanrobbin.jigsaw.client.commands.option;

public abstract class SingleCustomOption extends CommandOptionOnephrase {
	
	private String choiceName;

	public SingleCustomOption(String choiceName) {
		this.choiceName = choiceName;
	}
	
	@Override
	public String getSyntax() {
		return "§r<" + choiceName + ">";
	}
	
	public String getChoiceName() {
		return choiceName;
	}

}
