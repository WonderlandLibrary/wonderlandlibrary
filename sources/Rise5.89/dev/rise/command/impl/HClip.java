package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

@CommandInfo(name = "HClip", description = "Clips you on the horizontal axis", syntax = ".hclip <distance>", aliases = {"hclip", "hc"})
public final class HClip extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args.length > 0) {
            final double dist = Double.parseDouble(args[0]);
            final String direction = dist > 0 ? "forward" : "back";

            final double rotation = Math.toRadians(mc.thePlayer.rotationYaw);

            final double x = Math.sin(rotation) * dist;
            final double z = Math.cos(rotation) * dist;

            mc.thePlayer.setPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ + z);

            Rise.INSTANCE.getNotificationManager().registerNotification("HClipped you " + direction + " " + dist + " blocks.");
        }
    }
}
