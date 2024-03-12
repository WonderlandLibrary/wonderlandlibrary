package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to change game speed", name = "Timer", key = Keyboard.KEY_NONE)
public class TimerModule extends Mod {
	
	public BooleanSettings jump = new BooleanSettings("Jump", false);
	public NumberSettings timer = new NumberSettings("Timer", 1, 0.1, 10f, 0.1);
	
	public TimerModule() {
		this.addSettings(jump, timer);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}
	
	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(timer.getValue() + "");
		
		if(MovementUtils.isMoving()) {
			mc.timer.timerSpeed = (float) timer.getValue();
		}else {
			mc.timer.timerSpeed = 1f;
		}
	}
	
	@EventListener
	public void onMoveButton(EventMoveButton e) {
		if(MovementUtils.isMoving() && jump.isEnabled()) {
			e.jump = true;
		}
	}
}
