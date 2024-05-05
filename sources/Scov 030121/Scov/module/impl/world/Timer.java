package Scov.module.impl.world;

import Scov.api.annotations.Handler;
import Scov.events.player.EventMotionUpdate;
import Scov.module.Module;
import Scov.value.impl.NumberValue;

public class Timer extends Module {
	
	private NumberValue<Float> timerValue = new NumberValue<>("Timer Speed", 1.0f, 0.1f, 10.0f);
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1.0f;
	}
	
	public Timer() {
		super("Timer",0, ModuleCategory.WORLD);
		addValues(timerValue);
	}
	
	@Handler
	public void onMotionUpdate(final EventMotionUpdate event) {
		mc.timer.timerSpeed = timerValue.getValue();
	}
}
