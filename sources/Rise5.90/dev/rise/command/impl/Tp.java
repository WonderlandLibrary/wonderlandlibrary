package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.misc.TPAura;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

@CommandInfo(name = "TP", description = "Teleports you to the position or player given", syntax = ".tp <location/name>", aliases = {"teleport", "tp"})
public final class Tp extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args.length == 3) {
            final double x = Double.parseDouble(args[0]);
            final double y = Double.parseDouble(args[1]);
            final double z = Double.parseDouble(args[2]);

            TPAura.tpToLocation(
                    500, 9.5, 9.0,
                    new ArrayList<>(), new ArrayList<>(),
                    new BlockPos(x, y, z)
            );

            mc.thePlayer.setPosition(x, y, z);

            Rise.INSTANCE.getNotificationManager().registerNotification("Teleported to the coordinates " + x + " " + y + " " + z + ".");
        } else {
            final EntityPlayer player = mc.theWorld.playerEntities
                    .stream()
                    .filter(entity -> entity.getGameProfile().getName().equalsIgnoreCase(args[0]))
                    .findFirst()
                    .orElse(null);

            if (player != null) {
                final double x = player.posX;
                final double y = player.posY;
                final double z = player.posZ;

                TPAura.tpToLocation(
                        500, 9.5, 9.0,
                        new ArrayList<>(), new ArrayList<>(),
                        new BlockPos(x, y, z)
                );

                mc.thePlayer.setPosition(x, y, z);

                Rise.INSTANCE.getNotificationManager().registerNotification("Successfully teleported to " + player.getName() + ".");
            } else {
                Rise.INSTANCE.getNotificationManager().registerNotification("Could not locate the player given!");
            }
        }
    }
}
