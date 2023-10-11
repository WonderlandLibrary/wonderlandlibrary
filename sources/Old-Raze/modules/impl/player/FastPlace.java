package markgg.modules.impl.player;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;

@ModuleInfo(name = "FastPlace", category = Module.Category.PLAYER)
public class FastPlace extends Module{

	public NumberSetting speed = new NumberSetting("Speed", this, 0,0,10,1);
	public ModeSetting placeMode = new ModeSetting("Mode",  this, "Hold", "Hold", "Auto");

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			switch(placeMode.getMode()) {
				case "Hold":
					int mappedSpeed = (int) Math.round(speed.getValue());
					mc.rightClickDelayTimer = (mappedSpeed == 0) ? 10 : 10 / mappedSpeed;
					break;
				case "Auto":
					if(mc.currentScreen == null){
						try {
							click();
						} catch (AWTException e1) {
							e1.printStackTrace();
						}
					}
					break;
			}
		}
	};


	private void click() throws AWTException {
		Robot bot = new Robot();
		bot.mousePress(InputEvent.BUTTON3_MASK);
		bot.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
	public void onDisable() {
		mc.rightClickDelayTimer = 6;
	}
}
