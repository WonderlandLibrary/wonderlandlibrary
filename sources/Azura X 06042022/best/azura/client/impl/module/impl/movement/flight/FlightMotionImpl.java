package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

import java.util.Collections;
import java.util.List;

public class FlightMotionImpl implements ModeImpl<Flight> {

	private final BooleanValue infinite = new BooleanValue("Infinite", "Verus infinite bypass", false);

	@Override
	public List<Value<?>> getValues() {
		return Collections.singletonList(infinite);
	}

	@Override
	public Flight getParent() {
		return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
	}

	@Override
	public String getName() {
		return "Motion";
	}

	@Override
	public void onEnable() {
		Client.INSTANCE.getEventBus().register(this);
	}

	@Override
	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(this);
	}


	@EventHandler
	public final Listener<EventMove> moveListener = e -> {
		e.setY(mc.thePlayer.motionY = (infinite.getObject() ? (mc.thePlayer.ticksExisted % 4 == 0 ? 0.075 * 3 : -0.075) : 0));
		if (mc.gameSettings.keyBindSneak.pressed)
			e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
		if (mc.gameSettings.keyBindJump.pressed)
			e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
		if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Flight.speedValue.getObject(), e);
		else MovementUtil.setSpeed(0, e);
	};
}