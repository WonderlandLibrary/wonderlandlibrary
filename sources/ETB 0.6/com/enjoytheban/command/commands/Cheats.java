package com.enjoytheban.command.commands;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.module.Module;
import com.enjoytheban.utils.Helper;

import net.minecraft.util.EnumChatFormatting;

public class Cheats extends Command {

	public Cheats() {
		super("Cheats", new String[] { "mods" }, "", "sketit");
	}

	@Override
	public String execute(String[] args) {
		// if the args are over a length of 2
		if (args.length == 0) {
			StringBuilder list = new StringBuilder(
					Client.instance.getModuleManager().getModules().size() + " Cheats - ");
			for (Module cheat : Client.instance.getModuleManager().getModules()) {
				list.append(cheat.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED).append(cheat.getName()).append(", ");
			}
			Helper.sendMessage("> "+list.toString().substring(0, list.toString().length() - 2));

		} else {
			Helper.sendMessage("> Correct usage .cheats");
		}
		return null;
	}
}
