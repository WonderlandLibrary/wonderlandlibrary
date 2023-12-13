package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "Timer")
public class Timer extends Module
{   
    @Op(min = 0.05, max = 20.0, increment = 0.01, name = "Speed")
    public float speed;
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public Timer() {
    	speed = 1;
    }
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
        net.minecraft.util.Timer.timerSpeed = speed;
    }
	
	public void disable() {
		net.minecraft.util.Timer.timerSpeed = 1.0f;
		super.disable();
	}
}
