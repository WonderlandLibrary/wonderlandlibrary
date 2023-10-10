package today.flux.utility;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import today.flux.event.MoveEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MoveUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void hypixelTeleport(final double[] startPos, final BlockPos endPos){
        double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos));
        double distanceEntreLesPackets = 0.31 + MoveUtils.getSpeedEffect() / 20;
        double xtp, ytp, ztp = 0;
        if(dist > distanceEntreLesPackets) {

            double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;

            xtp = mc.thePlayer.posX;
            ytp = mc.thePlayer.posY;
            ztp = mc.thePlayer.posZ;
            double count = 0;
            for (int i = 1; i < nbPackets; i++){
                double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);
                xtp += xdi;

                double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);
                ztp += zdi;

                double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);
                ytp += ydi;
                count ++;

                if(!mc.theWorld.getBlockState(new BlockPos(xtp, ytp-1, ztp)).getBlock().isFullBlock()){
                    if (count <= 2) {
                        ytp += 2E-8;
                    } else if (count >= 4) {
                        count = 0;
                    }
                }
                C03PacketPlayer.C04PacketPlayerPosition Packet = new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, false);
                PacketUtils.sendPacketNoEvent(Packet);
            }
            mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
        } else {
            mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
        }
    }


    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (float) (amplifier + 1) * 0.1F;
        }

        return baseJumpHeight;
    }

    public static double defaultSpeed() {
        return defaultSpeed(mc.thePlayer);
    }

    public static double defaultSpeed(EntityLivingBase entity) {
        return defaultSpeed(entity, 0.2);
    }

    public static double defaultSpeed(EntityLivingBase entity, double effectBoost) {
        double baseSpeed = 0.2873D;
        if (entity.isPotionActive(Potion.moveSpeed)) {
            int amplifier = entity.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + effectBoost * (amplifier + 1));
        }
        return baseSpeed;
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static void strafe(MoveEvent e) {
        strafe(e , getSpeed());
    }

    public static void strafe(final double d) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * d;
        mc.thePlayer.motionZ = Math.cos(yaw) * d;
    }

    public static void strafe(MoveEvent e , final double d) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        e.setX(mc.thePlayer.motionX = -Math.sin(yaw) * d);
        e.setZ(mc.thePlayer.motionZ = Math.cos(yaw) * d);
    }

    public static final void doStrafe(double speed) {
        if (!isMoving()) return;

        final double yaw = getYaw(true);
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public final void doStrafe(double speed, double yaw) {
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else
        if (mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static float getMovementDirection(final float forward,
                                             final float strafing,
                                             float yaw) {
        if (forward == 0.0F && strafing == 0.0F) return yaw;

        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f *
                (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

        if (reversed)
            yaw += 180.0f;
        if (strafing > 0.0f)
            yaw -= strafingYaw;
        else if (strafing < 0.0f)
            yaw += strafingYaw;

        return yaw;
    }

    public static boolean isOverVoid() {
        for (double posY = mc.thePlayer.posY; posY > 0.0D; posY--) {
            if (!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }

    public final void doStrafe() {
        doStrafe(getSpeed());
    }

    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return baseSpeed;
    }

    public static final List<Double> frictionValues = new ArrayList<>();
    public static double calculateFriction(final double moveSpeed, final double lastDist, final double baseMoveSpeedRef) {
        frictionValues.clear();
        frictionValues.add(lastDist - lastDist / 159.9999985);
        frictionValues.add(lastDist - (moveSpeed - lastDist) / 33.3);
        final double materialFriction = mc.thePlayer.isInWater() ? 0.8899999856948853 : (mc.thePlayer.isInLava() ? 0.5350000262260437 : 0.9800000190734863);
        frictionValues.add(lastDist - baseMoveSpeedRef * (1.0 - materialFriction));
        return Collections.min((Collection<? extends Double>)frictionValues);
    }

    public static final double getYaw(boolean strafing) {
        float rotationYaw = mc.thePlayer.rotationYawHead;
        float forward = 1F;

        final double moveForward = mc.thePlayer.movementInput.moveForward;
        final double moveStrafing = mc.thePlayer.movementInput.moveStrafe;
        final float yaw = mc.thePlayer.rotationYaw;

        if (moveForward < 0) {
            rotationYaw += 180F;
        }

        if (moveForward < 0) {
            forward = -0.5F;
        } else
        if (moveForward > 0) {
            forward = 0.5F;
        }

        if (moveStrafing > 0) {
            rotationYaw -= 90F * forward;
        } else
        if (moveStrafing < 0) {
            rotationYaw += 90F * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public static boolean isBlockNearBy(double distance) {
        double smallX = Math.min(mc.thePlayer.posX - distance, mc.thePlayer.posX + distance);
        double smallY = Math.min(mc.thePlayer.posY, mc.thePlayer.posY);
        double smallZ = Math.min(mc.thePlayer.posZ - distance, mc.thePlayer.posZ + distance);
        double bigX = Math.max(mc.thePlayer.posX - distance, mc.thePlayer.posX + distance);
        double bigY = Math.max(mc.thePlayer.posY, mc.thePlayer.posY);
        double bigZ = Math.max(mc.thePlayer.posZ - distance, mc.thePlayer.posZ + distance);
        int x = (int) smallX;
        while ((double) x <= bigX) {
            int y = (int) smallY;
            while ((double) y <= bigY) {
                int z = (int) smallZ;
                while ((double) z <= bigZ) {
                    if (!checkPositionValidity(new Vec3(x, y, z)) && checkPositionValidity(new Vec3(x, y + 1, z))) {
                        return true;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }

    public static boolean checkPositionValidity(Vec3 vec3) {
        BlockPos pos = new BlockPos(vec3);
        if (isBlockSolid(pos) || isBlockSolid(pos.add(0, 1, 0))) {
            return false;
        }
        return isSafeToWalkOn(pos.add(0, -1, 0));
    }

    private static boolean isBlockSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return block instanceof BlockSlab || block instanceof BlockStairs || block instanceof BlockCactus || block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSkull || block instanceof BlockPane || block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockGlass || block instanceof BlockPistonBase || block instanceof BlockPistonExtension || block instanceof BlockPistonMoving || block instanceof BlockStainedGlass || block instanceof BlockTrapDoor;
    }

    private static boolean isSafeToWalkOn(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return !(block instanceof BlockFence) && !(block instanceof BlockWall);
    }


    public static void setMotion(double speed) {
        setMotion(speed, mc.thePlayer.rotationYaw);
    }

    public static void setMotion(MoveEvent e, double speed, float yaw) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = e.x = 0;
        	mc.thePlayer.motionZ = e.z = 0;
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
            mc.thePlayer.motionX = e.x = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = e.z = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }

    public static void setMotion(MoveEvent e, double speed) {
        setMotion(e, speed, mc.thePlayer.rotationYaw);
    }

    public static void setMotion(double speed, float yaw) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
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

    public static boolean checkTeleport(double x, double y, double z,double distBetweenPackets){
    	double distx = mc.thePlayer.posX - x;
    	double disty = mc.thePlayer.posY - y;
    	double distz = mc.thePlayer.posZ - z;
    	double dist = Math.sqrt(mc.thePlayer.getDistanceSq(x, y, z));
    	double distanceEntreLesPackets = distBetweenPackets;
    	double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
    	
    	double xtp = mc.thePlayer.posX;
    	double ytp = mc.thePlayer.posY;
    	double ztp = mc.thePlayer.posZ;		
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (x - mc.thePlayer.posX)/( nbPackets);	
    			 xtp += xdi;
    			 
    			double zdi = (z - mc.thePlayer.posZ)/( nbPackets);	
    			 ztp += zdi;
    			 
    			double ydi = (y - mc.thePlayer.posY)/( nbPackets);	
    			ytp += ydi;			
    	    	AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3);
    	    	if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()){
    	    		return false;
    	    	}
    			
    		}
    	return true;
    }


    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isOnGround(Entity entity, double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump))
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }
    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }

    public static int getSpeedEffect(EntityPlayer player) {
        if (player.isPotionActive(Potion.moveSpeed))
            return player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return mc.theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }

    public static Block getBlockAtPosC(double x, double y, double z) {
        EntityPlayer inPlayer = mc.thePlayer;
        return mc.theWorld.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }

    public static float getDistanceToGround(Entity e) {
        if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
            return 0.0F;
        }
        for (float a = (float) e.posY; a > 0.0F; a -= 1.0F) {
            int[] stairs = {53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180};
            int[] exemptIds = {
                    6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59,
                    63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94,
                    104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150,
                    157, 171, 175, 176, 177};
            Block block = mc.theWorld.getBlockState(new BlockPos(e.posX, a - 1.0F, e.posZ)).getBlock();
            if (!(block instanceof BlockAir)) {
                if ((Block.getIdFromBlock(block) == 44) || (Block.getIdFromBlock(block) == 126)) {
                    return (float) (e.posY - a - 0.5D) < 0.0F ? 0.0F : (float) (e.posY - a - 0.5D);
                }
                int[] arrayOfInt1;
                int j = (arrayOfInt1 = stairs).length;
                for (int i = 0; i < j; i++) {
                    int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return (float) (e.posY - a - 1.0D) < 0.0F ? 0.0F : (float) (e.posY - a - 1.0D);
                    }
                }
                j = (arrayOfInt1 = exemptIds).length;
                for (int i = 0; i < j; i++) {
                    int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return (float) (e.posY - a) < 0.0F ? 0.0F : (float) (e.posY - a);
                    }
                }
                return (float) (e.posY - a + block.getBlockBoundsMaxY() - 1.0D);
            }
        }
        return 0.0F;
    }
    
    
    public static float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX +  (double) face.getFrontOffsetX()/2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ +  (double) face.getFrontOffsetZ()/2;
        double y = (block.getY() + 0.5);
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }
    public static boolean isBlockAboveHead(){
    	AxisAlignedBB bb =new AxisAlignedBB(mc.thePlayer.posX - 0.3, mc.thePlayer.posY+mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + 0.3,
 											mc.thePlayer.posX + 0.3, mc.thePlayer.posY+2.5 ,mc.thePlayer.posZ - 0.3);
 	  return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty();
    }
    public static boolean isCollidedH(double dist){
    	AxisAlignedBB bb =new AxisAlignedBB(mc.thePlayer.posX - 0.3, mc.thePlayer.posY+2, mc.thePlayer.posZ + 0.3,
											mc.thePlayer.posX + 0.3, mc.thePlayer.posY+3 ,mc.thePlayer.posZ - 0.3);
    	if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3 + dist, 0, 0)).isEmpty()) {
    		return true;
    	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3 - dist, 0, 0)).isEmpty()) {
    		return true;
    	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, 0.3 + dist)).isEmpty()) {
    		return true;
    	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, -0.3 - dist)).isEmpty()) {
    		return true;
	 	}
    	return false;
    }
    public static boolean isRealCollidedH(double dist){
		AxisAlignedBB bb =new AxisAlignedBB(mc.thePlayer.posX - 0.3, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ + 0.3,
											mc.thePlayer.posX + 0.3, mc.thePlayer.posY+1.9 ,mc.thePlayer.posZ - 0.3);
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3 + dist, 0, 0)).isEmpty()) {
		  	return true;
	  	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3 - dist, 0, 0)).isEmpty()) {
		  	return true;
	  	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, 0.3 + dist)).isEmpty()) {
		  return true;
	  	}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, -0.3 - dist)).isEmpty()) {
		  	return true;
	  	}
	  return false;
    }

    public static boolean isOnLiquid() {
        if (mc.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        final int y = (int)mc.thePlayer.boundingBox.offset(0.0, -0.0, 0.0).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
}
