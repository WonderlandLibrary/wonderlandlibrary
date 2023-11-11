package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.other.ChatUtils;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggles a module by name.", "toggle <module>", "t");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            String name = args[0];
            Module module = flush.getModuleManager().getModule(name);

            if (module != null) {
                module.toggle();
                ChatUtils.println((module.isEnabled() ? "Enabled " : "Disabled ") + module.getName() + ".");
            } else {
                ChatUtils.println("Module \"" + name + "\" not found.");
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
