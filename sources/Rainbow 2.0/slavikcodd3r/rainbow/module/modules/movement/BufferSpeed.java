package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "BufferSpeed")
public class BufferSpeed extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
        Blocks.ice.slipperiness = 0.4f;
        Blocks.packed_ice.slipperiness = 0.4f;
		super.enable();
	}
	
	public void disable() {
        Blocks.ice.slipperiness = 0.89f;
        Blocks.packed_ice.slipperiness = 0.89f;
		super.disable();
	}
}