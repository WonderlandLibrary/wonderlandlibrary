package tech.atani.client.feature.command.impl;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

@CommandInfo(name = "toggle", aliases = {"t"}, description = "toggle a module")
public class Toggle extends Command {

    @Override
    public boolean execute(String[] args) {
        if(args.length == 1) {
            final Module module = ModuleStorage.getInstance().getModule(args[0]);
            if(module != null) {
                module.toggle();
                sendMessage((module.isEnabled() ? "§a" : "§c") + "Toggled §e" + module.getName());
            } else
                sendError("DOES NOT EXIST", "§aModule §l" + args[0] + " §anot found!");
        } else if (args.length == 0){
            sendHelp(this, "[Module]");
        } else {
            return false;
        }
        return true;
    }
}