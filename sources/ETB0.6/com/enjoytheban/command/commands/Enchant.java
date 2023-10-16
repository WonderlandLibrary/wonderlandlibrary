package com.enjoytheban.command.commands;

import com.enjoytheban.command.Command;
import com.enjoytheban.utils.Helper;

import net.minecraft.client.Minecraft;

/*
 * Created by Jutting on Nov 16, 2018
 */

public class Enchant extends Command {

	public Enchant() {
		super("Enchant", new String[] { "e" }, "", "enchanth");
	}

	@Override
	public String execute(String[] args) {
		if (args.length < 1) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/give " + Minecraft.getMinecraft().thePlayer.getName()
					+ " diamond_sword 1 0 {ench:[{id:16,lvl:127}]}");
		} else {
			Helper.sendMessage("invalid syntax " + "Valid .enchant");
		}
		return null;
	}
}