package markgg.modules.impl.render;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;

@ModuleInfo(name = "Rotations", category = Module.Category.RENDER)
public class Rotations extends Module {

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		mc.thePlayer.rotationYawHead = e.getYaw();
		mc.thePlayer.renderYawOffset = e.getYaw();
	};
	
}
