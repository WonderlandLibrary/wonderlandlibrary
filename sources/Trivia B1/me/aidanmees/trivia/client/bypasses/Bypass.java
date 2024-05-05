package me.aidanmees.trivia.client.bypasses;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMultiset;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.module.Module;

public abstract class Bypass {
	
	protected boolean enabled = false;
	protected ArrayList<Module> allowedMods = new ArrayList<Module>();
	public Bypass() {
		setBypassableMods();
	}
	public void setBypassableMods() {
		//this.allowedMods.add(trivia.getModuleByName("Open Bypasses List"));
	}
	
	public ArrayList<Module> getAllowedModules() {
		return allowedMods;
	}
	
	public void onEnable() {
		//trivia.getGUIMananger().reloadSettingScreens();
		for(Module mod : trivia.getToggledModules()) {
			if(!allowedMods.contains(mod)) {
				mod.setToggled(false, true);
			}
		}
	}
	
	public void onDisable() {
		
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled) {
			onEnable();
		}
		else {
			onDisable();
		}
	}
	
	public String getName() {
		return null;
	}
	
}
