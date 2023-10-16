package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

/*
 * Created by Jutting on Oct 11, 2018
 */

public class Boost extends Module {
	
	private TimerUtil timer = new TimerUtil();
	
	public Boost() {
		super("Boost", new String[] { "boost" }, ModuleType.Movement);
		setColor(new Color(216, 253, 100).getRGB());
	}
	
	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		mc.timer.timerSpeed = 3.0f;
		if (mc.thePlayer.ticksExisted % 15 == 0) {
			this.setEnabled(false);
		}
	}
	
	@Override
	public void onDisable() {
		this.timer.reset();
		mc.timer.timerSpeed = 1.0f;
	}
	
}

