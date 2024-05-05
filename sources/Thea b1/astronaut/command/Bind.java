package astronaut.command;



import astronaut.Duckware;
import astronaut.modules.ModuleManager;
import org.lwjgl.input.Keyboard;

import javax.swing.plaf.DimensionUIResource;

public class Bind extends Command {
    public Bind() {
        super(".bind");
    }

    @Override
    public void performAction(String message) {
        String[] command = message.split(" ");
        if (command.length < 3) {

        } else {
            if (Duckware.moduleManager.getModuleByName(command[1]) == null) {
            } else if (command[2].length() > 1) {
            } else {
                Duckware.moduleManager.getModuleByName(command[1]).setKeyBind(Keyboard.getKeyIndex(command[2].toUpperCase()));
                Duckware.moduleManager.sendChatMessage(": Bound " + command[1] + " to " + command[2].toUpperCase());
            }
        }
    }
}