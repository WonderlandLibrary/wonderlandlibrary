package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "reconnect", "rc" })
public class Reconnect extends Command
{	
	private ServerData server;
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	this.server = mc.getCurrentServerData();
        Minecraft.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
        mc.displayGuiScreen(new GuiConnecting(null, mc, this.server));
    }
    
    @Override
    public String getHelp() {
        return "reconnect";
    }
}
