package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.NoCommandOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandBleach extends Command {

	@Override
	public String getActivator() {
		return "bleach";
	}

	@Override
	public String getDesc() {
		return "Kills you by fall damage.";
	}

	@Override
	public CommandOption[] setOptions() {
		return new CommandOption[] {
				new NoCommandOption() {
					@Override
					public void run() {
						Jigsaw.getModuleByName("Bleach").setToggled(true, true);
					}
				}
		};
	}
	
}
