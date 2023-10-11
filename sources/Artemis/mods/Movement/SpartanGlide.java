package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class SpartanGlide extends module{
	
	int tick = 0;
	
	  public SpartanGlide() {
		    super("SpartanGlide", "Allows you to glide with Spartan anticheat", Keyboard.KEY_NONE, 14, Category.MOVEMENT);
		  }
		  
		  public void onDisable() {
		    super.onDisable();
		  }
		  
		  public void onUpdate() {
		    if (isToggled()) { 
		    	 if (!mc.thePlayer.onGround) {
		             if (tick == 3 || tick == 5 || tick == 7 || tick == 9 || tick == 11 || tick == 15 || tick == 17 || tick == 19) {
		                 mc.thePlayer.motionY = 0.07;

		             } if (tick == 20) {
		                 tick = 0;
		             } else {
		                 tick++;
		                 mc.thePlayer.onGround = false;
		             }
		         }
		    	
		    	
		      super.onUpdate();
		    } 
		  }
		}


