package dev.tenacity.command.impl;

import dev.tenacity.command.AbstractCommand;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class HClip extends AbstractCommand {

    public HClip() {
        super("hclip", "horizontal clip", ".hclip [distance]", 1);
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length != getNecessaryArguments()) {
            usage();
        } else {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.thePlayer;
                double distance = Double.parseDouble(args[0]);

                float yaw = mc.thePlayer.rotationYaw;
                double radYaw = Math.toRadians(yaw);

                double xDir = -Math.sin(radYaw);
                double zDir = Math.cos(radYaw);

                double length = Math.sqrt(xDir * xDir + zDir * zDir);
                xDir /= length;
                zDir /= length;

                double newX = player.posX + xDir * distance;
                double newZ = player.posZ + zDir * distance;

                double newY = player.posY;

                mc.thePlayer.setPosition(newX, newY, newZ);
                ChatUtil.success("Clipped " + distance + " blocks!");
            } catch (NumberFormatException e) {
                usage();
            }
        }
    }

    private void usage() {
        ChatUtil.error("Usage: " + getUsage());
    }
}
