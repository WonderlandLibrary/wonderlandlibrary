package artemis.mods.Combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;
/*
 * Credit to GA • Let's codes(GleichAngemeldet) for GommeHD velocity
 * 
 * 
 */
public class Velocity extends module {
	
	public String VelocityMode = Artemis.instance.settingsManager.getSettingByName("Velocity Mode").getValString();;
	
public Velocity() {
		super("Velocity", "Reduces Knockback", Keyboard.KEY_NONE, 4353, Category.COMBAT);
	}


@Override
public void setup() {
    ArrayList<String> options = new ArrayList<String>();
    options.add("Normal");
    options.add("GommeHD");
    Artemis.instance.settingsManager.rSetting(new Setting("Velocity Mode", this, "Normal", options));
}


public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	 this.setName("Velocity §7" + VelocityMode);

     if(VelocityMode.equalsIgnoreCase("Normal")) {
	if(mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime <= 6) {
        mc.thePlayer.motionX *= 0.6;
        mc.thePlayer.motionZ *= 0.6;
          }
    }
     if(VelocityMode.equalsIgnoreCase("GommeHD")) {
    	 if(this.mc.thePlayer.hurtTime > 0) {
 			/* Flag Velocity */
 			this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.26, this.mc.thePlayer.posZ);
 			this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.3, this.mc.thePlayer.posZ);
 		}
     }
    super.onUpdate();
  }
  }
}
