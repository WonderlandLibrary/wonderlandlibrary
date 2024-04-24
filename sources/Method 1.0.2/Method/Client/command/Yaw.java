package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

import java.math.BigInteger;

@Command.CommandFeatures(description = "Set Yaw", Syntax = "Yaw <Num>")

public class Yaw extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        long Yaw = new BigInteger(args[0]).longValue();
        mc.player.rotationYaw = Yaw;
        ChatUtils.message("Yaw =" + Yaw);
    }


}