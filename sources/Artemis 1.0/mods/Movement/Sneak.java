package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class Sneak extends module {
 
  
  public Sneak() {
    super("Sneak", "Automatically sneaks", Keyboard.KEY_H, 13, Category.MOVEMENT);
  }
  
  public void onDisable() {
	  mc.gameSettings.keyBindSneak.pressed = false;
    super.onDisable();
  }
  @Override
	public void onUpdate() {
		if(this.isToggled()){
			mc.gameSettings.keyBindSneak.pressed = true;
  }
 }
}
