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

@Com(names = { "namechange", "nc" })
public class NameChange extends Command
{	
	private ServerData server;
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	this.server = mc.getCurrentServerData();
    	final Session s = mc.getSession();
        Minecraft.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
        mc.setSession(new Session(args[1], s.getPlayerID(), s.getToken(), s.getSessionType().name().toLowerCase()));
        mc.displayGuiScreen(new GuiConnecting(null, mc, this.server));
    }
    
    @Override
    public String getHelp() {
        return "namechange";
    }
}
