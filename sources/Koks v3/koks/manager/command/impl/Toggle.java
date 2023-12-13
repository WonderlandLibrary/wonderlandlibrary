package koks.manager.command.impl;

import koks.Koks;
import koks.manager.command.Command;
import koks.manager.command.CommandInfo;
import koks.manager.module.Module;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:57
 */

@CommandInfo(name = "toggle", alias = "t")
public class Toggle extends Command {

    @Override
    public void execute(String[] args) {
        if(args.length == 1) {
            Module module = Koks.getKoks().moduleManager.getModule(args[0]);
            if(module != null) {
                module.toggle();
                sendmsg((module.isToggled() ? "§a" : "§c") + "Toggled §e" + module.getName(), true);
            }else{
                sendError("Module", args[0] + " not found!");
            }
        }else{
            sendError("Usage", ".toggle [Module]");
        }
    }
}
