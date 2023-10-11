package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class FastLadder extends module{

	public FastLadder() {
		super("FastLadder", "Allows you to climb up ladders faster", Keyboard.KEY_U, 99, Category.MOVEMENT);
	}

	public void onDisable() {
	 
	    super.onDisable();
	  }
	  
	  public void onUpdate() {
	    if (isToggled()) {
	    	if(mc.thePlayer.isOnLadder()) {
	    		mc.thePlayer.motionY = 0.169;
	    	}
	     
	      super.onUpdate();
	    } 
	  }
	
}
