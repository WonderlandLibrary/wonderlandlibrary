/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.combat;

import me.wintware.client.Main;
import me.wintware.client.module.movement.Scaffold;
import me.wintware.client.utils.other.MinecraftHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationSpoofer
implements MinecraftHelper {
    public static Float[] getLookAngles(Vec3d vec) {
        Float[] angles = new Float[2];
        Minecraft mc = Minecraft.getMinecraft();
        angles[0] = Float.valueOf((float)(Math.atan2(Minecraft.player.posZ - vec.zCoord, Minecraft.player.posX - vec.xCoord) / Math.PI * 180.0) + 90.0f);
        float heightdiff = (float)(Minecraft.player.posY + (double)Minecraft.player.getEyeHeight() - vec.yCoord);
        float distance = (float)Math.sqrt((Minecraft.player.posZ - vec.zCoord) * (Minecraft.player.posZ - vec.zCoord) + (Minecraft.player.posX - vec.xCoord) * (Minecraft.player.posX - vec.xCoord));
        angles[1] = Float.valueOf((float)(Math.atan2(heightdiff, distance) / Math.PI * 180.0));
        return angles;
    }

    public static boolean isFacingEntity(EntityLivingBase entityLivingBase) {
        float yaw = RotationSpoofer.getNeededRotations(entityLivingBase)[0];
        float pitch = RotationSpoofer.getNeededRotations(entityLivingBase)[1];
        float playerYaw = Minecraft.player.rotationYaw;
        float playerPitch = Minecraft.player.rotationPitch;
        float boudingBoxSize = 21.0f + entityLivingBase.getCollisionBorderSize();
        if (playerYaw < 0.0f) {
            playerYaw += 360.0f;
        }
        if (playerPitch < 0.0f) {
            playerPitch += 360.0f;
        }
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if (pitch < 0.0f) {
            pitch += 360.0f;
        }
        if (playerYaw >= yaw - boudingBoxSize && playerYaw <= yaw + boudingBoxSize) {
            return playerPitch >= pitch - boudingBoxSize && playerPitch <= pitch + boudingBoxSize;
        }
        return false;
    }

    public static float getAngleChange(EntityLivingBase entityIn) {
        float yaw = RotationSpoofer.getNeededRotations(entityIn)[0];
        float pitch = RotationSpoofer.getNeededRotations(entityIn)[1];
        float playerYaw = Minecraft.player.rotationYaw;
        float playerPitch = Minecraft.player.rotationPitch;
        if (playerYaw < 0.0f) {
            playerYaw += 360.0f;
        }
        if (playerPitch < 0.0f) {
            playerPitch += 360.0f;
        }
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if (pitch < 0.0f) {
            pitch += 360.0f;
        }
        float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }

    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - Minecraft.player.posX;
        double d1 = entityIn.posZ - Minecraft.player.posZ;
        double d2 = entityIn.posY + (double)entityIn.getEyeHeight() - (Minecraft.player.getEntityBoundingBox().minY + (double)Minecraft.player.getEyeHeight());
        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float)(MathHelper.atan2(d1, d0) * 180.0 / Math.PI) - 90.0f;
        float f1 = (float)(-(MathHelper.atan2(d2, d3) * 180.0 / Math.PI));
        return new float[]{f, f1};
    }

    public static float[] getRotations(EntityLivingBase entityIn, float speed) {
        float yaw = RotationSpoofer.updateRotation(Minecraft.player.rotationYaw, RotationSpoofer.getNeededRotations(entityIn)[0], speed);
        float pitch = RotationSpoofer.updateRotation(Minecraft.player.rotationPitch, RotationSpoofer.getNeededRotations(entityIn)[1], speed);
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float currentRotation, float intendedRotation, float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);
        if (f > increment) {
            f = increment;
        }
        if (f < -increment) {
            f = -increment;
        }
        return currentRotation + f;
    }

    public static boolean isLookingAtEntity(Entity e) {
        return RotationSpoofer.isLookingAt(e.getPositionEyes(Minecraft.getMinecraft().timer.renderPartialTicks));
    }

    public static void lookAtEntityPacketinstant(Entity e) {
        RotationSpoofer.lookAtPosPacket(e.getPositionEyes(Minecraft.getMinecraft().timer.renderPartialTicks));
    }

    public static void lookAtPosPacket(Vec3d vec) {
        Float[] angles = RotationSpoofer.getLookAngles(vec);
        Main.setFakeYaw(angles[0].floatValue());
        Main.setFakePitch(angles[1].floatValue());
    }

    public static boolean lookAtPacketSmooth(Vec3d vec, float maxyawchange, float maxpitchchange, boolean smartsmooth) {
        if (smartsmooth) {
            Float[] arrayOfFloat = RotationSpoofer.getLookAngles(vec);
            float f = Math.abs(MathHelper.wrapAngleTo180_float(arrayOfFloat[0].floatValue() - Main.getFakeYaw())) / 6.0f;
            Main.setFakeYaw(RotationSpoofer.clampF(arrayOfFloat[0].floatValue(), Main.getFakeYaw(), f * maxyawchange / 15.0f));
            Main.setFakePitch(RotationSpoofer.clampF(arrayOfFloat[1].floatValue(), Main.getFakePitch(), maxpitchchange));
            arrayOfFloat = RotationSpoofer.getLookAngles(vec);
            f = Math.abs(MathHelper.wrapAngleTo180_float(arrayOfFloat[0].floatValue() - Main.getFakeYaw())) / 2.0f;
            return f < 20.0f;
        }
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        Main.setFakeYaw(RotationSpoofer.clampF(targetangles[0].floatValue(), Main.getFakeYaw(), maxyawchange));
        Main.setFakePitch(RotationSpoofer.clampF(targetangles[1].floatValue(), Main.getFakePitch(), maxpitchchange));
        targetangles = RotationSpoofer.getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapAngleTo180_float(targetangles[0].floatValue() - Main.getFakeYaw())) / 2.0f;
        return change < 20.0f;
    }

    public static boolean isLookingAt(Vec3d vec) {
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        targetangles = RotationSpoofer.getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapAngleTo180_float(targetangles[0].floatValue() - Main.getFakeYaw())) / (float)Main.instance.setmgr.getSettingByName("RayCast Box").getValDouble();
        return change < 20.0f;
    }

    public static boolean isLookingAt2(Vec3d vec) {
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        targetangles = RotationSpoofer.getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapAngleTo180_float(targetangles[0].floatValue() - Main.getFakeYaw())) / 0.6f;
        return change < 20.0f;
    }

    public static boolean isLookingAt1(Vec3d vec) {
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        targetangles = RotationSpoofer.getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapAngleTo180_float(targetangles[0].floatValue() - Main.getFakeYaw())) / Scaffold.cast.getValFloat();
        return change < 20.0f;
    }

    public static void lookAtPosSmooth(Vec3d vec, float maxyawchange, float maxpitchchange) {
        Minecraft mc = Minecraft.getMinecraft();
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        Minecraft.player.rotationYaw = RotationSpoofer.clampF(targetangles[0].floatValue(), Minecraft.player.rotationYaw, maxyawchange);
        Minecraft.player.rotationPitch = RotationSpoofer.clampF(targetangles[1].floatValue(), Minecraft.player.rotationPitch, maxpitchchange);
    }

    public static void lookAtPos(Vec3d vec) {
        Minecraft mc = Minecraft.getMinecraft();
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        Minecraft.player.rotationYaw = targetangles[0].floatValue();
        Minecraft.player.rotationPitch = targetangles[1].floatValue();
    }

    public static double getLookDiff(Vec3d vec) {
        Minecraft mc = Minecraft.getMinecraft();
        Float[] targetangles = RotationSpoofer.getLookAngles(vec);
        return Math.sqrt((Minecraft.player.rotationYaw - targetangles[0].floatValue()) * (Minecraft.player.rotationYaw - targetangles[0].floatValue()) + (Minecraft.player.rotationPitch - targetangles[1].floatValue()) * (Minecraft.player.rotationPitch - targetangles[1].floatValue()));
    }

    public static float clampF(float intended, float current, float maxchange) {
        float change = MathHelper.wrapAngleTo180_float(intended - current);
        if (change > maxchange) {
            change = maxchange;
        } else if (change < -maxchange) {
            change = -maxchange;
        }
        return MathHelper.wrapAngleTo180_float(current + change);
    }

    public static float clampFPercentage(float intended, float current, float percentage) {
        float change = MathHelper.wrapAngleTo180_float(intended - current);
        return MathHelper.wrapAngleTo180_float(current + change * percentage);
    }

    public static void lookAtBlockPacket(BlockPos bpos, EnumFacing face) {
        Float[] angles = RotationSpoofer.getLookAngles(new Vec3d(bpos).addVector(0.5, 0.5, 0.5).addVector((double)face.getFrontOffsetX() * 0.5, (double)face.getFrontOffsetY() * 0.5, (double)face.getFrontOffsetZ() * 0.5));
        Main.setFakeYaw(angles[0].floatValue());
        Main.setFakePitch(angles[1].floatValue());
    }

    public static void lookAtBlockPacketSmooth(BlockPos bpos, EnumFacing face, float maxyaw, float maxpitch) {
        Float[] targetangles = RotationSpoofer.getLookAngles(new Vec3d(bpos).addVector(0.5, 0.5, 0.5).addVector((double)face.getFrontOffsetX() * 0.5, (double)face.getFrontOffsetY() * 0.5, (double)face.getFrontOffsetZ() * 0.5));
        Main.setFakeYaw(RotationSpoofer.clampF(targetangles[0].floatValue(), Main.getFakeYaw(), maxyaw));
        Main.setFakePitch(RotationSpoofer.clampF(targetangles[1].floatValue(), Main.getFakePitch(), maxpitch));
    }

    public static void lookAtBlockPacketSmooth(BlockPos bpos, EnumFacing face, float percentage) {
        Float[] targetangles = RotationSpoofer.getLookAngles(new Vec3d(bpos).addVector(0.5, 0.5, 0.5).addVector((double)face.getFrontOffsetX() * 0.5, (double)face.getFrontOffsetY() * 0.5, (double)face.getFrontOffsetZ() * 0.5));
        Main.setFakeYaw(RotationSpoofer.clampFPercentage(targetangles[0].floatValue(), Main.getFakeYaw(), percentage));
        Main.setFakePitch(targetangles[1].floatValue());
    }

    public static void lookAtAngleSmooth(float pitch, float yaw, float maxyaw, float maxpitch) {
        Main.setFakeYaw(RotationSpoofer.clampF(yaw, Main.getFakeYaw(), maxyaw));
        Main.setFakePitch(RotationSpoofer.clampF(pitch, Main.getFakePitch(), maxpitch));
    }

    public static boolean lookAtEntityPacketSmooth(Entity e, float horspeed, float verspeed, boolean smartSmooth) {
        return RotationSpoofer.lookAtPacketSmooth(e.getPositionEyes(Minecraft.getMinecraft().timer.renderPartialTicks), horspeed, verspeed, smartSmooth);
    }
}

