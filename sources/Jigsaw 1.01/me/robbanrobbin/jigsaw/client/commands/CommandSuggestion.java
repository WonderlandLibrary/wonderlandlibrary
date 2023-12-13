package me.robbanrobbin.jigsaw.client.commands;

public class CommandSuggestion {
	
	public String command;
	
	public String matchedString;
	
	public CommandSuggestion(String command, String matchedString) {
		this.command = command;
		this.matchedString = matchedString;
	}
	
}
