package markgg.modules.impl.movement;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.util.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

@ModuleInfo(name = "CustomSpeed", category = Module.Category.MOVEMENT)
public class CustomSpeed extends Module {

	private int onGroundPackets;

	public NumberSetting moreOnGroundPackets = new NumberSetting("OnGround Packets", this, 0, 0, 20, 1);
	public NumberSetting strafeSpeed = new NumberSetting("Strafe Speed", this, 0.3, 0, 1, 0.1);
	public NumberSetting motion = new NumberSetting("Motion", this, 0.42, 0, 2, 0.01);
	public NumberSetting boost = new NumberSetting("Boost", this, 1, 0, 2, 0.1);
	public NumberSetting timerSpeed = new NumberSetting("Timer Speed", this, 1, 0, 10, 0.01);
	public BooleanSetting autoJump = new BooleanSetting("Auto Jump", this, true);

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
			mc.timer.timerSpeed = (float) timerSpeed.getValue();
			if (!MoveUtil.isMoving())
				MoveUtil.stop();

			mc.thePlayer.motionX *= boost.getValue();
			mc.thePlayer.motionZ *= boost.getValue();
			if (strafeSpeed.getValue() != 0)
				MoveUtil.strafe((float) strafeSpeed.getValue());

			if (mc.thePlayer.onGround) {
				if (autoJump.getValue() && MoveUtil.isMoving())
					mc.thePlayer.motionY = motion.getValue();

				onGroundPackets = 0;
			} else if (!mc.thePlayer.onGround)
				onGroundPackets++;

			if (onGroundPackets < moreOnGroundPackets.getValue()) {
				mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(x, y, z, true));
			}
		}
	};
	
	public void onDisable() {
		mc.timer.timerSpeed = 1;
		mc.thePlayer.speedInAir = 0.02F;
	}
}