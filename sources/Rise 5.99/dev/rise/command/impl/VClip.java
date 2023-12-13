package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

@CommandInfo(name = "VClip", description = "Clips you on the vertical axis", syntax = ".vclip <distance>", aliases = "vclip")
public final class VClip extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args.length > 0) {
            final double dist = Double.parseDouble(args[0]);
            final String direction = dist > 0 ? "up" : "down";

            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + dist, mc.thePlayer.posZ);

            Rise.INSTANCE.getNotificationManager().registerNotification("VClipped you " + direction + " " + Math.abs(dist) + " blocks.");
        }
    }
}
