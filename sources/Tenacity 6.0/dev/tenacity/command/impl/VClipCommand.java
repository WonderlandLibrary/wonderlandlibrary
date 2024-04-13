package dev.tenacity.command.impl;

import dev.tenacity.command.AbstractCommand;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;

public final class VClipCommand extends AbstractCommand {

    public VClipCommand() {
        super("vclip", "vertical clip", ".vclip [distance]", 1);
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length != getNecessaryArguments()) {
            usage();
        } else {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.parseDouble(args[0]), mc.thePlayer.posZ);
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
