package artemis.mods.Movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;
import artemis.util.MoveUtil;
import artemis.util.MovementUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/*
 * Credit to GA • Let's codes(GleichAngemeldet) for NCP Bhop
 * 
 * 
 */

public class Bhop extends module {
	MoveUtil move;
	private int airMoves;
	public String BhopMode = Artemis.instance.settingsManager.getSettingByName("Bhop Mode").getValString();
	private boolean boosted;;
  public Bhop() {
    super("Bhop", "Makes you jump like a bunny", Keyboard.KEY_J, 4, Category.MOVEMENT);
  }
  private boolean spoofSlot;
  private float speed = 0F;
  @Override
  public void setup() {
      ArrayList<String> options = new ArrayList<String>();
      options.add("AAC");
      options.add("AAC2");
      options.add("NCP");
      options.add("Hypixel");
      options.add("Hypixel2");
      options.add("Timer");
      options.add("Spartan");
      options.add("Spectre");
      options.add("Hive");
      options.add("SlowHop");
      options.add("Mineplex");
      Artemis.instance.settingsManager.rSetting(new Setting("Bhop Mode", this, "AAC", options));
  }

  
  public void onUpdate() {
    if (isToggled()) {
    	 this.setName("Bhop §7" + BhopMode);
    	 if(BhopMode.equalsIgnoreCase("Spectre")) {
    		  if(!(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0 || mc.thePlayer.movementInput.jump))
    	            return;

    	        if(mc.thePlayer.onGround) {
    	            MovementUtils.strafe(1.1F);
    	            mc.thePlayer.motionY = 0.44D;
    	            return;
    	        }

    	        MovementUtils.strafe();
    	    }
    	 if(BhopMode.equalsIgnoreCase("SlowHop")) {
    		 if(mc.thePlayer.isInWater())
    	            return;

    	        if(MovementUtils.isMoving()) {
    	            if(mc.thePlayer.onGround)
    	                mc.thePlayer.jump();
    	            else
    	                MovementUtils.strafe(MovementUtils.getSpeed() * 1.011F);
    	        }else{
    	            mc.thePlayer.motionX = 0D;
    	            mc.thePlayer.motionZ = 0D;
    	        }
    	 }
    	 if(BhopMode.equalsIgnoreCase("Mineplex")) {
    		 if(!MovementUtils.isMoving() || !mc.thePlayer.onGround || mc.thePlayer.inventory.getCurrentItem() == null || mc.thePlayer.isUsingItem())
    	            return;

    	        spoofSlot = false;

    	        for(int i = 36; i < 45; i++) {
    	            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

    	            if(itemStack != null) continue;

    	            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i - 36));
    	            spoofSlot = true;
    	            break;
    	        }
    	        
    	        
    	        if(!MovementUtils.isMoving() || !mc.thePlayer.onGround || mc.thePlayer.isUsingItem()) {
    	            speed = 0F;
    	            return;
    	        }

    	        if(!spoofSlot && mc.thePlayer.inventory.getCurrentItem() != null) {
    	            mc.thePlayer.addChatMessage(new ChatComponentText("§8[§c§lMineplex§aSpeed§8] §cYou need one empty slot."));
    	            return;
    	        }

    	        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1, mc.thePlayer.posZ);
    	        //final Vec3 vec = new Vec3(blockPos).addVector(0.4F, 0.4F, 0.4F).add(new Vec3(EnumFacing.UP.getDirectionVec()));
    	        //mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4F, vec.yCoord * 0.4F, vec.zCoord * 0.4F));

    	        //final float targetSpeed = ((Speed) Artemis.getModules(Speed.class)).mineplexGroundSpeedValue.get();
    	        final float targetSpeed = 3.0F;
    	        if(targetSpeed > speed) speed += targetSpeed / 8;
    	        if(speed >= targetSpeed) speed = targetSpeed;

    	        MovementUtils.strafe(speed);

