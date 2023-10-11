package markgg.modules.impl.movement;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.util.timer.Timer;

@ModuleInfo(name = "Glide", category = Module.Category.MOVEMENT)
public class Glide extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Vulcan", "SkyCave");
	public Timer timer = new Timer();

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			switch(mode.getMode()) {
				case "Vanilla":
					if(mc.thePlayer.fallDistance > 1.1)
						mc.thePlayer.motionY = -0.07F;
					break;
				case "Vulcan":
					if(timer.hasTimeElapsed(100, true)) 
						mc.thePlayer.motionY = -0.155F;
					else 
						mc.thePlayer.motionY = -0.1F;
					break;
				case "SkyCave":
					if(timer.hasTimeElapsed(100, true)) 
						mc.thePlayer.motionY = -0.145F;
					else 
						mc.thePlayer.motionY = -0.1F;
					break;
			}
		}
	};

}
