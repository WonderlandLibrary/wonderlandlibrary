package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

public final class BindCommand extends AbstractCommand {

    public BindCommand() {
        super("bind", "Sets the keybind of a specified module to a specified key", ".bind (module) (key) | .b (module) (key)", 2);
    }

    @Override
    public void onCommand(final String[] arguments) {
        try {
            final Module module = Tenacity.getInstance().getModuleRepository().getModuleByName(arguments[0]);
            module.setKeyCode(Keyboard.getKeyIndex(arguments[1].toUpperCase()));
            ChatUtil.print("Set keybind for " + module.getName() + " to " + arguments[1].toUpperCase() + "!");
        } catch (final UnknownModuleException unknownModuleException) {
            unknownModuleException.printErrorToChat();
        }
    }
}
