package koks.command;

import koks.api.registry.command.Command;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "toggle", aliases = {"t"}, description = "toggle a module")
public class Toggle extends Command {

    @Override
    public boolean execute(String[] args) {
        if(args.length == 1) {
            final Module module = ModuleRegistry.getModule(args[0]);
            if(module != null) {
                module.toggle();
                sendMessage((module.isToggled() ? "§a" : "§c") + "Toggled §e" + module.getName());
            } else
                sendError("NOT EXIST", "§aModule §l" + args[0] + " §anot found!");
        } else if (args.length == 0){
            sendHelp(this, "[Module]");
        } else {
            return false;
        }
        return true;
    }
}
