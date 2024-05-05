package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "servericon", "si" })
public class ServerIcon extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	if (!mc.isSingleplayer()) {
    	ClientUtils.sendMessage(mc.getCurrentServerData().getBase64EncodedIconData());
    	} else {
        ClientUtils.sendMessage("Null");	
    	}
    }
    
    @Override
    public String getHelp() {
        return "servericon";
    }
}
