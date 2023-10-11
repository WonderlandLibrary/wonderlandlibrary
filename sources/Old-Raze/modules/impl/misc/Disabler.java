package markgg.modules.impl.misc;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.PacketEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.util.timer.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;


@ModuleInfo(name = "Disabler", category = Module.Category.MISC)
public class Disabler extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Negativity V2");

	private boolean teleported = false;
	
	public void onEnable() {
		teleported = false;
	}
	
	@EventHandler
	private Listener<MotionEvent> motionEventListener = e -> {
		switch (mode.getMode()){
			case "Negativity V2":
				if(mc.thePlayer.ticksExisted % 20 == 0) {
					mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook());
					teleported = true;
				}
				break;
		}
	};
	
	@EventHandler
	private Listener<PacketEvent> packetEventListener = e -> {
		switch (mode.getMode()){
			case "Negativity V2":
				if(e.getPacket() instanceof S08PacketPlayerPosLook && this.teleported) {
					this.teleported = false;
					e.setCancelled(true);
					mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook) e.getPacket()).field_148938_b,
							((S08PacketPlayerPosLook) e.getPacket()).field_148939_c, ((S08PacketPlayerPosLook) e.getPacket()).field_148940_a,
							((S08PacketPlayerPosLook) e.getPacket()).field_148936_d, ((S08PacketPlayerPosLook) e.getPacket()).field_148937_e, true));
				}
				break;
		}
	};

}
