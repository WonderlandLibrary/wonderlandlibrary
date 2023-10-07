package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

import java.util.Collections;
import java.util.List;

public class LongJumpSpartanB456Impl implements ModeImpl<LongJump> {

	private final BooleanValue infinite = new BooleanValue("Infinite", "High jump infinitely", false);

	@Override
	public List<Value<?>> getValues() {
		return Collections.singletonList(infinite);
	}

	@Override
	public LongJump getParent() {
		return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
	}

	@Override
	public String getName() {
		return "Spartan B456";
	}

	@EventHandler
	public final Listener<EventUpdate> eventUpdateListener = e -> {
		if (infinite.getObject()) {
			for (int i = 1; i <= 5; i++)
				MovementUtil.spoof(0.01 * i, true);
			MovementUtil.spoof(500, true);
			MovementUtil.spoof(0, true);
		} else {
			if (mc.thePlayer.fallDistance > 0) {
				MovementUtil.vClip(Math.min(0.8, mc.thePlayer.fallDistance / 5.0 * 0.95));
			}
		}
	};

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}


}


