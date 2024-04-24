package Method.Client.command;

import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Creates a fake player", Syntax = "FakePlayer <Name>")
public class FakePlayer extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        Utils.createPlayer(mc.player, args[0], true);
        ChatUtils.message("Added Fake Player ");
    }

}