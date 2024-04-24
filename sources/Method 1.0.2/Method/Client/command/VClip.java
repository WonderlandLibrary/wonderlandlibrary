package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

import java.math.BigInteger;

@Command.CommandFeatures(description = "Teleports you up/down.", Syntax = "vclip <height>")

public class VClip extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        mc.player.setPosition(mc.player.posX,
                mc.player.posY + new BigInteger(args[0]).longValue(), mc.player.posZ);
        ChatUtils.message("Height teleported to " + new BigInteger(args[0]).longValue());
    }

}