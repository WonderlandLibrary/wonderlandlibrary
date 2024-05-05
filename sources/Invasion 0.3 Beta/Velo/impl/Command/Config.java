package Velo.impl.Command;

import java.io.File;
import java.util.List;


import Velo.api.Command.Command;
import Velo.api.Main.Main;
import Velo.api.Util.Other.ChatUtil;


public class Config extends Command {
	
   public Config() {
      super("Config", "Save, load and delete configs.", "config <save/load/delete/list/refresh> <name(optional)>", "config", "cfg");
   }    String configName;



@Override
public void onCommand(String[] args, String command) {
    if (args.length < 1) {
        ChatUtil.addChatMessage("Config Manager Commands");
        ChatUtil.addChatMessage(".config load <name>");
        ChatUtil.addChatMessage(".config save <name>");
        ChatUtil.addChatMessage(".config delete <name>");
        ChatUtil.addChatMessage(".config list");
        return;
    }

    if (args.length >= 2) {
        configName = args[1];
    }

    if (args[0].equalsIgnoreCase("load")) {
    	Main.cfgManager.load(configName);
        ChatUtil.addChatMessage("Config \"" + configName + "\" was loaded.");
    } else if (args[0].equalsIgnoreCase("save")) {
    	Main.cfgManager.save(configName);
        ChatUtil.addChatMessage("Current config has been saved as \"" + configName + "\"");
    } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
    	Main.cfgManager.delete(configName);
        ChatUtil.addChatMessage("The config \"" + configName + "\" has been removed");
    } else if (args[0].equalsIgnoreCase("list")) {
  
        ChatUtil.addChatMessage("List of configs:");
        String[] pathnames = Main.cfgManager.file.list();
 
	   for(String pathname : pathnames) {
            // Print the names of files and directories
            ChatUtil.addChatMessage(pathname);
	   }
	   
	   

   
        
    } else {
    	ChatUtil.addChatMessage("+ Command Not Found!");
        ChatUtil.addChatMessage("+ Config Manager Commands");
        ChatUtil.addChatMessage("+ .config load <name>");
        ChatUtil.addChatMessage("+ .config save <name>");
        ChatUtil.addChatMessage("+ .config delete <name>");
       ChatUtil.addChatMessage("+ .config list");
    }

}
}
