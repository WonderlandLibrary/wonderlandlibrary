package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggle a module", ".toggle <name>", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 0) {
            String moduleName = args[0];

            boolean found = false;

            for(Module m : Monsoon.modules) {
                if(m.name.equalsIgnoreCase(moduleName)) {
                    m.toggle();

                    found = true;
                    break;
                }
            }
            if(!found) {
                NotificationManager.show(new Notification(NotificationType.ERROR, "Toggle", "Could not find module " + moduleName + "!", 1));
            }
        }
    }
}
