package slavikcodd3r.rainbow.module.modules.fun;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0APacketAnimation;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option;

@Module.Mod (displayName = "AnimationsSpam")
public class AnimationsSpam extends Module
{  
    @Option.Op(name = "Delay", min = 1, max = 20, increment = 1)
    public long delay;
	Minecraft mc = Minecraft.getMinecraft();
	
	public AnimationsSpam() {
		this.delay = 1;
	}
	
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
    	if (mc.thePlayer.ticksExisted % delay == 0) {
    	mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
    	}
    }
}