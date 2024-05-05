package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "version", "v", "serverversion", "v" })
public class Version extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	if (!mc.isSingleplayer()) {
    	ClientUtils.sendMessage(mc.getCurrentServerData().gameVersion);
    	} else {
        ClientUtils.sendMessage("null");	
    	}
    }
    
    @Override
    public String getHelp() {
        return "version";
    }
}
