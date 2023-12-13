package summer.base.utilities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import summer.cheat.eventsystem.events.player.EventMotion;

public class MoveUtils {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	
	
	public static void setMoveSpeed(EventMotion eventMotion, double speed, String mode) {
		
		double f = mc.thePlayer.movementInput.moveForward;
		double s = mc.thePlayer.movementInput.moveStrafe;
		
		float playerYaw = mc.thePlayer.rotationYaw;
		if(f != 0) {
			if(s > 0) {
				playerYaw += (f > 0 ? -42 : 42);
			} else if (s < 0) {
                playerYaw += (f > 0 ? 42 : -42);
            }
			s = 0;
			if(f > 0) {
				f = 1;
			} else if (f < 0) {
				f = -1;
			}
		}
		
		switch(mode) {
		case "event":
			if(mode != null) {
				eventMotion.setX(f * speed * Math.cos(Math.toRadians(playerYaw + 90)) + s * speed * Math.sin(Math.toRadians(playerYaw + 90)));
				eventMotion.setZ(f * speed * Math.sin(Math.toRadians(playerYaw + 90)) - s * speed * Math.cos(Math.toRadians(playerYaw + 90)));
			}
			break;
		case "default":
			if(mode != null) {
				mc.thePlayer.motionX = f * speed * Math.cos(Math.toRadians(playerYaw + 90)) + s * speed * Math.sin(Math.toRadians(playerYaw + 90));
				mc.thePlayer.motionZ = f * speed * Math.sin(Math.toRadians(playerYaw + 90)) - s * speed * Math.cos(Math.toRadians(playerYaw + 90));
			}
			break;
		}
	}

	public static void setMotion(EventMotion e, final double speed) {
        double forward = MoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
        	if (e != null) {
        		e.setX(0);
        		e.setZ(0);
        	} else {
                MoveUtils.mc.thePlayer.motionX = 0.0;
                MoveUtils.mc.thePlayer.motionZ = 0.0;
        	}
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            if (e != null) {
                e.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 91.50F)));
                e.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.25F)));
            } else {
            	mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 91.50F));
            	mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.25F));
            }
        }
    }

	public static double defaultSpeed() {
        double baseSpeed = 0.30;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

	public static int getJumpEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

	public static int getSpeedEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
	public static float getMaxFallDist() {
	      PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
	      int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
	      return (float)(mc.thePlayer.getMaxFallHeight() + f);
	   }

	public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
	public static double getBaseMovementSpeed() {
        double baseSpeed = 0.29;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1 + .2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return baseSpeed;
    }

	public static void setMotion(EventMotion e, double baseMovementSpeed, int i) {
		double forward = MoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
        	if (e != null) {
        		e.setX(0);
        		e.setZ(0);
        	} else {
                MoveUtils.mc.thePlayer.motionX = 0.0;
                MoveUtils.mc.thePlayer.motionZ = 0.0;
        	}
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            if (e != null) {
                e.setX(forward * baseMovementSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * baseMovementSpeed * Math.sin(Math.toRadians(yaw + 91.50F)));
                e.setZ(forward * baseMovementSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * baseMovementSpeed * Math.cos(Math.toRadians(yaw + 90.25F)));
            } else {
            	mc.thePlayer.motionX = forward * baseMovementSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * baseMovementSpeed * Math.sin(Math.toRadians(yaw + 91.50F));
            	mc.thePlayer.motionZ = forward * baseMovementSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * baseMovementSpeed * Math.cos(Math.toRadians(yaw + 90.25F));
            }
        }
	}

	 public static boolean isMoving() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()
                || Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()
                || Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown()
                || Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
    }

	 public static void setMotionWithValues(EventMotion em, double speed, float yaw, double forward, double strafe) {
		    if (forward == 0.0D && strafe == 0.0D) {
		      if (em != null) {
		        em.setX(0.0D);
		        em.setZ(0.0D);
		      } else {
		        mc.thePlayer.motionX = 0.0D;
		        mc.thePlayer.motionZ = 0.0D;
		      } 
		    } else {
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
		      if (em != null) {
		        em.setX(forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F))));
		        em.setZ(forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F))));
		      } else {
		        mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
		        mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
		      } 
		    } 
		  }

	 public static boolean isBlockUnderneath(BlockPos pos) {
		    for (int k = 0; k < pos.getY() + 1; k++) {
		      if (mc.theWorld.getBlockState(new BlockPos(pos.getX(), k, pos.getZ())).getBlock().getMaterial() != Material.air)
		        return true; 
		    } 
		    return false;
		  }
	 public static double getBaseSpeed() {
	        double baseSpeed = 0.2873;
	        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
	            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
	        }
	        return baseSpeed;
	    }

	 public static void setMotion(EventMotion em, double speed, double strafeSpeed) {
	        double forward = mc.thePlayer.movementInput.moveForward;
	        double strafe = mc.thePlayer.movementInput.moveStrafe * strafeSpeed;
	        float yaw = mc.thePlayer.rotationYaw;
	        if (forward == 0.0 && strafe == 0.0) {
	        	if (em != null) {
	        		em.setX(0);
	        		em.setZ(0);
	        	} else {
	                mc.thePlayer.motionX = 0.0;
	                mc.thePlayer.motionZ = 0.0;
	        	}
	        }
	        else {
	            if (forward != 0.0) {
	                if (strafe > 0.0) {
	                    yaw += ((forward > 0.0) ? -45 : 45);
	                }
	                else if (strafe < 0.0) {
	                    yaw += ((forward > 0.0) ? 45 : -45);
	                }
	                strafe = 0.0;
	                if (forward > 0.0) {
	                    forward = 1.0;
	                }
	                else if (forward < 0.0) {
	                    forward = -1.0;
	                }
	            }
	            if (em != null) {
	                em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90f)));
	                em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90f)));
	            } else {
	            	mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90f));
	            	mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90f));
	            }
	        }
		}
	 public static double getMinFallDist() {
			double minDist = 3.0;
			if (mc.thePlayer.isPotionActive(Potion.jump))
				minDist += mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
			return minDist;
		}
	 public static boolean isOnGround() {
			return mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically;
		}
	 public static boolean isInLiquid() {
			return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
		}
}
        
    
