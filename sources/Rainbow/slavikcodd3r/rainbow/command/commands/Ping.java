package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetworkUtils;

@Com(names = { "p", "ping" })
public class Ping extends Command
{
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public void runCommand(final String[] args) {
    	if (!mc.isSingleplayer()) {
		ClientUtils.sendMessage(mc.currentServerData.pingToServer + " ms");
    	} else {
    	ClientUtils.sendMessage("0" + " ms");	
    	}
    }
    
    @Override
    public String getHelp() {
        return "ping";
    }
}
