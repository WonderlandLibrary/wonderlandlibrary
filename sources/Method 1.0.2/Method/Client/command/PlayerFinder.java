package Method.Client.command;


import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Objects;

@Command.CommandFeatures(description = "Get list of players.", Syntax = "pfind <all/creatives>")

public class PlayerFinder extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        ArrayList<String> list = new ArrayList<String>();

        if (args[0].equalsIgnoreCase("all")) {
            for (NetworkPlayerInfo npi : Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getPlayerInfoMap()) {
                list.add("\n" + npi.getGameProfile().getName());
            }
        } else if (args[0].equalsIgnoreCase("creatives")) {
            for (NetworkPlayerInfo npi : Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getPlayerInfoMap()) {
                if (npi.getGameType().isCreative()) {
                    list.add("\n" + npi.getGameProfile().getName());
                }
            }
        }

        if (list.isEmpty()) {
            ChatUtils.error("List is empty.");
        } else {
            Wrapper.INSTANCE.copy(list.toString());
            ChatUtils.message("List copied to clipboard.");
        }

    }

}