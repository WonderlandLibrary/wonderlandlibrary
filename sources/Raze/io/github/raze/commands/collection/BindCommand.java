package io.github.raze.commands.collection;

import io.github.raze.Raze;
import io.github.raze.commands.system.Command;
import io.github.raze.modules.system.AbstractModule;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "Bind modules to keys", "bind <Module> <Key>", "b");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length < 3) {
            return "Usage: " + getSyntax();
        }

        AbstractModule module = Raze.INSTANCE.managerRegistry.moduleRegistry.getModule(arguments[1]);

        if (module != null) {
            int keyCode = Keyboard.getKeyIndex(arguments[2].toUpperCase());

            module.setKeyCode(keyCode);

            return String.format("Successfully bound %s to the key %s.", module.getName(), Keyboard.getKeyName(keyCode));
        } else {
            return String.format("Could not find module %s", arguments[1]);
        }
    }




}
