package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;

public class Fullbright extends Module{
	
	public Fullbright(){
		super("Fullbright", Keyboard.KEY_O, Category.RENDER, "Makes the world brighter, and whiter!");
	}
	
	public void onEnable(){
		mc.gameSettings.gammaSetting = 100;
	}
	
	public void onDisable(){
		mc.gameSettings.gammaSetting = 1;
	}

}