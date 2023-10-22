package epsilon.util;

import javax.swing.text.html.parser.Entity;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.listeners.EventMotion;
import epsilon.modules.Module.Category;
import epsilon.modules.combat.KillAura;
import epsilon.modules.combat.TargetStrafe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class MoveUtil {

	
	public double x, y, z;
	public float yaw, pitch;
	public boolean onGround;
	

	Minecraft mc = Minecraft.getMinecraft();
	

	public double getX() {
		return x;
	}

	public static double getBlocksPerSecond() {

		if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.ticksExisted < 1) {
			return 0;
		}

		return Minecraft.getMinecraft().thePlayer.getDistance(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.lastTickPosY, Minecraft.getMinecraft().thePlayer.lastTickPosZ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);


	}
	
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	public boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }
	public boolean isMovingServer() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F) && mc.thePlayer.motionX != 0 && mc.thePlayer.motionZ != 0;
    }
	
	public static void SetMomentum(double x, double y, double z) {
		Minecraft.getMinecraft().thePlayer.motionX = x;
		Minecraft.getMinecraft().thePlayer.motionY = y;
		Minecraft.getMinecraft().thePlayer.motionZ = z;
		
	}
	
	private float getYawTarget() {
        final double x = (KillAura.target.posX - (KillAura.target.lastTickPosX - KillAura.target.posX)) - mc.thePlayer.posX;
        final double z = (KillAura.target.posZ - (KillAura.target.lastTickPosZ - KillAura.target.posZ)) - mc.thePlayer.posZ;

        return (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0F);
    }
	
	public void strafeT(double moveSpeed) {
        Minecraft mc = Minecraft.getMinecraft();
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double yaw = getYawTarget();
        double modifier = moveForward == 0.0F ? 90.0F : moveForward < 0.0F ? -45.0F : 45.0F;
        boolean moving = moveForward != 0 || moveStrafe != 0;

        yaw += moveForward < 0.0F ? 180.0F : 0.0F;

        if (moveStrafe < 0.0F) {
            yaw += modifier;
        } else if (moveStrafe > 0.0F) {
            yaw -= modifier;
        }

        if (moving) {
            setX(-(MathHelper.sin((float) Math.toRadians(yaw)) * moveSpeed));
            setZ(MathHelper.cos((float) Math.toRadians(yaw)) * moveSpeed);
            if(mc.thePlayer.getDistanceToEntity(KillAura.target) > TargetStrafe.rad && TargetStrafe.rad !=0) {
            	mc.thePlayer.motionX = -x;
                mc.thePlayer.motionZ = -z;
            }else {
            	 mc.thePlayer.motionX = x;
                 mc.thePlayer.motionZ = z;
            }
        } 
    }
	
	public void setMoveSpeed(double moveSpeed) {
        Minecraft mc = Minecraft.getMinecraft();
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double yaw = mc.thePlayer.rotationYaw;
        double modifier = moveForward == 0.0F ? 90.0F : moveForward < 0.0F ? -45.0F : 45.0F;
        boolean moving = moveForward != 0 || moveStrafe != 0;

        yaw += moveForward < 0.0F ? 180.0F : 0.0F;

        if (moveStrafe < 0.0F) {
            yaw += modifier;
        } else if (moveStrafe > 0.0F) {
            yaw -= modifier;
        }

        if (moving) {
            setX(-(MathHelper.sin((float) Math.toRadians(yaw)) * moveSpeed));
            setZ(MathHelper.cos((float) Math.toRadians(yaw)) * moveSpeed);
            mc.thePlayer.motionX = x;
            mc.thePlayer.motionZ = z;
        }
    }
	
	
	
	public double getSpeed() {
       
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }
	public void strafe() {
        strafe(getSpeed());
    }
	public void strafe(final float speed) {
        if (!isMoving()) return;

        final double yaw = getDirection();

        mc.thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        mc.thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }

    public void strafe(final double speed) {
        if (!isMoving()) return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
        mc.thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
    }
    public double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (mc.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
    
    public double getJumpMotion(float motionY) {
        final Potion potion = Potion.jump;

        if (mc.thePlayer.isPotionActive(potion)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            motionY += (amplifier + 1) * 0.1F;
        }

        return motionY;
    }
    
	public double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

        return baseSpeed;
    }
	public static Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX + offsetX, Minecraft.getMinecraft().thePlayer.posY + offsetY, Minecraft.getMinecraft().thePlayer.posZ + offsetZ)).getBlock();
    }

    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }

	public static void setMotionStrafe(double speed,float yaw,double strafe,double forward) {
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	Minecraft.getMinecraft().thePlayer.motionX = 0;
        	Minecraft.getMinecraft().thePlayer.motionZ = 0;
        	
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
            Minecraft.getMinecraft().thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            Minecraft.getMinecraft().thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }

	
	
	public static void damage() {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 3.1, Minecraft.getMinecraft().thePlayer.posZ, false));
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, false));
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, true));
	}
	public static void damagePreservePacket() {
		Minecraft.getMinecraft().thePlayer.jump();
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 3, Minecraft.getMinecraft().thePlayer.posZ, false));
		Minecraft.getMinecraft().getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer(true));
	}
	
	public void timerDamageHypixel() {
		
        for(int i=0;i<8;i++) {
        	mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42f, mc.thePlayer.posZ, false));
        	mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        }
        if(!mc.thePlayer.onGround)
        	mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer(true));
	}
	
	public void zonecraftDamage() {
		timerDamageHypixel();
		
	}
	
	public void verusDamage() {
		mc.getNetHandler().sendPacketNoEvent(new C08PacketPlayerBlockPlacement
				(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
				new ItemStack(Blocks.stone.getItem
				(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f));
		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, Math.round(mc.thePlayer.posY+4), mc.thePlayer.posZ, false));
    	mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    	mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(true));
	}
		
	
	
	double expectedX;
    double expectedY;
    double expectedZ;
    
	
    public void packetComedy(double h, double v) {
    	
    	//I lied, there are no packets, take off your clothes.
    	
    	double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * h);
        expectedY = y + v;
        expectedZ = z + (Math.cos(yaw) * h);
        mc.thePlayer.setPosition(expectedX, expectedY, expectedZ);
        
    }
    
    public double[] getXZDifference(double x1, double z1, double x2, double z2) {
    	
    	double diffx = (x1 - x2) > (x2-x1) ? x1-x2 : x2-x1;
    	double diffz = (z1 - z2) > (z2-x1) ? z1-x2 : z2-z1;
    	
    	return new double[] {diffx, diffz};
    	
    }
    
    public double getRandomPositiveBoundsY(double min, double max) {
    	return Math.random() * (max - min) + min + 12;
    }
    public double getRandomNegativeBoundsY(double min, double max) {
    	return -(Math.random() * (max - min) + min + 12);
    }
    public double getDirectionWrappedTo90() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F && mc.thePlayer.moveStrafing == 0F) rotationYaw += 180F;

        final float forward = 1F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

	public boolean canSprint() {
		if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally)
			return true;
		return false;
	}
	
	public void place(BlockPos b, int placeDirect, ItemStack itemStack, float faceX, float faceY, float faceZ) {
		mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(b, placeDirect, itemStack, faceX, faceY, faceZ));
	}
	
	public double[] getForwardXYZ(double distX, double distY) {
		
		double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * distX);
        expectedY = y + distY;
        expectedZ = z + (Math.cos(yaw) * distX);
        
        return new double[]{x,y,z};
        
		
	}

	public double[] getForwardXYZ(double x, double y, double z, double distX, double distY) {
		
		final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * distX);
        expectedY = y + distY;
        expectedZ = z + (Math.cos(yaw) * distX);
        
        return new double[]{x,y,z};
        
		
	}
	
	public double[] getForwardXZ(double distX, double distY) {
		
		double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;

        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * distX);
        expectedZ = z + (Math.cos(yaw) * distX);
        
        
        return new double[]{x,z, expectedX, expectedZ};
        
		
	}

	public double[] getForwardXZ(double x, double z, double distX) {
		
		final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * distX);
        expectedZ = z + (Math.cos(yaw) * distX);
        
        return new double[]{x,z};
        
		
	}

	public double[] getXZDist(float speed, float cYaw) {
        double[] arr = new double[2];
        final double yaw = getDirectionRotation(cYaw, mc.thePlayer.moveStrafing, mc.thePlayer.moveForward);
        arr[0] = -Math.sin(yaw) * speed;
        arr[1] = Math.cos(yaw) * speed;
        return arr;
    }
	
	public double getDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if(pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(pForward < 0F)
            forward = -0.5F;
        else if(pForward > 0F)
            forward = 0.5F;

        if(pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if(pStrafe < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
	public static int getJumpMotion() {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
            return Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }


}
