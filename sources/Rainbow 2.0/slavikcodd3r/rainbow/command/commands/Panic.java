package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "panic" })
public class Panic extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	 for (final Module m : ModuleManager.getModules()) {
             try {
                 if (m.getDisplayName().equalsIgnoreCase("Commands") || !m.isEnabled()) {
                     continue;
                 }
                 m.disable();
             }
             catch (Exception e) {
            	 e.printStackTrace();
             }
         }
    }
    
    @Override
    public String getHelp() {
        return "panic";
    }
}
