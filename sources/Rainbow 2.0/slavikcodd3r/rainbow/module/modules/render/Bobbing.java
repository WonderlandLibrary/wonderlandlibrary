package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "Bobbing")
public class Bobbing extends Module
{   
    @Op(name = "BobOnGround", min = 0.0, max = 30.0, increment = 0.01)
    private float bobonground;
    @Op(name = "BobInAir", min = 0.0, max = 30.0, increment = 0.01)
    private float bobinair;
	@Op(name = "BobOnGround")
	private boolean bobongroundo;
	@Op(name = "BobInAir")
	private boolean bobinairo;
	@Op(name = "NoBob")
	private boolean nobob;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Bobbing() {
		this.bobinair = 0.11f;
		this.bobonground = 0.11f;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (this.bobinairo && mc.thePlayer.onGround == false) {
		mc.thePlayer.cameraYaw = bobinair;
		}
		if (this.bobongroundo && mc.thePlayer.onGround == true) {
		mc.thePlayer.cameraYaw = bobonground;
		}
		if (this.nobob) {
			mc.thePlayer.distanceWalkedModified = 0.0f;
		}
	}
}
