package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventManager;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.module.modules.player.AlwaysGround;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "AirJump")
public class AirJump extends Module
{   
    @Op(name = "New")
    private boolean newmod;
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		if (this.newmod) {
			mc.thePlayer.jump();
		} else {
		super.enable();
		}
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (!this.newmod) {
		mc.thePlayer.onGround = true;
		}
	}
}
