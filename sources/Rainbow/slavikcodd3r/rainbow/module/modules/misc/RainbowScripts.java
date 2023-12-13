package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "RainbowScripts")
public class RainbowScripts extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		ClientUtils.sendMessage("Drop your .js script in .minecraft/Rainbow 2.0 folder");
		this.disable();
	}
}
