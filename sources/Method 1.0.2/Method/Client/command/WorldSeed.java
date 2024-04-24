package Method.Client.command;

import Method.Client.utils.SeedViewer.WorldLoader;
import Method.Client.utils.visual.ChatUtils;

import java.math.BigInteger;

@Command.CommandFeatures(description = "WorldSeed", Syntax = "WorldSeed <seed>")

public class WorldSeed extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        long Seed = new BigInteger(args[0]).longValue();
        WorldLoader.seed = Seed;
        ChatUtils.message("Seed = " + Seed);

    }

}