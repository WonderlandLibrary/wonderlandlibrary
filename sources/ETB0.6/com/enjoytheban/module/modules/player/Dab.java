package com.enjoytheban.module.modules.player;

import java.awt.Color;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class Dab extends Module {

	public Dab() {
		super("Dab", new String[] { "dab" }, ModuleType.Player);
		setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
		setRemoved(false);
	}
}
