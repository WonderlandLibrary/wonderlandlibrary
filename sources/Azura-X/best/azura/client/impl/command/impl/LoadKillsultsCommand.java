package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.module.impl.other.KillSults;
import net.minecraft.util.ChatComponentText;

public class LoadKillsultsCommand extends ACommand {
    @Override
    public String getName() {
        return "loadkillsults";
    }

    @Override
    public String getDescription() {
        return "Load kill sults from file";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "lk" };
    }


    @Override
    public void handleCommand(String[] args) {
        KillSults.loadKillInsults();
        mc.thePlayer.addChatMessage(new ChatComponentText(Client.PREFIX + "Loaded kill insults."));
    }
}
