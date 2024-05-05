package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.world.WorldSettings;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;

@Module.Mod(displayName = "MusicPlayer")
public class MusicPlayer extends Module
{
	public void enable() {

		super.enable();
	}
	
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	
    }
    
    public void disable() {
    	
    	super.disable();
    }
}