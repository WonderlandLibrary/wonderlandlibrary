package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.impl.Event.EventScoreboard;
import Velo.impl.Settings.BooleanSetting;

public class Scoreboard extends Module {
	
	public BooleanSetting noScoreboard = new BooleanSetting("NoScoreboard", true);
	
	
	public Scoreboard() {
		super("Scoreboard", "Scoreboard", Keyboard.KEY_NONE, Category.VISUALS);
		this.loadSettings(noScoreboard);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEventScoreboard(EventScoreboard event) {
		if(noScoreboard.isEnabled()) {
			event.setCancelled(true);
		}
	}
} 
