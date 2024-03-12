package de.theBest.MythicX.command;



import de.theBest.MythicX.MythicX;
import org.lwjgl.input.Keyboard;

import static de.theBest.MythicX.MythicX.fileUtil;

public class Bind extends Command {
    public Bind() {
        super(".bind");
    }

    @Override
    public void performAction(String message) {
        String[] command = message.split(" ");
        if (command.length < 3) {

        } else {
            if (MythicX.moduleManager.getModuleByName(command[1]) == null) {
            } else if (command[2].length() > 1) {
            } else {
                MythicX.moduleManager.getModuleByName(command[1]).setKeyBind(Keyboard.getKeyIndex(command[2].toUpperCase()));
                MythicX.moduleManager.sendChatMessage(": Bound " + command[1] + " to " + command[2].toUpperCase());
                fileUtil.saveKeys();
            }
        }
    }
}