    	        if(!spoofSlot)
    	            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    	 }
    	 if(BhopMode.equalsIgnoreCase("Hive")) {
    		  if(MovementUtils.isMoving()) {
    	            if(mc.thePlayer.onGround)
    	                mc.thePlayer.motionY = 0.3;

    	            mc.thePlayer.speedInAir = 0.0425F;
    	            mc.timer.timerSpeed = 1.04F;
    	            MovementUtils.strafe();
    	        }else{
    	            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
    	            mc.thePlayer.speedInAir = 0.02F;
    	            mc.timer.timerSpeed = 1F;
    	        }
    	 }
    	 
    	 
    	 
         if(BhopMode.equalsIgnoreCase("Timer")) {
    	if(mc.thePlayer.onGround) {
    		if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
        		mc.thePlayer.jump();
        	}
    		mc.timer.timerSpeed = 1.1f;
    		mc.thePlayer.motionZ /= 2f;
    		mc.thePlayer.motionX /= 2f;
    		mc.thePlayer.motionY += 0.05f;
    		}else {
    		  mc.thePlayer.motionY -= 0.03f;
    		  
    		  mc.timer.timerSpeed = 1.11f;
    		  mc.thePlayer.moveStrafing *= 0.1f;
    		  mc.thePlayer.jumpMovementFactor = 0.05f;
    		  super.onUpdate();
    		}
         }
         if(BhopMode.equalsIgnoreCase("Spartan")) {
        	  if(mc.gameSettings.keyBindForward.isPressed() && !mc.gameSettings.keyBindJump.isPressed()) {
                  if(mc.thePlayer.onGround) {
                      mc.thePlayer.jump();
                      airMoves = 0;
                  }else{
                      mc.timer.timerSpeed = 1.08F;

                      if(airMoves >= 3)
                          mc.thePlayer.jumpMovementFactor = 0.0275F;

                      if(airMoves >= 4 && airMoves % 2 == 0.0) {
                          mc.thePlayer.motionY = -0.32F - 0.009 * Math.random();
                          mc.thePlayer.jumpMovementFactor = 0.0238F;
                      }

                      airMoves++;
                  }
              }
         }
         if(BhopMode.equalsIgnoreCase("AAC")) {
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
         if(BhopMode.equalsIgnoreCase("AAC2")) {
         if(mc.thePlayer.isInWater()) {
             return;
    	 }
         if(mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
         mc.timer.timerSpeed = 1.08F;

         if(mc.thePlayer.onGround) {
             mc.thePlayer.motionY = 0.399D;
             float f = mc.thePlayer.rotationYaw * 0.017453292F;
             mc.thePlayer.motionX -= MathHelper.sin(f) * 0.2F;
             mc.thePlayer.motionZ += MathHelper.cos(f) * 0.2F;
             mc.timer.timerSpeed = 2F;
         }else{
             mc.thePlayer.motionY *= 0.97D;
             mc.thePlayer.motionX *= 1.008D;
             mc.thePlayer.motionZ *= 1.008D;
         }
     }else{
         mc.thePlayer.motionX = 0D;
         mc.thePlayer.motionZ = 0D;
         mc.timer.timerSpeed = 1F;
     }
         }      
         if(BhopMode.equalsIgnoreCase("NCP")) {
        	  if(mc.thePlayer != null && mc.theWorld != null) {
                  if(mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.isCollidedHorizontally) {
                      mc.gameSettings.keyBindJump.pressed = false;
                      if(mc.thePlayer.onGround) {
                          mc.thePlayer.jump();
                          mc.timer.timerSpeed = 1.05F;
                          mc.thePlayer.motionX *= 1.0708F;
                          mc.thePlayer.motionZ *= 1.0708F;
                          mc.thePlayer.moveStrafing *= 2;
                      } else {
                          mc.thePlayer.jumpMovementFactor = 0.0265F;
                      }
                  }
              }
         }
         
         if(BhopMode.equalsIgnoreCase("Hypixel2")) {
        	 if (this.mc.gameSettings.keyBindForward.pressed) {

     			this.mc.gameSettings.keyBindSprint.pressed = true;

     			if (this.mc.thePlayer.onGround) {

     				this.mc.gameSettings.keyBindJump.pressed = false;
     				this.mc.thePlayer.jump();
     				this.boosted = false;

     			} else {

     				if (!this.boosted) {

     					this.mc.timer.timerSpeed = 1.28f;

     					double motionV = 0.15;

     					double x = this.move.instance.getPosForSetPosX(motionV);
     					double z = this.move.instance.getPosForSetPosZ(motionV);

     					this.mc.thePlayer.motionX = this.mc.thePlayer.motionX + x;
     					this.mc.thePlayer.motionZ = this.mc.thePlayer.motionZ + z;
     					this.boosted = true;
     				}

     			}
     		}
         }
         
         if(BhopMode.equalsIgnoreCase("Hypixel")) {
        	 if (mc.theWorld != null && mc.thePlayer != null) {
                 mc.gameSettings.keyBindJump.pressed = false;
                 if (mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.isCollidedHorizontally) {
                     if (mc.thePlayer.onGround) {
                         mc.thePlayer.jump();
                         mc.timer.timerSpeed = 1.05F;
                         mc.thePlayer.motionX *= 1.07F;
                         mc.thePlayer.motionZ *= 1.07F;
                     } else {
                         mc.thePlayer.jumpMovementFactor = 0.0265F;
                         mc.timer.timerSpeed = 1.4F;
                         double direction = mc.thePlayer.getDirection();
                         double speed = 1;
                         double currentMotion = Math.sqrt(
                                 mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
      
                         mc.thePlayer.motionX = -Math.sin(direction) * speed * currentMotion;
                         mc.thePlayer.motionZ = - Math.cos(direction) * speed * currentMotion;
                     }
                 }
             }

         }
         }
       }
    
  
  public void onDisable() {
	  mc.thePlayer.speedInAir = 0.0425F;
	  mc.timer.timerSpeed = 1f;
	  mc.thePlayer.jumpMovementFactor = 0.03f;
    super.onDisable();
  }
  
}

