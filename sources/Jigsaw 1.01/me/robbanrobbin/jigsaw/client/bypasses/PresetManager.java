package me.robbanrobbin.jigsaw.client.bypasses;

import java.util.ArrayList;

public class PresetManager {

	private ArrayList<ModPreset> bypasses = new ArrayList<ModPreset>();

	public PresetManager() {
		bypasses.add(new AntiGCheat());
		bypasses.add(new AntiCubecraft());
		bypasses.add(new AntiWatchdog());
		bypasses.add(new AntiGwen());
		bypasses.add(new NCP());
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
