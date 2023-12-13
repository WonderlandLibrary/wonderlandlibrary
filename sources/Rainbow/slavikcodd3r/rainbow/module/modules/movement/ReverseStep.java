package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "ReverseStep")
public class ReverseStep extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		mc.thePlayer.motionY = -1.635;
	}
}
