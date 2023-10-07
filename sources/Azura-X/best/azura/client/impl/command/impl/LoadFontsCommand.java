package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.ui.font.Fonts;
import net.minecraft.util.ChatComponentText;

public class LoadFontsCommand extends ACommand {
    @Override
    public String getName() {
        return "loadfonts";
    }

    @Override
    public String getDescription() {
        return "Load fonts";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "lf" };
    }


    @Override
    public void handleCommand(String[] args) {
        Fonts.INSTANCE.reload();
        mc.thePlayer.addChatMessage(new ChatComponentText(Client.PREFIX + "Loaded fonts."));
    }
}
