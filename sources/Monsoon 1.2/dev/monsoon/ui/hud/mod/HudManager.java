package dev.monsoon.ui.hud.mod;

import java.util.ArrayList;

import dev.monsoon.ui.hud.mod.impl.TargetHUD;

public class HudManager {
	
	public ArrayList<HudMod> hudMods = new ArrayList<>();

	public TargetHUD targetHud;
	
	public HudManager() {
		hudMods.add(targetHud = new TargetHUD());
	}
	
	public void renderMods() {
		for(HudMod m : hudMods) {
			if(m.isEnabled()) {
				m.draw();
			}
		}
	}

}
