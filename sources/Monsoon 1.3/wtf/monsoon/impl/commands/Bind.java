package wtf.monsoon.impl.commands;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.command.Command;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;

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

            for(Module m : Monsoon.INSTANCE.manager.modules) {
                if(m.name.equalsIgnoreCase(moduleName)) {
                    m.setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    NotificationManager.show(new Notification(NotificationType.INFO, "Bind", "Bound " + moduleName + " to " + keyName + ".", 2));
                    if(Monsoon.INSTANCE.saveLoad != null) {
                        Monsoon.INSTANCE.saveLoad.save();
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
                for(Module m : Monsoon.INSTANCE.manager.modules) {
                    m.setKey(0);
                }
                NotificationManager.show(new Notification(NotificationType.INFO, "Bind", "Cleared binds", 2));
                if(Monsoon.INSTANCE.saveLoad != null) {
                    Monsoon.INSTANCE.saveLoad.save();
                }
            }
        }
    }
}
