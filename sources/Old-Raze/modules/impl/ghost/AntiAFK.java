package markgg.modules.impl.ghost;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.util.timer.Timer;

@ModuleInfo(name = "AntiAFK", category = Module.Category.GHOST)
public class AntiAFK extends Module {

	public BooleanSetting hit = new BooleanSetting("Punch",this, false);
	public NumberSetting delay = new NumberSetting("Delay",this, 5.0, 2.5, 10.0, 0.5);
	public Timer timer = new Timer();


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			mc.gameSettings.keyBindForward.pressed = true;
			if (timer.hasTimeElapsed((long)delay.getValue() * 100L, true)) {
				if (hit.getValue()) mc.clickMouse();
				mc.thePlayer.rotationYaw = mc.thePlayer.prevRotationYaw -= 90.0f;
				mc.gameSettings.keyBindForward.pressed = false;
			}
		}
	};

}
