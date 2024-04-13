package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;

public final class ToggleCommand extends AbstractCommand {

    public ToggleCommand() {
        super("toggle", "Enables or disables a specified module", ".toggle (module) | .t (module)", 1);
    }

    @Override
    public void onCommand(final String[] arguments) {
        try {
            final Module module = Tenacity.getInstance().getModuleRepository().getModuleByName(arguments[0]);
            module.toggle();
            ChatUtil.print(module.getName() + " has been " + (module.isEnabled() ? "enabled" : "disabled") + "!");
        } catch (final UnknownModuleException unknownModuleException) {
            unknownModuleException.printErrorToChat();
        }
    }

}
