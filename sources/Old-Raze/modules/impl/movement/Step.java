package markgg.modules.impl.movement;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

@ModuleInfo(name = "Step", category = Module.Category.MOVEMENT)
public class Step extends Module {

	public ModeSetting stepMode = new ModeSetting("Step Mode", this, "Vanilla", "Vanilla", "Motion", "Motion2", "SetPos");
	public NumberSetting stepHeight = new NumberSetting("Height", this, 1, 0, 10, 0.5);


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			switch (stepMode.getMode()) {
				case "Vanilla":
					mc.thePlayer.stepHeight = (float) stepHeight.getValue();
					break;
				case "Motion":
					mc.thePlayer.stepHeight = 0.6F;
					if(mc.thePlayer.isCollidedHorizontally) {
						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ);
						mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, false));
						mc.thePlayer.jump();
					}
					break;
				case "Motion2":
					mc.thePlayer.stepHeight = 0.6F;
					if (mc.thePlayer.isCollidedHorizontally)
						mc.thePlayer.motionY = 0.2f;
					break;
				case "SetPos":
					mc.thePlayer.stepHeight = 0.6F;
					if(mc.thePlayer.isCollidedHorizontally)
						mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + stepHeight.getValue(), mc.thePlayer.posZ);
					break;
			}
		}
	};
	
	public void onDisable(){
		mc.thePlayer.stepHeight = 0.6F;
		mc.timer.timerSpeed = 1;			 
	}
}
