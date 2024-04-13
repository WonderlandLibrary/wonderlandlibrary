package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;

public final class ToggleCommandT extends AbstractCommand {

    public ToggleCommandT() {
        super("t", "Enables or disables a specified module", ".t (module) | .toggle (module)", 1);
    }

    @Override
    public void onCommand(final String[] arguments) {
        try {
            final Module module = Tenacity.getInstance().getModuleRepository().getModuleByName(arguments[0]);
            module.toggle();
            ChatUtil.notify(module.getName() + " has been " + (module.isEnabled() ? "enabled" : "disabled") + "!");
        } catch (final UnknownModuleException unknownModuleException) {
            unknownModuleException.printErrorToChat();
        }
    }

}
