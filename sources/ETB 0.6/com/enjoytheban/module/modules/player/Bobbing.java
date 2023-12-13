package com.enjoytheban.module.modules.player;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

/*
 * Created by Jutting on Oct 12, 2018
 */

public class Bobbing extends Module {

	private Numbers<Double> boob = new Numbers("Amount", "Amount", 1.0, 0.1, 5.0, 0.5);

	public Bobbing() {
		super("Bobbing+", new String[] { "bobbing+" }, ModuleType.Player);
		addValues(boob);
	}

	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		this.setColor(new Color(20,200,100).getRGB());
		if (mc.thePlayer.onGround) {
			mc.thePlayer.cameraYaw = (float) (0.099999994f / 1.1f * boob.getValue());
		}
	}

}
