package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;

public class Rider extends Module{
	
	public ModeSetting mode = new ModeSetting("Entity", "Arrow", "Arrow", "SnowBall", "Egg","FishingRod", "SplashPotion", "MatrixArrow", "VulcanArrow", "SpartanArrow");
	public BooleanSetting doI = new BooleanSetting("Auto", false);
	
	public Rider(){
		super("Rider", Keyboard.KEY_NONE, Category.MOVEMENT, "Launches a projectile and copies its momentum whilst collided.");
		this.addSettings(mode);
	}	
	
	public void onEnable() {
		Epsilon.addChatMessage("This is a WIP Module! It does nothing currently");
	}
	public void onDisable() {
		
	}
	

	public void onEvent(Event e){
		if(e instanceof EventUpdate){
			if(e.isPre()){
				/*
				 *Description
				 *Throws a projectile/shoots an arrow and then copies its momentum on launch whilst the player is collided 
				 * Should bypass some anticheats that exempt for entity collision
				 * Theory that itll work on matrix (as I saw in a video :trol:)
				 * 
				 *
				 */
			}
		}
	}	
	

}
