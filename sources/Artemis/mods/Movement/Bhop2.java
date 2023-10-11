package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class Bhop2 extends module {
  
  public Bhop2() {
    super("Bhop", "Makes you jump like a bunny", Keyboard.KEY_J, 4, Category.MOVEMENT);
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	 if(mc.thePlayer.isInWater()) {
             return;
    	 }
         if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
             if(mc.thePlayer.onGround) {
                 mc.thePlayer.jump();
                 mc.thePlayer.motionX *= 1.02D;
                 mc.thePlayer.motionZ *= 1.02D;
             }else if(mc.thePlayer.motionY > -0.2D) {
                 mc.thePlayer.jumpMovementFactor = 0.08F;
                 mc.thePlayer.motionY += 0.0143099999999999999999999999999D;
                 mc.thePlayer.jumpMovementFactor = 0.07F;
             }
         }else{
             mc.thePlayer.motionX = 0D;
             mc.thePlayer.motionZ = 0D;
         }
     }
    		  super.onUpdate();
    		}
         }
     
  
