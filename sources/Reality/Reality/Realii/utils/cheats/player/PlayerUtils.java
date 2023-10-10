package Reality.Realii.utils.cheats.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vector3d;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.utils.math.RotationUtil;
import libraries.javax.vecmath.Vector2f;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;


import static Reality.Realii.utils.cheats.player.Helper.mc;

import com.jcraft.jorbis.Block;

public class PlayerUtils {
	  public static int worldChanges;
	  public static String serverIp;
	    public static long timeJoined;
	   // this.movementYaw = event.getYaw();
	    public static float movementYaw;
	
	  
	  
	  public static boolean generalAntiPacketLog() {
	        return worldChanges > 1;
	    }
	  
	  public static void fakeDamage() {
	        mc.thePlayer.handleHealthUpdate((byte) 2);
	        mc.ingameGUI.healthUpdateCounter = mc.ingameGUI.updateCounter + 20;
	    }
	  
	  public static double getBaseMoveSpeed() {
	        double baseSpeed = mc.thePlayer.capabilities.getWalkSpeed() * 2.873;
	        if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
	            baseSpeed /= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
	        }
	        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	            baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
	        }
	        return baseSpeed;
	    }
	  
	  public static boolean isBlockUnder(final double height) {
	        return isBlockUnder(height, true);
	    }
	  public static net.minecraft.block.Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
	        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
	    }
	  public static boolean isBlockUnder(final double height, final boolean boundingBox) {
	        if (boundingBox) {
	            for (int offset = 0; offset < height; offset += 2) {
	                final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

	                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
	                    return true;
	                }
	            }
	        } else {
	            for (int offset = 0; offset < height; offset++) {
	                if (blockRelativeToPlayer(0, -offset, 0).isFullBlock()) {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

	    public static boolean isBlockUnder() {
	        return isBlockUnder(mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
	    }
	  
	   public static String getSessionLengthString() {
		   
	        long totalSeconds = (System.currentTimeMillis() - timeJoined) / 1000L;
	        long hours = totalSeconds / 3600L;
	        long minutes = totalSeconds % 3600L / 60L;
	        long seconds = totalSeconds % 60L;
	       // if(mc.getCurrentServerData() != null) {
	        	//hours = totalSeconds / 3600L;;
	        	//minutes = totalSeconds % 3600L / 60L;
	        	//seconds = totalSeconds % 60L;
	      //  }
	   //     if(mc.getCurrentServerData() == null) {
	        //	hours = 0;
	        //	minutes = 0;
	        //	seconds = 0;
	        	
	      //  }
	        return (hours > 0L ? hours + "h " : "") + minutes + "m " + seconds + "s";
	        
	        
	    }

	   
	   

	public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static boolean isMoving2() {
        return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
    }

    public static int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump))
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }


    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
    
    public static List<EntityPlayer> getLoadedPlayers() {
        return mc.theWorld.playerEntities;
    }



    public static float[] getRotations(EntityLivingBase entity) {
        EntityLivingBase entityLivingBase = entity;
        double diffX = entityLivingBase.posX - mc.thePlayer.posX;
        double diffZ = entityLivingBase.posZ - mc.thePlayer.posZ;
        double diffY = entityLivingBase.posY + (double) entity.getEyeHeight() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double X = diffX;
        double Z = diffZ;
        double dist = MathHelper.sqrt_double((double) (X * X + Z * Z));
        float yaw = (float) (Math.atan2((double) diffZ, (double) diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2((double) diffY, (double) dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }


    public static void rotate(float yaw, float pitch) {
//        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationYawHead = yaw;
        mc.thePlayer.renderYawOffset = yaw;
//        mc.thePlayer.rotationPitch = pitch;
        mc.thePlayer.rotationPitchHead = pitch;
    }
    
    public static void strafe(final double speed) {
        if (!isMoving()) {
            return;
        }

        final double yaw = direction();
        mc.thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        mc.thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }
    public void onUpdate(EventPreUpdate event) {
    	this.movementYaw = event.getYaw();
    }
    
    
    public static double direction() {
        float rotationYaw = movementYaw;

        if (mc.thePlayer.moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.thePlayer.moveForward < 0) {
            forward = -0.5F;
        } else if (mc.thePlayer.moveForward > 0) {
            forward = 0.5F;
        }
        if (mc.thePlayer.moveStrafing > 0) {
            rotationYaw -= 70 * forward;
        }

        if (mc.thePlayer.moveStrafing < 0) {
            rotationYaw += 70 * forward;
        }

        return Math.toRadians(rotationYaw);
    }
  


    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
        
    }
    
    public static void sendClick(final int button, final boolean state) {
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(keyBind, state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }
    
    
   
}
