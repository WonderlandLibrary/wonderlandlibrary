package de.theBest.MythicX.command;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.utils.FileUtil;
import org.lwjgl.input.Keyboard;

import static de.theBest.MythicX.MythicX.fileUtil;

public class Config extends Command{
    public Config() {
        super(".config");
    }
    @Override
    public void performAction(String message) {
        String[] command = message.split(" ");
        if(command[1].equalsIgnoreCase("load")) {
            fileUtil.LoadConfig(command[2]);
        }else if(command[1].equalsIgnoreCase("save")){
            fileUtil.saveConfig(command[2]);
            MythicX.moduleManager.sendChatMessage("done");
        }

    }
}
