package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

import java.math.BigInteger;

@Command.CommandFeatures(description = "Set Pitch", Syntax = "Pitch <Num>")

public class Pitch extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        long Pitch = new BigInteger(args[0]).longValue();
        mc.player.rotationPitch = Pitch;

        ChatUtils.message("Pitch = " + Pitch);

    }


}