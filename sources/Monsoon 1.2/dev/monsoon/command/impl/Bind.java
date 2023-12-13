package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Binds a module", "bind <name> <key> | clear", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length == 2) {
            String moduleName = args[0];
            String keyName = args[1];
            boolean foundMod = false;

            for(Module m : Monsoon.modules) {
                if(m.name.equalsIgnoreCase(moduleName)) {
                    m.setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    NotificationManager.show(new Notification(NotificationType.INFO, "Bind", "Bound " + moduleName + " to " + keyName + ".", 2));
                    if(Monsoon.saveLoad != null) {
                        Monsoon.saveLoad.save();
                    }
                    foundMod = true;
                    break;
                }
            }
            if(!foundMod) {
                NotificationManager.show(new Notification(NotificationType.ERROR, "Toggle", "Could not find module " + moduleName + "!", 2));
            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("clear")) {
                for(Module m : Monsoon.modules) {
                    m.setKey(0);
                }
                NotificationManager.show(new Notification(NotificationType.INFO, "Bind", "Cleared binds", 2));
                if(Monsoon.saveLoad != null) {
                    Monsoon.saveLoad.save();
                }
            }
        }
    }
}
