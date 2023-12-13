package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.world.WorldSettings;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;

@Module.Mod(displayName = "Radio")
public class Radio extends Module
{
    @Op(name = "FM", min = 87.5, max = 108.0, increment = 0.01)
    private double FM;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Radio() {
		this.FM = 87.5;
	}
	
	public void enable() {

		super.enable();
	}
	
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        this.setSuffix(new StringBuilder(String.valueOf(FM + " FM")).toString());
    	
    }
    
    public void disable() {
    	
    	super.disable();
    }
}