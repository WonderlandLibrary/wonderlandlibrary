package koks.command;

import koks.api.registry.command.Command;
import net.minecraft.client.gui.GuiScreen;

@Command.Info(name = "copyip", aliases = {"cip"}, description = "You can copy the ip")
public class CopyIp extends Command {
    @Override
    public boolean execute(String[] args) {
        GuiScreen.setClipboardString(mc.isSingleplayer() ? "SinglePlayer" : mc.getCurrentServerData().serverIP);
        sendMessage("IP was saved in the clipboard!");
        return true;
    }
}
