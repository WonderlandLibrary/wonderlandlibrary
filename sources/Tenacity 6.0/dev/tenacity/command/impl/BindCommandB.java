package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

public final class BindCommandB extends AbstractCommand {

    public BindCommandB() {
        super("b", "Sets the keybind of a specified module to a specified key", ".b (module) (key) | .bind (module) (key)", 2);
    }

    @Override
    public void onCommand(final String[] arguments) {
        try {
            final Module module = Tenacity.getInstance().getModuleRepository().getModuleByName(arguments[0]);
            module.setKeyCode(Keyboard.getKeyIndex(arguments[1].toUpperCase()));
            ChatUtil.notify("Set keybind for " + module.getName() + " to " + arguments[1].toUpperCase() + "!");
        } catch (final UnknownModuleException unknownModuleException) {
            unknownModuleException.printErrorToChat();
        }
    }
}
