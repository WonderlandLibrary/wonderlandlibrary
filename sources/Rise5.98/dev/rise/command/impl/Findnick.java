package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.notifications.NotificationType;
import dev.rise.util.sniping.AntisniperWrapper;

import java.util.Objects;

@CommandInfo(name = "Findnick", description = "Find's a players' nick on Hypixel using a database", syntax = ".findnick <ign>", aliases = {"findnick", "fn"})
public final class Findnick extends Command {
    private long lastFindnick = 0;

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (System.currentTimeMillis() - lastFindnick < 2500) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Please wait before using this again!", 5000, NotificationType.ERROR);
            return;
        }
        lastFindnick = System.currentTimeMillis();

        if (Objects.equals(args[0], "")) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Enter a name to find the nick of", 5000, NotificationType.ERROR);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String nick = AntisniperWrapper.findnick(args[0]);
                if (nick != null) {
                    if (nick.equals(args[0])) {
                        Rise.INSTANCE.getNotificationManager().registerNotification(args[0] + " isn't a real player! Did you mean to denick them instead?", 5000, NotificationType.NOTIFICATION);
                        return;
                    }
                    Rise.INSTANCE.getNotificationManager().registerNotification(args[0] + "'s nick is " + nick, 7500, NotificationType.NOTIFICATION);
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("Couldn't find the nick for " + args[0] + "! They might not be in the database", 5000, NotificationType.ERROR);
                }
            }
        });
        thread.start();
    }
}
