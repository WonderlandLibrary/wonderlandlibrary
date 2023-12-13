package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class Sprint extends Module {

	/**
	 * @author Purity
	 * Basic Sprint with a hunger check
	 */

	private Option<Boolean> omni = new Option("Omni-Directional", "omni", true);

	public Sprint() {
		super("Sprint", new String[] {"run"}, ModuleType.Movement);
		//gen a color
		setColor(new Color(158,205,125).getRGB());
		addValues(omni);
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if(mc.thePlayer.getFoodStats().getFoodLevel() > 6 && omni.getValue() ? mc.thePlayer.moving() : mc.thePlayer.moveForward > 0) {
			mc.thePlayer.setSprinting(true);
		}
	}	
}