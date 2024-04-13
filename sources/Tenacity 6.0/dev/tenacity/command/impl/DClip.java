package dev.tenacity.command.impl;

import dev.tenacity.command.AbstractCommand;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;

public final class DClip extends AbstractCommand {

    public DClip() {
        super("dclip", "diagonal clip", ".dclip [distance]", 1);
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length != getNecessaryArguments()) {
            usage();
        } else {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                mc.thePlayer.setPosition(mc.thePlayer.posX + Double.parseDouble(args[0]), mc.thePlayer.posY, mc.thePlayer.posZ + Double.parseDouble(args[0]));
                ChatUtil.success("Clipped " + Double.parseDouble(args[0]) + " blocks!");
            } catch (NumberFormatException e) {
                usage();
            }
        }
    }

    private void usage() {
        ChatUtil.error("Usage: " + getUsage());
    }
}
