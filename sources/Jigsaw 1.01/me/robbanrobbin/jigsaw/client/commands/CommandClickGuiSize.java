package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOptionActivator;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class CommandClickGuiSize extends Command {
	
	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
				new SingleCustomOptionActivator("set", "size") {
					@Override
					public void run(String arg) {
						ClientSettings.clickGuiFontSize = Integer.parseInt(arg);
					}
				},
				new SingleCustomOptionActivator("reset", "blablatest") {
					@Override
					public void run(String arg) {
						ClientSettings.clickGuiFontSize = Integer.parseInt(arg);
					}
				}
		};
	}

	@Override
	public String getActivator() {
		return "fontsize";
	}

	@Override
	public String getDesc() {
		return "Sets the font size for the ClickGUI. Default is 13.";
	}
}
