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
import slavikcodd3r.rainbow.utils.MoveUtils;

@Module.Mod(displayName = "Sonic")
public class Sonic extends Module
{   
    @Op(name = "Speed", min = 0.0, max = 10.0, increment = 0.01)
    private double speed;
    @Op(name = "Bobbing")
    private boolean bobbing;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Sonic() {
		this.speed = 1.0;
	}
	
    public void enable() {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
		MoveUtils.setMotion(speed);
	}
}
