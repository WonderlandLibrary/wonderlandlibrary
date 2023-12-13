package de.tired.base.command.impl;

import de.tired.base.annotations.CommandAnnotation;

import de.tired.base.command.Command;
import de.tired.base.interfaces.IHook;
import de.tired.base.module.Module;
import de.tired.Tired;
import de.tired.util.log.IngameChatLog;

@CommandAnnotation(name = "Toggle", help = "ToggleSuss", alias = {"toggle", "t"})
public class Toggle extends Command implements IHook {

    @Override
    public void doCommand(String[] args) {
        if (args.length != 1) {
            IngameChatLog.INGAME_CHAT_LOG.doLog("You can toggle with .t name or with .toggle name");
        }

        for (Module module : Tired.INSTANCE.moduleManager.getModuleList()) {
            if (!args[0].equalsIgnoreCase(module.getName())) continue;
            module.executeMod();
            IngameChatLog.INGAME_CHAT_LOG.doLog(module.getName() + " was toggled");
            break;
        }
        super.doCommand(args);
    }

}
