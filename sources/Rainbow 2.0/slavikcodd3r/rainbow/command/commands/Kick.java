package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "kick", "k" })
public class Kick extends Command
{
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public void runCommand(final String[] args) {
    	mc.playerController.attackEntity(mc.thePlayer, mc.thePlayer);
    }
    
    @Override
    public String getHelp() {
        return "kick";
    }
}
