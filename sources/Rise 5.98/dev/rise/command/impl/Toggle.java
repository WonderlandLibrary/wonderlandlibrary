package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.module.Module;

@CommandInfo(name = "Toggle", description = "Toggles the specified module", syntax = ".t <module>", aliases = {"toggle", "t"})
public final class Toggle extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final Module[] modules = Rise.INSTANCE.getModuleManager().getModuleList();

        for (final Module module : modules) {
            if (args[0].equalsIgnoreCase(module.getModuleInfo().name())) {
                module.toggleModule();
                Rise.INSTANCE.getNotificationManager().registerNotification((module.isEnabled() ? "Enabled " : "Disabled ") + module.getModuleInfo().name() + ".");
                return;
            }
        }

        Rise.INSTANCE.getNotificationManager().registerNotification("Couldn't find the module.");
    }
}
