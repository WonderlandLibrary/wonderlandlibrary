package markgg.modules.impl.movement;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.PacketEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "NoFall", category = Module.Category.MOVEMENT)
public class NoFall extends Module{	

	public ModeSetting fallMode = new ModeSetting("Mode", this, "Packet", "Packet", "Verus", "AAC", "OnGround");

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			switch (fallMode.getMode()) {
			case "Packet":
				if (mc.thePlayer.fallDistance > 2.0f)
					mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
				break;
			case "Verus":
				if(mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3) {
					mc.thePlayer.motionY = 0.0;
					mc.thePlayer.fallDistance = 0.0f;
					mc.thePlayer.motionX *= 0.6;
					mc.thePlayer.motionZ *= 0.6;
					mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
				}
				break;
			case "AAC":
				if(mc.thePlayer.fallDistance >= 3){
					mc.timer.timerSpeed = 0.1f;
					mc.thePlayer.motionY -= 0.965F;
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,true));
				}else
					mc.timer.timerSpeed = 1.0f;
				break;
			case "OnGround":
				mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				break;
			}
		}
	};

	public void onDisable() {
		mc.timer.timerSpeed = 1;
	}
}
