package Velo.api.Util.Other;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.other.PlayerUtil;
import Velo.api.Util.Other.other.RotationUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Modules.combat.Killaura;
import Velo.impl.Modules.combat.TargetStrafe;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class MovementUtil {
	
	/*MADE BY TAB2
	 I HATE MAKING UTILS
	 BUT THEY ARE NEED
	 XD
	 WHY DOES IT HAVE TO BE ME
	I DONT WANT TO BE CALLED A SKID
	 */
	
	

	   public static final double BUNNY_SLOPE = 0.72D;
	   public static final double SPRINTING_MOD = 1.2999999523162842D;
	   public static final double SNEAK_MOD = 0.30000001192092896D;
	   public static final double ICE_MOD = 2.5D;
	   public static final double VANILLA_JUMP_HEIGHT = 0.41999998688697815D;
	   public static final double WALK_SPEED = 0.22100000083446503D;
	   private static final List<Double> frictionValues = Arrays.asList(0.0D, 0.0D, 0.0D);
	   private static final double AIR_FRICTION = 0.9800000190734863D;
	   private static final double WATER_FRICTION = 0.8899999856948853D;
	   private static final double LAVA_FRICTION = 0.5350000262260437D;
	   private static final double SWIM_MOD = 0.5203619984250619D;
	   private static final double[] DEPTH_STRIDER_VALUES = new double[]{1.0D, 1.4304347400741908D, 1.7347825295420374D, 1.9217391028296074D};
	   private static final double MIN_DIST = 0.001D;
	   public static final double MAX_DIST = 2.149D;
	   public static final double BUNNY_DIV_FRICTION = 159.999D;
	
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean isMoving() {
		return  mc.thePlayer != null && mc.thePlayer.movementInput.moveStrafe != 0f || (mc.thePlayer.movementInput.moveForward != 0f);
	}
	 public static float applyFriction(float speed) {
	      float percent = 0.0F;
	      if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
	         percent = 99.5F;
	      }

	      if (mc.thePlayer.isMoving() && !mc.thePlayer.onGround) {
	         percent = 98.0F;
	      }

	      if (mc.thePlayer.isMoving() && mc.thePlayer.isInWater()) {
	         percent = 80.3F;
	      }

	      float value = speed / 100.0F * percent;
	      return value;
	   }

	   public static float applyCustomFriction(float speed, float friction) {
	      float value = speed / 100.0F * friction;
	      return value;
	   }
	
	
	
	public static boolean isBlockUnderneath(BlockPos pos) {
		for (int k = 0; k < pos.getY() + 1; k++) {
			if (mc.theWorld.getBlockState(new BlockPos(pos.getX(), k, pos.getZ())).getBlock().getMaterial() != Material.air) {
				return true;
			}
		}
		return false;
	}
	
	
	public static void actualSetSpeed(double moveSpeed) {
  		setSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
  	}
	
    public static void setSpeed(final EventMovement moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }
    
    public static double defaultMoveSpeed() {
        return mc.thePlayer.isSprinting() ? 0.28700000047683716 : 0.22300000488758087;
    }
    
    
    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {

        double fforward = forward;
        double sstrafe = strafe;
        float yyaw = yaw;
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;


    }
    
    
	
    public static void setSpeed1( EventMovement e, double speed) {
        final EntityPlayerSP player = mc.thePlayer;
        if (player.isMoving()) {
	        final TargetStrafe targetStrafe = (TargetStrafe) new TargetStrafe();
	        final Killaura killAura = (Killaura) new Killaura();
	        if (targetStrafe.isEnabled() && (!targetStrafe.space.isEnabled() || Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
	            final EntityLivingBase target = killAura.target;
	            if (target != null) {
	                float dist = mc.thePlayer.getDistanceToEntity(target);
	                double radius = targetStrafe.range.getValue();
	                setSpeed(e, speed, dist <= radius + 1.0E-4D ? 0 : 1, dist <= radius + 1.0D ? targetStrafe.dir : 0, killAura.getKaRotations(target)[0]);
	                return;
	            }
	        }
	        setSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
        }
    }
    
    public static void setSpeed( EventMovement e, double speed, float forward, float strafing, float yaw) {
        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f *
                (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

        if (reversed)
            yaw += 180.0f;
        if (strafing > 0.0f)
            yaw -= strafingYaw;
        else if (strafing < 0.0f)
            yaw += strafingYaw;

        double x = Math.cos(Math.toRadians(yaw + 90.0f));
        double z = Math.cos(Math.toRadians(yaw));

        e.setX(x * speed);
        e.setZ(z * speed);
    }
	
    public static void setSpeed12(final EventMovement e, double speed, float forward, float strafing, float yaw) {
        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f *
                (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

        if (reversed)
            yaw += 180.0f;
        if (strafing > 0.0f)
            yaw -= strafingYaw;
        else if (strafing < 0.0f)
            yaw += strafingYaw;

        double x = Math.cos(Math.toRadians(yaw + 90.0f));
        double z = Math.cos(Math.toRadians(yaw));

        e.setX(x * speed);
        e.setZ(z * speed);
    }
    
    

    public static double jumpHeight() {
        if (mc.thePlayer.isPotionActive(Potion.jump))
            return 0.419999986886978 + 0.1 * (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
        else
            return 0.419999986886978;
    }

    public double getTickDist() {
        double xDist = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(zDist, 2));
    }
    
    public static void setSpeed(final EventMovement moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -25 : 25);
            } else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 25 : -25);
            }
            strafe = 0.0F;
            if (forward > 0.0) {
                forward = 0.1F;
            } else if (forward < 0.0) {
                forward = -0.1F;
            }
        }

        if (strafe > 0.0) {
            strafe = 1F;
        } else if (strafe < 0.0) {
            strafe = -1F;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        moveEvent.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);

    }

    public static boolean isMovingOnGround(){
        return isMoving() && mc.thePlayer.onGround;
    }
	
	public static double getBlocksPerSecond() {
		
		if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
			return 0;
		}
		
		return mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);
		
	}
	
	public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }
	
	
    public static void strafe() {
        strafe(getSpeed());
    }

    
    
    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
    
    
    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = 0;
        	mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)); 
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
    
    
    
    public static void setMotion(double speed, float directionInYaw) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = directionInYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = 0;
        	mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)); 
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
    
    
    
    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isOnGround(double height, EntityPlayer player) {
        if (!mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


public static boolean canStep(double height) {
    	
    	if (!mc.thePlayer.isCollidedHorizontally || !isOnGround(0.001))
    		return false;
    	
		if ((!mc.theWorld
				.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().expand(0.1, 0, 0).offset(0.0D, height - 0.1, 0.0D))
				.isEmpty()
				&& mc.theWorld
						.getCollidingBoundingBoxes(mc.thePlayer,
								mc.thePlayer.getEntityBoundingBox().expand(0.1, 0, 0).offset(0.0D, height + 0.1, 0.0D))
						.isEmpty())
				|| (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().expand(-0.1, 0, 0).offset(0.0D, height - 0.1, 0.0D))
						.isEmpty()
						&& mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
								mc.thePlayer.getEntityBoundingBox().expand(-0.1, 0, 0).offset(0.0D, height + 0.1,
										0.0D))
								.isEmpty())
				|| (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().expand(0, 0, 0.1).offset(0.0D, height - 0.1, 0.0D))
						.isEmpty()
						&& mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
								mc.thePlayer.getEntityBoundingBox().expand(0, 0, 0.1).offset(0.0D, height + 0.1, 0.0D))
								.isEmpty())
				|| (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().expand(0, 0, -0.1).offset(0.0D, height - 0.1, 0.0D))
						.isEmpty()
						&& mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
								.expand(0, 0, -0.1).offset(0.0D, height + 0.1, 0.0D)).isEmpty())) {
			return true;
		} else {
			return false;
		}
    }

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}
    
	   public static void setSpeed(EventPreMotion moveEvent, double moveSpeed) {
	      setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, (double)mc.thePlayer.movementInput.moveStrafe, (double)mc.thePlayer.movementInput.moveForward);
	   }

	   public static void setSpeed(EventPreMotion moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
	      double forward = pseudoForward;
	      double strafe = pseudoStrafe;
	      float yaw = pseudoYaw;
	      if (pseudoForward != 0.0D) {
	         if (pseudoStrafe > 0.0D) {
	            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? -45 : 45);
	         } else if (pseudoStrafe < 0.0D) {
	            yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? 45 : -45);
	         }

	         strafe = 0.0D;
	         if (pseudoForward > 0.0D) {
	            forward = 1.0D;
	         } else if (pseudoForward < 0.0D) {
	            forward = -1.0D;
	         }
	      }

	      if (strafe > 0.0D) {
	         strafe = 1.0D;
	      } else if (strafe < 0.0D) {
	         strafe = -1.0D;
	      }

	      double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
	      double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
	      moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
	      moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
	   }
	
	private void mineplexDamage(EntityPlayerSP thePlayer) {
	      NetHandlerPlayClient net = mc.getNetHandler();
	      double offset = 0.060100000351667404D;

	      for(int i = 0; i < 20; ++i) {
	         for(int p = 0; (double)p < (double)PlayerUtil.getMaxFallDist() / 0.060100000351667404D + 1.0D; ++p) {
	        	 net.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.060100000351667404D, mc.thePlayer.posZ, false));
	        	 net.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5.000000237487257E-4D, mc.thePlayer.posZ, false));
	         }
	      }

	      net.addToSendQueue(new C03PacketPlayer(true));
	   }
	
    public static void speed(Float s) {
        if (!isMoving()) 
        	return;
        		
        Double y = dir();
        mc.thePlayer.motionX = -Math.sin(y) * s;
        mc.thePlayer.motionZ = Math.cos(y) * s;
    }
    
    public static Double dir() {

    	double ry = (double)mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0f) ry += 180f;
        float f = 1f;
        if (mc.thePlayer.moveForward < 0f) {
        	f = -0.5f ;
        }
        		else if (mc.thePlayer.moveForward > 0f) {
        			f = 0.5f;
        		}
        
        
        if (mc.thePlayer.moveStrafing < 0f) ry += 90f * f;
        if (mc.thePlayer.moveStrafing > 0f) ry -= 90f * f;
      
    	return Math.toRadians(ry);
    }
    
    public static double getBaseSpeed() {
 double baseSpeed;

        return baseSpeed = 0.2873D;
     }

     public static double getBaseSpeedAAC() {
        double baseSpeed = 0.24D;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
           int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
           baseSpeed *= 1.0D + 0.0525D * (double)(amplifier + 1);
        }

        return baseSpeed;
     }

     public static float getSensitivityMultiplier() {
        float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return f * f * f * 8.0F * 0.15F;
     }

  



     public static double getMotion(EntityPlayerSP player) {
        return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
     }

   




     public static double calculateFriction(double moveSpeed, double lastDist, double baseMoveSpeed) {
        frictionValues.set(0, lastDist - lastDist / 159.999D);
        frictionValues.set(1, lastDist - (moveSpeed - lastDist) / 33.3D);
        double materialFriction = mc.thePlayer.isInWater() ? 0.8899999856948853D : (mc.thePlayer.isInLava() ? 0.5350000262260437D : 0.9800000190734863D);
        frictionValues.set(2, lastDist - baseMoveSpeed * (1.0D - materialFriction));
        return (Double)Collections.min(frictionValues);
     }



   

    public static void setSpeed(double speed) {
        EntityPlayerSP player = mc.thePlayer;
        if (player.isMoving()) {
           TargetStrafe targetStrafe = new TargetStrafe();
           Killaura killAura = new Killaura();
           if (ModuleManager.get("TargetStrafe").isEnabled() && (!targetStrafe.space.isEnabled() || Keyboard.isKeyDown(57))) {
              EntityLivingBase target = killAura.target;
              if (target != null) {
                 float dist = mc.thePlayer.getDistanceToEntity(target);
                 double radius = (Double)targetStrafe.range.getValue();
                 setSpeed(speed, (double)dist <= radius + 1.0E-4D ? 0.0F : 1.0F, (double)dist <= radius + 1.0D ? (float)targetStrafe.dir : 0.0F, RotationUtils.getYawToEntity(target, false));
                 return;
              }
           }

           setSpeed(speed, player.moveForward, player.moveStrafing, player.rotationYaw);
        }

     }

   

     
   

 

 

}
