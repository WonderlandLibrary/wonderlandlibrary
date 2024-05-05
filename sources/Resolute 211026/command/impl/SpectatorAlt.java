// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import net.minecraft.client.Minecraft;
import vip.Resolute.command.Command;

public class SpectatorAlt extends Command
{
    public Minecraft mc;
    
    public SpectatorAlt() {
        super("Alt", "Sets mineplex alt", ".alt <email:pass>", new String[] { "alt" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            Resolute.instance.setAlt(args[0]);
            Resolute.getNotificationManager().add(new Notification("Success", "Set Account to " + args[0], 5000L, NotificationType.SUCCESS));
        }
    }
}
