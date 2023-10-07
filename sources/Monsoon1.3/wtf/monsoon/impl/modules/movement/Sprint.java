package wtf.monsoon.impl.modules.movement;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.util.entity.MovementUtil;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Automatically sprints for you.", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (!mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && MovementUtil.isMoving()){
			mc.thePlayer.setSprinting(true);
		}
	}

}
