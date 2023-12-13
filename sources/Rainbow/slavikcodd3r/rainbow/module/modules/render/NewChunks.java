package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "NewChunks")
public class NewChunks extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {

	}
	
	@EventTarget
    private void onUpdate(final Render2DEvent event) {

	}
	
	@EventTarget
    private void onUpdate(final Render3DEvent event) {

	}
}
