package com.enjoytheban.command.commands;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.module.Module;
import com.enjoytheban.utils.Helper;

import net.minecraft.util.EnumChatFormatting;

/**
 * Bind command
 * @author Purity
 */

public class Bind extends Command {

	public Bind() {
		super("Bind", new String[] {"b"}, "", "sketit");
	}

	@Override
	public String execute(String[] args) {
		//if the args are over a length of 2
		if (args.length >= 2) {
			//grab the module by alias
			Module m = Client.instance.getModuleManager().getAlias(args[0]);
			//if module found
			if (m != null) {
				int k = Keyboard.getKeyIndex(args[1].toUpperCase());
				//set the bind
				m.setKey(k);
				//print msg
				Helper.sendMessage(
						String.format("> Bound %s to %s", m.getName(), (k == 0 ? "none" : args[1].toUpperCase())));
			} else {
				Helper.sendMessage("> Invalid module name, double check spelling.");
			}
		} else {
        	Helper.sendMessageWithoutPrefix("§bCorrect usage:§7 .bind <module> <key>");
		}
		return null;
	}
}