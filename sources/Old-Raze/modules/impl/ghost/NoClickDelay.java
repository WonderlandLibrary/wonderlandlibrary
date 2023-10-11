package markgg.modules.impl.ghost;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;

@ModuleInfo(name = "NoClickDelay", category = Module.Category.GHOST)
public class NoClickDelay extends Module {

	@EventHandler
	private final Listener<MotionEvent> motionEventListener = e -> {
		mc.leftClickCounter = 0;
	};

}
