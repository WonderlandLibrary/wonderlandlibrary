// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import exhibition.management.notifications.Notifications;
import exhibition.management.command.Command;

public class NotificationTest extends Command
{
    public NotificationTest(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        final Notifications not = Notifications.getManager();
        if (args[0].equalsIgnoreCase("notify")) {
            not.post("Player Warning", "Some one called you a §chacker!", 0L, 2500L, Notifications.Type.NOTIFY);
        }
        else if (args[0].equalsIgnoreCase("warning")) {
            not.post("Warning Alert", "§cBob §fis now §6Vanished!", 0L, 2500L, Notifications.Type.WARNING);
        }
        else if (args[0].equalsIgnoreCase("info")) {
            not.post("Friend Info", "§bArithmo §fhas §cdied!", 0L, 2500L, Notifications.Type.INFO);
        }
        else if (args[0].equalsIgnoreCase("f")) {
            not.post("Friend Info", "§aA §fG §cD!", 0L, 2500L, Notifications.Type.INFO);
        }
        else if (args[0].equalsIgnoreCase("cgui")) {
            Client.resetClickGui();
        }
        else if (args[0].equalsIgnoreCase("font")) {
            Client.instance.setupFonts();
        }
        else {
            ChatUtil.printChat("§4[§cE§4]§8 ???");
        }
    }
    
    @Override
    public String getUsage() {
        return null;
    }
}
