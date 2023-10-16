package com.enjoytheban.command.commands;

import org.apache.commons.lang3.ObjectUtils.Null;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.module.Module;
import com.enjoytheban.utils.Helper;

import net.minecraft.util.EnumChatFormatting;

/** 
 * @author Purity
 * A basic toggle command for modules
 */

public class Toggle extends Command {	
	
	public Toggle() {
		super("t", new String[] {"toggle", "togl", "turnon", "enable"}, "", "Toggles a specified Module");
	}

	@Override
	public String execute(String[] args) {
		 String modName = "";
		 //if args is over length
	        if (args.length > 1) {
	            modName = args[1];
	        } else if(args.length < 1) {
	        	Helper.sendMessageWithoutPrefix("§bCorrect usage:§7 .t <module>");
	        }
	        boolean found = false;
	        //get all modules by alias
			Module m = Client.instance.getModuleManager().getAlias(args[0]);
			// if the mod could be found
			if (m != null) {
					if (!m.isEnabled())  {
						m.setEnabled(true);
					
					}	else {
						m.setEnabled(false);
					}
					found = true;
					if(m.isEnabled()) {
					//if enabled
					Helper.sendMessage("> "+m.getName() + EnumChatFormatting.GRAY + " was" + EnumChatFormatting.GREEN + " enabled");
				} else {
					//if not enabled
					Helper.sendMessage("> "+m.getName() + EnumChatFormatting.GRAY + " was" + EnumChatFormatting.RED + " disabled");
				}
				}
			//if not a valid mod
			if(found == false){
				Helper.sendMessage("> Module name " + EnumChatFormatting.RED +args[0] + EnumChatFormatting.GRAY  + " is invalid");
			}
			//return null
			return null;
	}
}
