package artemis.mods.Combat;

 
import java.util.Random;

import javax.swing.event.DocumentEvent.EventType;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import artemis.Category;
import artemis.module;
import artemis.util.TimeUtils;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.settings.KeyBinding;
 
public class AutoClicker extends module {
    public AutoClicker() {
        super("AutoClicker", "Automatically clicks when you hold down", Keyboard.KEY_NONE, 3, Category.COMBAT);
    }
    private long lastClick;
	private long hold;
	
	private double speed;
	private double holdLength;
	private double min = 10;
	private double max = 20;
    TimeUtils time = new TimeUtils();
    Random rand = new Random();
 
    public void onDisable() {
        super.onDisable();
      }
    
    
    public void onUpdate() {
        if (isToggled()) {
        	speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
        	holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);
    if (Mouse.isButtonDown(0)) {
		if (System.currentTimeMillis() - lastClick > speed * 1000) {
			lastClick = System.currentTimeMillis();
			if (hold < lastClick) {
				hold = lastClick;
			}
			int key = mc.gameSettings.keyBindAttack.getKeyCode();
			KeyBinding.setKeyBindState(key, true);
			KeyBinding.onTick(key);
			//this.updateVals();
		} else if (System.currentTimeMillis() - hold > holdLength * 1000) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
			//this.updateVals();
		}
	}
    if (min >= max) {
		max = min + 1;
	}
	
	
}
    /*public void onUpdate() {
        if (isToggled()) {
        if(mc.gameSettings.keyBindAttack.pressed) {
            if (time.hasTimerReached(rand.nextInt(125))) {
                time.setLastMS();
                mc.thePlayer.setSprinting(true);
                mc.clickMouse();
            }
        }
    }*/
 }
}

