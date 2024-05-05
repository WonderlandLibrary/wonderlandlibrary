package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "ip" })
public class IP extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	if (!mc.isSingleplayer()) {
    	ClientUtils.sendMessage(mc.getCurrentServerData().serverIP);
    	} else {
        ClientUtils.sendMessage("localhost");	
    	}
    }
    
    @Override
    public String getHelp() {
        return "ip";
    }
}
