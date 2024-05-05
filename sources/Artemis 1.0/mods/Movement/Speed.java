package artemis.mods.Movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;
import artemis.util.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
/*
 * Credit to GA • Let's codes(GleichAngemeldet) for McGamester Speed
 * 
 * 
 */
public class Speed extends module {
	TimeUtils time;
	public String SpeedMode = Artemis.instance.settingsManager.getSettingByName("Speed Mode").getValString();;
    public Speed() {
        super("Speed", "Gives you Speed", Keyboard.KEY_U, 9999, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Normal");
        options.add("McGamester");
        options.add("Cubecraft");
        options.add("Y-Port");
        Artemis.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Normal", options));
    }

    @Override
  	public void onUpdate() {
  		if(this.isToggled()){
  		  this.setName("Speed §7" + SpeedMode);

          if(SpeedMode.equalsIgnoreCase("Normal")) {
        if(isOnLiquid())
            return;
        if(mc.thePlayer.onGround && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
            if(mc.thePlayer.ticksExisted % 2 != 0)
                //event.y += .4;
            mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? .45F : .2F);
            mc.timer.timerSpeed = 1.095F;
          }
  		}
          if(SpeedMode.equalsIgnoreCase("McGamester")) {
        	   if(mc.thePlayer.onGround && mc.thePlayer.moveForward > 0) {
                   
                   double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                   double motionX = -Math.sin(yaw);
                   double motionZ = Math.cos(yaw);
                   mc.thePlayer.motionX = motionX * 5;
                   mc.thePlayer.motionZ = motionZ * 5;
                   mc.thePlayer.cameraYaw = 0.15F;
                   mc.thePlayer.setSprinting(true);
                  
               }
        	  
        	  
          }
          if(SpeedMode.equalsIgnoreCase("Y-Port")) {
          if (this.mc.gameSettings.keyBindForward.pressed) {
  			if (this.mc.thePlayer.onGround) {

  				double speedValue = 0.27;

  				double yaw = Math.toRadians(this.mc.thePlayer.rotationYaw);
  				double x = -Math.sin(yaw) * speedValue;
  				double z = Math.cos(yaw) * speedValue;

  				this.mc.thePlayer.motionX = x;
  				this.mc.thePlayer.motionZ = z;
  				this.mc.thePlayer.jump();

  			} else {
  				this.mc.thePlayer.motionY = -0.5;
  			}

  		}
          }
          if(SpeedMode.equalsIgnoreCase("Cubecraft")) {
        	     if(mc.thePlayer != null && mc.theWorld != null) {
        	            if(mc.thePlayer.onGround) {
        	                if(time.hasTimerReached(100L)) {
        	                    double rotationYaw = mc.thePlayer.rotationYaw;
        	                   
        	                    double yaw = Math.toRadians(rotationYaw);
        	                    /*The 1.5 is the teleport length*/
        	                    double x = -Math.sin(yaw) * 1.5;
        	                    double z = Math.cos(yaw) * 1.5;
        	                    /*Thats the teleport*/
        	                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        	               
        	                    mc.timer.timerSpeed = 1.0F;
        	                    time.reset();
        	                }
        	            } else {
        	                mc.timer.timerSpeed = 1.0F;
        	                toggle();
        	            }
        	        }
          }

        	  
    }
    }
    private boolean isOnLiquid() {
        boolean onLiquid = false;
        final int y = (int)(mc.thePlayer.boundingBox.minY - .01);
        for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if(block != null && !(block instanceof BlockAir)) {
                    if(!(block instanceof BlockLiquid))
                        return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
}
