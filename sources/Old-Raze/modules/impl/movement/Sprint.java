package markgg.modules.impl.movement;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;

import markgg.event.Event;
import markgg.modules.Module;

@ModuleInfo(name = "Sprint", category = Module.Category.MOVEMENT)
public class Sprint extends Module{

	public ModeSetting sprintMode = new ModeSetting("Mode", this, "Legit", "Legit", "Omni");


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			switch (sprintMode.getMode()) {
				case "Legit":
					if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally)
						mc.thePlayer.setSprinting(true);
					break;
				case "Omni":
					mc.thePlayer.setSprinting(true);
					break;
			}
		}
	};

	public void onDisable() {
		mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.getIsKeyPressed());
	}
}
