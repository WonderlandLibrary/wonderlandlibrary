package markgg.modules.impl.ghost;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.ModuleInfo;
import org.lwjgl.input.Mouse;

import markgg.event.Event;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.util.timer.Timer;

@ModuleInfo(name = "AutoClicker", category = Module.Category.GHOST)
public class AutoClicker extends Module {

	public NumberSetting speed = new NumberSetting("CPS", this, 12, 1, 24, 1);
	public NumberSetting randAmount = new NumberSetting("Randomness", this, 3, 1, 7, 1);
	public ModeSetting clickingMode = new ModeSetting("Mode", this, "Left", "Left", "Right", "Both");
	public BooleanSetting randomCPS = new BooleanSetting("Randomize", this, true);
	public BooleanSetting holdClick = new BooleanSetting("Hold Click", this, true);

	public Timer timer = new Timer();

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		Random rand = new Random();
		int currentSpeed = (int) speed.getValue(), newSpeed;

		if (randomCPS.getValue()) {
			int randomNum = rand.nextInt(5);
			newSpeed = (int) (currentSpeed + randomNum - randAmount.getValue());
		} else
			newSpeed = currentSpeed;

		int maxSpeed = (int) speed.getMaximum();
		if (newSpeed > maxSpeed)
			newSpeed = maxSpeed;
		else if (newSpeed < 0)
			newSpeed = 0;

		newSpeed = newSpeed;

		if(timer.hasTimeElapsed((long) (1000.0D / newSpeed), true) && mc.currentScreen == null && !holdClick.getValue()){
			try {
				click();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			if(timer.hasTimeElapsed((long) (1000.0D / newSpeed), true) && mc.currentScreen == null && holdClick.getValue()) {
				if(Mouse.isButtonDown(0)) {
					try {
						click();
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	};

	private void click() throws AWTException {
		Robot bot = new Robot();
		switch (clickingMode.getMode()) {
		case "Right":
			mc.rightClickMouse();
			break;
		case "Left":
			mc.clickMouse();
			break;
		case "Both":
			mc.clickMouse();
			mc.rightClickMouse();
			break;
		}
	}

}
