package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetworkUtils;

@Module.Mod(displayName = "IP")
public class IP extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		if (!mc.isSingleplayer()) {
	    	ClientUtils.sendMessage(mc.getCurrentServerData().serverIP);
	    	} else {
	        ClientUtils.sendMessage("localhost");	
	    	}
	}
}
