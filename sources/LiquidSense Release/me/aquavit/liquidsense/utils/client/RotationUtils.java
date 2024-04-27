package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.Listenable;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.TickEvent;
import me.aquavit.liquidsense.module.modules.blatant.FastBow;
import me.aquavit.liquidsense.utils.entity.RaycastUtils;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public final class RotationUtils extends MinecraftInstance implements Listenable {

    private static Random random = new Random();

    private static int keepLength;

    public static Rotation targetRotation;
    public static Rotation serverRotation = new Rotation(0F, 0F);
    //public static Rotation exhiRotation = new Rotation(getMoveYaw(), 90F);

    private static double x = random.nextDouble();
    private static double y = random.nextDouble();
    private static double z = random.nextDouble();

    public static boolean keepCurrentRotation = false;

    public static float getRotations(EntityLivingBase ent , float otPos) {
        double x = ent.posX + Math.sin(otPos);
        double z = ent.posZ + Math.cos(otPos);
        return getRotationFromPosition(x, z);
    }

    public static float getMoveYaw() {
        float moveYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward != 0F && mc.thePlayer.moveStrafing == 0F) {
            if (mc.thePlayer.moveForward > 0) {
                moveYaw += 0;
            } else moveYaw += 180;
        } else if (mc.thePlayer.moveForward != 0F && mc.thePlayer.moveStrafing != 0F) {
            if (mc.thePlayer.moveForward > 0) {
                if (mc.thePlayer.moveStrafing > 0) {
                    moveYaw += -45;
                } else moveYaw += 45;
            } else {
                if (mc.thePlayer.moveStrafing > 0) {
                    moveYaw -= -45;
                } else moveYaw += 45;
            }
            if (mc.thePlayer.moveForward > 0) {
                moveYaw += 0;
            } else moveYaw += 180;
        } else if (mc.thePlayer.moveStrafing != 0F && mc.thePlayer.moveForward == 0F) {
            if(mc.thePlayer.moveStrafing > 0) {
                moveYaw += -90;
            } else moveYaw += 90;
        }
        return moveYaw;
    }

    public static VecRotation searchCenterforTargetStrafe(final AxisAlignedBB bb) {

        VecRotation vecRotation;

        final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        final Rotation rotation = toRotation(vec3, false);

        vecRotation = new VecRotation(vec3, rotation);

        return vecRotation;
    }

    public static float getRotationFromPosition(double x, double z) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;

        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        return yaw;
    }

    public static void faceBow(final Entity target, final boolean silent, final boolean predict, final float predictSize) {
        final EntityPlayerSP player = mc.thePlayer;

        final double posX = target.posX + (predict ? (target.posX - target.prevPosX) * predictSize : 0) - (player.posX + (predict ? (player.posX - player.prevPosX) : 0));
        final double posY = target.getEntityBoundingBox().minY + (predict ? (target.getEntityBoundingBox().minY - target.prevPosY) * predictSize : 0) + target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().minY + (predict ? (player.posY - player.prevPosY) : 0)) - player.getEyeHeight();
        final double posZ = target.posZ + (predict ? (target.posZ - target.prevPosZ) * predictSize : 0) - (player.posZ + (predict ? (player.posZ - player.prevPosZ) : 0));
        final double posSqrt = Math.sqrt(posX * posX + posZ * posZ);

        float velocity = LiquidSense.moduleManager.getModule(FastBow.class).getState() ? 1F : player.getItemInUseDuration() / 20F;
        velocity = (velocity * velocity + velocity * 2) / 3;

        if(velocity > 1) velocity = 1;

        final Rotation rotation = new Rotation(
                (float) (Math.atan2(posZ, posX) * 180 / Math.PI) - 90,
                (float) -Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(velocity * velocity * velocity * velocity - 0.006F * (0.006F * (posSqrt * posSqrt) + 2 * posY * (velocity * velocity)))) / (0.006F * posSqrt)))
        );

        if(silent)
            setTargetRotation(rotation);
        else
            limitAngleChange(new Rotation(player.rotationYaw, player.rotationPitch), rotation,10 +
                    new Random().nextInt(6)).toPlayer(mc.thePlayer);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static Vec3 getEyesPos() {
        float x = 0;
        float y = 0;
        float z = 0;
        x = (float) Minecraft.getMinecraft().thePlayer.posX;
        y = (float) Minecraft.getMinecraft().thePlayer.posY;
        z = (float) Minecraft.getMinecraft().thePlayer.posZ;
        if(y / 1000  < Minecraft.getMinecraft().thePlayer.getEyeHeight()) {
            y =  (float) (y - (0.75 * Minecraft.getMinecraft().thePlayer.getEyeHeight()));
        }
        return new Vec3(x,y + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(),z);
    }

    public static VecRotation Center(final AxisAlignedBB bb) {
        VecRotation vecRotation = null;
        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * (ySearch * 0.6), bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, false);
                    final VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                        vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }


    /**
     * Face block
     *
     * @param blockPos target block
     */
    public static VecRotation faceBlock(final BlockPos blockPos) {
        if (blockPos == null)
            return null;

        VecRotation vecRotation = null;

        for(double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
            for(double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                    final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                    final Vec3 posVec = new Vec3(blockPos).addVector(xSearch, ySearch, zSearch);
                    final double dist = eyesPos.distanceTo(posVec);

                    final double diffX = posVec.xCoord - eyesPos.xCoord;
                    final double diffY = posVec.yCoord - eyesPos.yCoord;
                    final double diffZ = posVec.zCoord - eyesPos.zCoord;

                    final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

                    final Rotation rotation = new Rotation(
                            MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                            MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                    );

                    final Vec3 rotationVector = getVectorForRotation(rotation);
                    final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * dist, rotationVector.yCoord * dist,
                            rotationVector.zCoord * dist);
                    final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false,
                            false, true);

                    if (obj != null && obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        final VecRotation currentVec = new VecRotation(posVec, rotation);

                        if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    /**
     * Translate vec to rotation
     *
     * @param vec target vec
     * @param predict predict new location of your body
     * @return rotation
     */
    public static Rotation toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if(predict) eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Rotation(MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                MathHelper.wrapAngleTo180_float((float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))))
        );
    }

    /**
     * Get the center of a box
     *
     * @param bb your box
     * @return center of box
     */
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    /**
     * Search good center
     *
     * @param bb enemy box
     * @return center
     */
    public static VecRotation searchCenter(final AxisAlignedBB bb, final float distance) {

        final Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);

        final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);

        VecRotation vecRotation = null;

        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final double vecDist = eyes.distanceTo(vec3);
                    final Rotation rotation = toRotation(vec3, false);
                    if (vecDist > distance)
                        continue;

                    if (isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    public static Rotation OtherRotation(final AxisAlignedBB bb,final Vec3 vec,final boolean throughWalls, final float distance) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);
        VecRotation vecRotation = null;
        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, false);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if(throughWalls || isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null)
                            vecRotation = currentVec;
                    }
                }
            }
        }

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));


    }

    public static VecRotation searchCenterAAC(final AxisAlignedBB bb, final boolean outborder, final boolean random,
                                           final boolean predict, final boolean throughWalls, final float distance) {
        if(outborder) {
            final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, toRotation(vec3, predict));
        }

        final Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);
        final Rotation randomRotation = toRotation(randomVec, predict);

        final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);

        VecRotation vecRotation = null;

        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if(throughWalls || isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null || (random ? getRotationDifference(currentVec.getRotation(), randomRotation) < getRotationDifference(vecRotation.getRotation(), randomRotation) : getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    public static VecRotation searchCenterNCP(final AxisAlignedBB bb,final boolean throughWalls, final float distance) {
        double ySearch;
        boolean entityonl = false;
        boolean entityonr = false;
        VecRotation vecRotation = null;
        if ((bb.maxX - bb.minX) < (bb.maxZ - bb.minZ)) {
            entityonr = true;
        }if ((bb.maxX - bb.minX) > (bb.maxZ - bb.minZ)) {
            entityonl = true;
        }if((bb.maxX - bb.minX) == (bb.maxZ - bb.minZ)) {
            entityonr = false;
            entityonl = false;
        }
        double  x = bb.minX + (bb.maxX - bb.minX) * (entityonl ? 0.25: 0.5);
        double  z = bb.minZ + (bb.maxZ - bb.minZ) * (entityonr ? 0.25: 0.5);
        for(double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.15D) {
            for (ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.15D) {
                for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.15D) {
                    double pitch = bb.minY + (bb.maxY - bb.minY) * ySearch;
                    Vec3 vec3 = null;
                    vec3 = new Vec3(x, pitch, z);
                    final Rotation rotation = toRotation(vec3, false);
                    final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);

                    final double vecDist = eyes.distanceTo(vec3);
                    if (vecDist > distance)
                        continue;

                    if (vecDist <= (double)distance && (throughWalls || isVisible(vec3))) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);
                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                            vecRotation = currentVec;
                        }
                    }
                }
            }
        }
        return vecRotation;
    }

    /**
     * Calculate difference between the client rotation and your entity
     *
     * @param entity your entity
     * @return difference between rotation
     */
    public static double getRotationDifference(final Entity entity) {
        final Rotation rotation = toRotation(getCenter(entity.getEntityBoundingBox()), true);

        return getRotationDifference(rotation, new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch));
    }

    /**
     * Calculate difference between the server rotation and your rotation
     *
     * @param rotation your rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation rotation) {
        return serverRotation == null ? 0D : getRotationDifference(rotation, serverRotation);
    }

    /**
     * Calculate difference between two rotations
     *
     * @param a rotation
     * @param b rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    /**
     * Limit your rotation using a turn speed
     *
     * @param currentRotation your current rotation
     * @param targetRotation your goal rotation
     * @param turnSpeed your turn speed
     * @return limited rotation
     */
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );
    }

    /**
     * Calculate difference between two angle points
     *
     * @param a angle point
     * @param b angle point
     * @return difference between angle points
     */
    private static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    /**
     * Calculate rotation to vector
     *
     * @param rotation your rotation
     * @return target vector
     */
    public static Vec3 getVectorForRotation(final Rotation rotation) { //
        float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292F);
        float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292F);
        Vec3 vec3 = null;
        vec3 = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
        VecRotation vecRotation = null;
        final Rotation rotate = toRotation(vec3, false);
        final VecRotation currentVec = new VecRotation(vec3, rotation);
        vecRotation = currentVec;
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    /**
     * Allows you to check if your crosshair is over your target entity
     *
     * @param targetEntity your target entity
     * @param blockReachDistance your reach
     * @return if crosshair is over target
     */
    public static boolean isFaced(final Entity targetEntity, double blockReachDistance) {
        return RaycastUtils.raycastEntity(blockReachDistance, entity -> entity == targetEntity) != null;
    }

    /**
     * Allows you to check if your enemy is behind a wall
     */
    public static boolean isVisible(final Vec3 vec3) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        return mc.theWorld.rayTraceBlocks(eyesPos, vec3) == null;
    }

    /**
     * Handle minecraft tick
     *
     * @param event Tick event
     */
    @EventTarget
    public void onTick(final TickEvent event) {
        if(targetRotation != null) {
            keepLength--;

            if (keepLength <= 0)
                reset();
        }

        if(random.nextGaussian() > 0.8D) x = Math.random();
        if(random.nextGaussian() > 0.8D) y = Math.random();
        if(random.nextGaussian() > 0.8D) z = Math.random();
    }

    /**
     * Handle packet
     *
     * @param event Packet Event
     */
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if(packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            if(targetRotation != null && !keepCurrentRotation && (targetRotation.getYaw() != serverRotation.getYaw() || targetRotation.getPitch() != serverRotation.getPitch())) {
                packetPlayer.yaw = targetRotation.getYaw();
                packetPlayer.pitch = targetRotation.getPitch();
                packetPlayer.rotating = true;
            }

            if(packetPlayer.rotating) serverRotation = new Rotation(packetPlayer.yaw, packetPlayer.pitch);
        }
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation, final int keepLength) {
        if(Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    /**
     * Reset your target rotation
     */
    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }

    /**
     * @return YESSSS!!!
     */
    @Override
    public boolean handleEvents() {
        return true;
    }
}

