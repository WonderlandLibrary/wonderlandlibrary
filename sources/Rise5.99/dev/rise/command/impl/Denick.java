package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.notifications.NotificationType;
import dev.rise.util.sniping.AntisniperWrapper;

import java.util.Objects;

@CommandInfo(name = "Denick", description = "Find's a nicks real IGN on Hypixel using a database", syntax = ".denick <nick>", aliases = {"denick", "dn"})
public final class Denick extends Command {
    private long lastDenick = 0;

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (System.currentTimeMillis() - lastDenick < 2500) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Please wait before using this again!", 5000, NotificationType.ERROR);
            return;
        }
        lastDenick = System.currentTimeMillis();

        if (Objects.equals(args[0], "")) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Enter a nick to denick", 5000, NotificationType.ERROR);
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String realIgn = AntisniperWrapper.denick(args[0]);
                if (realIgn != null) {
                    if (realIgn.equals(args[0])) {
                        Rise.INSTANCE.getNotificationManager().registerNotification(args[0] + " isn't a nick", 5000, NotificationType.NOTIFICATION);
                        return;
                    }
                    Rise.INSTANCE.getNotificationManager().registerNotification(args[0] + "'s real IGN is " + realIgn, 7500, NotificationType.NOTIFICATION);
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification(args[0] + " couldn't be denicked! Double check their name is correct", 5000, NotificationType.ERROR);
                }
            }
        });
        thread.start();
    }
}
