package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class LongJumpMineLandImpl implements ModeImpl<LongJump> {

	@Override
	public LongJump getParent() {
		return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
	}

	@Override
	public String getName() {
		return "Mine Land";
	}

	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
	}

	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
		mc.timer.timerSpeed = 1.0F;
		ticks = 0;
	}

	@EventHandler
    private final Listener<Event> eventListener = this::onEvent;
	private int ticks;
	private double speed;


	public void onEvent(Event e) {
		if (e instanceof EventReceivedPacket && ((EventReceivedPacket) e).getPacket() instanceof S08PacketPlayerPosLook)
			speed = 0;
		if (e instanceof EventUpdate) {
			switch (ticks) {
				case 0:
					mc.thePlayer.jump();
					speed = MovementUtil.getBaseSpeed() + 0.7 - 0.2873;
					ticks++;
					break;
				case 1:
					if (mc.thePlayer.isCollidedHorizontally) speed = 0;
					speed = Math.max(0.255, speed * (mc.thePlayer.moveStrafing != 0 ? 0.98 : 0.99));
					if (mc.thePlayer.isMoving()) {
						mc.thePlayer.motionX = (-(Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * speed));
						mc.thePlayer.motionZ = (Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * speed);
						if (mc.thePlayer.fallDistance < 1.3 && mc.thePlayer.motionY < 0) mc.thePlayer.motionY += mc.thePlayer.fallDistance < 0.5 ? 0.04 : 0.035;
					} else mc.thePlayer.setSpeed(speed = 0);
					break;
			}
		}
		if (e instanceof EventMotion && ((EventMotion) e).isPost()) {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
		}
		if (e instanceof EventMove) {
			MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()), (EventMove) e);
			if (!mc.thePlayer.isMoving()) MovementUtil.setSpeed(0, (EventMove) e);
			if (mc.thePlayer.onGround && mc.thePlayer.motionY < 0) {
				mc.thePlayer.setSpeed(0);
				MovementUtil.setSpeed(0, (EventMove) e);
				getParent().setEnabled(false);
			}
		}
	}


}


