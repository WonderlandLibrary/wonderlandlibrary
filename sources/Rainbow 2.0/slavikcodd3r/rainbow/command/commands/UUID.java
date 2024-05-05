package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MojangUtils;

@Com(names = { "uuid" })
public class UUID extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	try {
    		ClientUtils.sendMessage(MojangUtils.getUUID(args[1]));
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		ClientUtils.sendMessage("Error: " + e.fillInStackTrace());
    	}
    }
    
    @Override
    public String getHelp() {
        return "uuid";
    }
}
