package astronaut.command;

import astronaut.Duckware;


public class Toggle extends Command{

    public Toggle() {
        super(".t");
    }

    @Override
    public void performAction(String message) {
        String[] command = message.split(" ");
        if (command.length < 2) {
        } else {
            if (Duckware.moduleManager.getModuleByName(command[1]) == null) {

            } else {
                Duckware.moduleManager.getModuleByName(command[1]).toggle();
                Duckware.moduleManager.sendChatMessage(": "+ (Duckware.moduleManager.getModuleByName(command[1]).isToggled() ? "" + "Enabled " + Duckware.moduleManager.getModuleByName(command[1]).getName() : "Disabled " + Duckware.moduleManager.getModuleByName(command[1]).getName()));
            }
        }
    }
}
