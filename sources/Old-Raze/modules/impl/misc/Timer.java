package markgg.modules.impl.misc;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;

@ModuleInfo(name = "Timer", category = Module.Category.MISC)
public class Timer extends Module {

	public NumberSetting speed = new NumberSetting("Speed",this, 1, 0.05, 4.0, 0.05);

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			mc.timer.timerSpeed = (float) speed.getValue();
		}
	};

	public void onDisable() {
		mc.timer.timerSpeed = 1;
	}
	
}
