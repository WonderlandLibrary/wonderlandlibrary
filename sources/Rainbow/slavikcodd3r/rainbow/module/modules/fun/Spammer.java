package slavikcodd3r.rainbow.module.modules.fun;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "Spammer")
public class Spammer extends Module
{   
    @Op(name = "Delay", min = 0.0, max = 10000.0, increment = 0.01)
    private double delay;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Spammer() {
		this.delay = 50;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (mc.thePlayer.ticksExisted % 100 == 0) {
			mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage((int) (Math.random() * 1000000000) + " Rainbow 2.0 by SlavikCodd3r | the best Russian hacked client " + (int) (Math.random() * 1000000000)));
		}
	}
}
