package Scov.module.impl.player;

import Scov.api.annotations.Handler;
import Scov.events.player.EventMotionUpdate;
import Scov.module.Module;

public class FastPlace extends Module {
	
	public FastPlace() {
		super("FastPlace", 0, ModuleCategory.PLAYER);
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
		mc.rightClickDelayTimer = 4;
	}
	
	@Handler
	public void onMotionUpdate(final EventMotionUpdate event) {
		mc.rightClickDelayTimer = 0;
	}
}
