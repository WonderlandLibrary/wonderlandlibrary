package artemis.mods.Other;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class Spammer extends module{

	public Spammer() {
	    super("Spammer", "Spams things in chat", Keyboard.KEY_NONE, 11, Category.OTHER);
	  }
	  
	  public void onDisable() {
	    super.onDisable();
	  }
	  
	  public void onUpdate() {
	    if (isToggled()) {
	    	for (int i = 0; i < 90000; i++) {
	    		  mc.thePlayer.sendChatMessage("oof");
	    		}
	      
	    } 
	    super.onUpdate();
	  }
	}