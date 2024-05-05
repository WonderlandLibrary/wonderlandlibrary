package artemis.mods.Movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;

public class HighJump extends module{
	public String JumpMode = Artemis.instance.settingsManager.getSettingByName("Jump Mode").getValString();;
	public HighJump() {
		super("HighJump", "Makes you jump high", Keyboard.KEY_NONE, 67, Category.MOVEMENT);
	}

	  @Override
	  public void setup() {
	      ArrayList<String> options = new ArrayList<String>();
	      options.add("Vanilla");
	      options.add("Cubecraft");
	      Artemis.instance.settingsManager.rSetting(new Setting("Jump Mode", this, "Cubecraft", options));
	  }
	public void onDisable() {
		 mc.timer.timerSpeed = 1.0F;
	    super.onDisable();
	  }
	  
	  public void onUpdate() {
	    if (isToggled()) {
	    	 this.setName("HighJump §7" + JumpMode);

	         if(JumpMode.equalsIgnoreCase("Vanilla")) {
	    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && mc.thePlayer.onGround) {
	    		mc.thePlayer.motionY = 4;
	    	}
	         } if(JumpMode.equalsIgnoreCase("Cubecraft")) {
	        	 if (mc.thePlayer != null && mc.theWorld != null) {
	                 if(mc.thePlayer.hurtTime > 0) {
	                     mc.timer.timerSpeed = 0.3F;
	                     mc.thePlayer.motionY = 3.8F;
	                 } else {
	                     mc.timer.timerSpeed = 1.0F;
	                 }
	             }
	         }
	      
	    }
	    super.onUpdate();
	  }

}
