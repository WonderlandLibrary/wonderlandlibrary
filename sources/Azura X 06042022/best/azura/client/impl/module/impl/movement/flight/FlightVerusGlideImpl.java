package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightVerusGlideImpl implements ModeImpl<Flight> {


	@Override
	public Flight getParent() {
		return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
	}

	@Override
	public String getName() {
		return "Verus Glide";
	}

	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
		MovementUtil.spoof(0, true);
		if (mc.thePlayer.onGround) mc.thePlayer.motionY = 0.5F;
	}

	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
		mc.timer.timerSpeed = 1F;
		mc.thePlayer.setSpeed(0);
	}

	@EventHandler
	public final Listener<EventMotion> eventMotionListener = e -> {
		if (mc.thePlayer.isSneaking()) return;
		if (mc.thePlayer.fallDistance > 0.02) {
			e.onGround = true;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.fallDistance = 0;
		}
		if (mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 10 == 0) getParent().setEnabled(false);
	};

	@EventHandler
	public final Listener<EventMove> moveListener = e -> {
		if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0.33);
	};
}