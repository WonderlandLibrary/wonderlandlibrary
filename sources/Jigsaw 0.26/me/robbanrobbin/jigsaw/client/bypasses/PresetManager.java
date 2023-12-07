package me.robbanrobbin.jigsaw.client.bypasses;

import java.util.ArrayList;

import org.newdawn.slick.state.transition.SelectTransition;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;

public class PresetManager {

	private ArrayList<ModPreset> bypasses = new ArrayList<ModPreset>();

	public PresetManager() {
		bypasses.add(new AntiGCheat());
		bypasses.add(new AntiCubecraft());
		bypasses.add(new AntiWatchdog());
		bypasses.add(new AntiGwen());
	}

	public ArrayList<ModPreset> getBypasses() {
		return bypasses;
	}
	
	public ModPreset getByName(String name) {
		for (ModPreset bypass : bypasses) {
			if (bypass.getName().equals(name)) {
				return bypass;
			}
		}
		return null;
	}

}
