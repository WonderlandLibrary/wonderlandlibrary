package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.player.PlayerUtil;

@CommandInfo(name = "Sniper", description = "Sets the username of the person to snipe", syntax = ".sniper <username>", aliases = {"sniper", "snipe"})
public final class Sniper extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (!PlayerUtil.isOnServer("Hypixel") && !mc.isSingleplayer()) {
            Rise.INSTANCE.getNotificationManager().registerNotification("Sniper only works on Hypixel.");
            return;
        }

        if (args.length > 0) {
            dev.rise.module.impl.other.Sniper.username = args[0];
            Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the Sniper target.");
        } else {
            Rise.INSTANCE.getNotificationManager().registerNotification("Please input a username for the Sniper target.");
        }
    }
}
