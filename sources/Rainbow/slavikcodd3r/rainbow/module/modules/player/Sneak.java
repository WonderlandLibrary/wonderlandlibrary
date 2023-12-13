package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Sneak")
public class Sneak extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	public void disable() {
		ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		super.disable();
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		 if (mc.thePlayer.ticksExisted % 5 == 0.0) {
			 ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		 } else {
			 ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		 }
	}
}
