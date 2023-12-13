package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "WallSpeed")
public class WallSpeed extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onMove(final MoveEvent event) {
		 if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
			 event.setY(mc.thePlayer.motionY = 0);
			 ClientUtils.setMoveSpeed(event, Speed.getBaseMoveSpeed() * 2);
		 }
	}
	
	public void disable() {
		super.disable();
	}
}
