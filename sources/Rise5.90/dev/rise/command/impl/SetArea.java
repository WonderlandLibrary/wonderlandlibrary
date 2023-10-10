package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.pathfinding.Vec3;

@CommandInfo(name = "SetArea", description = "Sets the area to nuke for area Nuker mode", syntax = ".setarea <1/2/reset>", aliases = "setarea")
public final class SetArea extends Command {

    public static Vec3 pos1, pos2;

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0] != null) {
            switch (args[0].toLowerCase()) {
                case "1":
                    pos1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                    Rise.INSTANCE.getNotificationManager().registerNotification("Set position one to " + ((int) mc.thePlayer.posX) + " " + (Math.round(mc.thePlayer.posY) - 1) + " " + ((int) mc.thePlayer.posZ) + ".");
                    break;

                case "2":
                    pos2 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                    Rise.INSTANCE.getNotificationManager().registerNotification("Set position two to " + ((int) mc.thePlayer.posX) + " " + (Math.round(mc.thePlayer.posY) - 1) + " " + ((int) mc.thePlayer.posZ) + ".");
                    break;

                case "reset":
                    pos1 = pos2 = null;
                    Rise.INSTANCE.getNotificationManager().registerNotification("Successfully reset the positions.");
                    break;

                default:
                    Rise.INSTANCE.getNotificationManager().registerNotification("Invalid argument.");
                    break;
            }
        } else {
            Rise.INSTANCE.getNotificationManager().registerNotification("Please input an argument.");
        }
    }
}
