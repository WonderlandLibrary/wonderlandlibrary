package Scov.module.impl.combat;

import Scov.api.annotations.Handler;
import Scov.events.player.EventMotionUpdate;
import Scov.module.Module;
import Scov.value.impl.NumberValue;

public class Hitboxes extends Module {
	
	private NumberValue<Float> size = new NumberValue<>("Hitbox Size", 0.4f, 0.1f, 1.0f);
	
	public Hitboxes() {
		super("Hitboxes", 0, ModuleCategory.COMBAT);
		addValues(size);
	}
	
	@Handler
	public void onMotionUpdate(final EventMotionUpdate event) {
		setSuffix(size.getValueAsString());
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	public float getSize() {
		return size.getValue();
	}
}
