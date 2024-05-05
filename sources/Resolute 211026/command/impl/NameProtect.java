// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import vip.Resolute.modules.impl.player.StreamerMode;
import vip.Resolute.command.Command;

public class NameProtect extends Command
{
    public NameProtect() {
        super("NameProtect", "Sets NameProtect name", ".name <username>", new String[] { "name" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            StreamerMode.name = args[0];
            Resolute.getNotificationManager().add(new Notification("Success", "Set name to " + args[0], 5000L, NotificationType.SUCCESS));
        }
    }
}
