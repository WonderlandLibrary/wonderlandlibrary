package xyz.cucumber.base.module.feat.combat;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;

@ModuleInfo(category = Category.COMBAT, description = "Automatically click for you", name = "Auto Clicker", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class AutoClickerModule extends Mod {
	
	public double leftDelay, rightDelay;
	public boolean shouldLeftClick, shouldRightClick;
	
	public BooleanSettings left = new BooleanSettings("Left", true);
	public BooleanSettings right = new BooleanSettings("Right", true);
	
	public NumberSettings minLeftCps = new NumberSettings("Min Left CPS", 10, 1, 20, 1);
	public NumberSettings maxLeftCps = new NumberSettings("Max Left CPS", 10, 1, 20, 1);
	public NumberSettings minRightCps = new NumberSettings("Min Right CPS", 10, 1, 20, 1);
	public NumberSettings maxRightCps = new NumberSettings("Max Right CPS", 10, 1, 20, 1);
	
	public BooleanSettings onlyBlock = new BooleanSettings("Only Blocks", true);
	
	public Timer leftTimer = new Timer();
	public Timer rightTimer = new Timer();
	public Timer timer = new Timer();
	
	public AutoClickerModule() {
		this.addSettings(left, right, minLeftCps, maxLeftCps, minRightCps, maxRightCps, onlyBlock);
	}
	
	@EventListener
	public void onTick(EventTick e) {
		if(shouldLeftClick && left.isEnabled() && mc.objectMouseOver.typeOfHit != MovingObjectType.BLOCK) {
			shouldLeftClick = false;
			mc.clickMouseEvent();
		}
		if(shouldRightClick && right.isEnabled()) {
			shouldRightClick = false;
			if(onlyBlock.isEnabled()) {
				if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
					mc.rightClickMouse();
				}
			}else {
				mc.rightClickMouse();
			}
		}
	}
	
	@EventListener
	public void onGameLoop(EventGameLoop e) {
		double leftMinValue = minLeftCps.getValue();
		double leftMaxValue = maxLeftCps.getValue();
	    
		if(timer.hasTimeElapsed(100, false)) {
			leftDelay = leftMinValue + (new Random().nextDouble() * (leftMaxValue-leftMinValue));
		}
		
		if(leftDelay < leftMinValue) {
			leftDelay = leftMinValue;
        }
        if(leftDelay > leftMaxValue) {
        	leftDelay = leftMaxValue;
        }
		
		double finalLeftDelay = (1000/leftDelay);
		finalLeftDelay -= 5;
		
		if(mc.gameSettings.keyBindAttack.pressed && leftTimer.hasTimeElapsed(finalLeftDelay, true)) {
        	shouldLeftClick = true;
        }
		
		double rightMinValue = minRightCps.getValue();
		double rightMaxValue = maxRightCps.getValue();
	    
		if(timer.hasTimeElapsed(100, true)) {
			rightDelay = rightMinValue + (new Random().nextDouble() * (rightMaxValue-rightMinValue));
		}
		
		if(rightDelay < rightMinValue) {
			rightDelay = rightMinValue;
        }
        if(rightDelay > rightMaxValue) {
        	rightDelay = rightMaxValue;
        }
		
		double finalRightDelay = (1000/rightDelay);
		finalRightDelay -= 5;
		
		if(mc.gameSettings.keyBindUseItem.pressed && rightTimer.hasTimeElapsed(finalRightDelay, true)) {
        	shouldRightClick = true;
        }
	}
}
