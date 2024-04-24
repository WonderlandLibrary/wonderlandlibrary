package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Gives your BedCoords", Syntax = "BedCoords")
public class BedCoords extends Command {
    @Override
    public void runCommand(String s, String[] args) {
        ChatUtils.message(mc.player.getBedLocation().toString());
    }
}