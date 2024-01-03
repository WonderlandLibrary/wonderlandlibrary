package de.theBest.MythicX.command;

import de.theBest.MythicX.MythicX;


public class Toggle extends Command{

    public Toggle() {
        super(".t");
    }

    @Override
    public void performAction(String message) {
        String[] command = message.split(" ");
        if (command.length < 2) {
        } else {
            if (MythicX.moduleManager.getModuleByName(command[1]) == null) {

            } else {
                MythicX.moduleManager.getModuleByName(command[1]).toggle();
                MythicX.moduleManager.sendChatMessage(": "+ (MythicX.moduleManager.getModuleByName(command[1]).isToggled() ? "" + "Enabled " + MythicX.moduleManager.getModuleByName(command[1]).getName() : "Disabled " + MythicX.moduleManager.getModuleByName(command[1]).getName()));
            }
        }
    }
}
