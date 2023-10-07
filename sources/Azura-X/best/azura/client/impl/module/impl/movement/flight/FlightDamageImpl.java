package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightDamageImpl implements ModeImpl<Flight> {

	@Override
	public Flight getParent() {
		return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
	}

	@Override
	public String getName() {
		return "Damage";
	}

	@Override
	public void onEnable() {
		MovementUtil.spoof(4, false);
		MovementUtil.spoof(0.1, false);
		MovementUtil.spoof(0, true);
		MovementUtil.vClip(0.5);
		Client.INSTANCE.getEventBus().register(this);
	}

	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
	}


	@EventHandler
	public final Listener<EventMove> moveListener = e -> {
		e.setY(mc.thePlayer.motionY = 0);
		if (mc.gameSettings.keyBindSneak.pressed)
			e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() / 2 * 0.75);
		if (mc.gameSettings.keyBindJump.pressed)
			e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() / 2 * 0.75);
		if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Flight.speedValue.getObject(), e);
		else MovementUtil.setSpeed(0, e);
	};

	@EventHandler
	public final Listener<EventMotion> motionListener = e -> {
		e.onGround = true;
	};
}