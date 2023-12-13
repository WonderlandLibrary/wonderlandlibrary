package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "antifreeze" })
public class AntiFreeze extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	mc.thePlayer.isDead = false;
    }
    
    @Override
    public String getHelp() {
        return "antifreeze";
    }
}
