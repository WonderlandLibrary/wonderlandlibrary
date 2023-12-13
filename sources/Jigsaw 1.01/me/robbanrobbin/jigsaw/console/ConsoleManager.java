package me.robbanrobbin.jigsaw.console;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class ConsoleManager {
	
	public boolean showingResults = false;
	
	public String typed = "";
	
	public ArrayList<String> history = new ArrayList<String>();
	
	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 14) { // Backspace
			if (!typed.isEmpty()) {
				typed = typed.substring(0, typed.length() - 1);
			}
			return;
		}
		if (keyCode == 28 || keyCode == 42 || keyCode == 29) {
			return;
		}
		if(keyCode == 200 && !history.isEmpty()) {
			typed = history.get(history.size() - 1);
			return;
		}
		if ((keyCode > 1 && keyCode < 12) || (keyCode > 15 && keyCode < 26) || (keyCode > 29 && keyCode < 39)
				|| (keyCode > 43 && keyCode < 51) || keyCode == 12 || keyCode == 147) {
			typed += Character.toString(typedChar);
		}
		if(keyCode == 57) {
			typed += " ";
		}
	}
	
	public void tryRunCommand(String typed) {
		history.add(typed);
		Jigsaw.getCommandManager().tryRunCommand(typed);
	}
	
	public void resetTyped() {
		typed = "";
	}
	
}
