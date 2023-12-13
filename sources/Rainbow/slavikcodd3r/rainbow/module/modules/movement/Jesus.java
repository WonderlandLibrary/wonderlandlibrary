package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Jesus")
public class Jesus extends Module
{   
    @Op(name = "Bobbing")
    private boolean bobbing;
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (mc.thePlayer.isInWater()) {
	    	if (this.bobbing) {
	    		mc.thePlayer.cameraYaw = 0.11f;
	    	}
            mc.thePlayer.onGround = true;
            mc.thePlayer.motionY = 0.0;
            ClientUtils.mc().gameSettings.keyBindJump.pressed = true;
        }
    }
}
