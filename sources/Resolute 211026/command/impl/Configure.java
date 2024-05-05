// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import java.util.Iterator;
import vip.Resolute.config.SaveLoad;
import vip.Resolute.Resolute;
import vip.Resolute.command.Command;

public class Configure extends Command
{
    public Configure() {
        super("Config", "Save & Load Configurations", "config", new String[] { "cfg" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            final String action = args[0];
            if (action.equalsIgnoreCase("load")) {
                if (Resolute.getConfigManager().loadConfig(args[1])) {
                    this.success("loaded", args[1]);
                }
                else {
                    this.fail("load", args[1]);
                }
            }
            if (action.equalsIgnoreCase("save")) {
                if (Resolute.getConfigManager().saveConfig(args[1])) {
                    this.success("saved", args[1]);
                }
                else {
                    this.fail("save", args[1]);
                }
            }
            if (action.equalsIgnoreCase("delete")) {
                if (Resolute.getConfigManager().deleteConfig(args[1])) {
                    this.success("deleted", args[1]);
                }
                else {
                    this.fail("delete", args[1]);
                }
            }
            if (action.equalsIgnoreCase("list")) {
                Resolute.addChatMessage("Available Configs:");
                for (final SaveLoad config : Resolute.getConfigManager().getElements()) {
                    Resolute.addChatMessage(config.getName());
                }
            }
        }
    }
    
    private void success(final String type, final String configName) {
        Resolute.getNotificationManager().add(new Notification(String.format("Successfully %s config: '%s'", type, configName), 3000L, NotificationType.SUCCESS));
    }
    
    private void fail(final String type, final String configName) {
        Resolute.getNotificationManager().add(new Notification(String.format("Failed to %s config: '%s'", type, configName), 3000L, NotificationType.ERROR));
    }
}
