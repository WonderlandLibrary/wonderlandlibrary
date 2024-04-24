package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

import java.math.BigInteger;

@Command.CommandFeatures(description = "Teleports you In the H.", Syntax = "Hclip <X> <Z>")

public class Hclip extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        long dir = new BigInteger(args[0]).longValue();
        long dir2 = new BigInteger(args[1]).longValue();

        float yaw = mc.player.rotationYaw;

        double newX = -Math.sin(Math.toRadians(yaw)) * dir + mc.player.posX;
        double newZ = Math.cos(Math.toRadians(yaw)) * dir2 + mc.player.posZ;

        mc.player.setPosition(newX, mc.player.posY, newZ);

        ChatUtils.message("Zoomed " + (dir + dir2) + " blocks.");

    }
}