package com.enjoytheban.module.modules.render;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class FullBright extends Module {

	public FullBright() {
		super("FullBright", new String[] {"fbright", "brightness", "bright"}, ModuleType.Render);
        setColor(new Color(244,255,149).getRGB());
	}

	private float old;

	@Override
	public void onEnable() {
		this.old = mc.gameSettings.gammaSetting;
	}

	@EventHandler
	private void onTick(EventTick e) {
		mc.gameSettings.gammaSetting = 15999999;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = this.old;
	}
}