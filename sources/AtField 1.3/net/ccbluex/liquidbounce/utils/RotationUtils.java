/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.MathHelper
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public final class RotationUtils
extends MinecraftInstance
implements Listenable {
    private static final Random random = new Random();
    private static double z;
    private static double y;
    public static Rotation targetRotation;
    public static Rotation serverRotation;
    private static int revTick;
    private static int keepLength;
    public static boolean keepCurrentRotation;
    private static double x;

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static VecRotation faceBlock(WBlockPos wBlockPos) {
        if (wBlockPos == null) {
            return null;
        }
        VecRotation vecRotation = null;
        for (double d = 0.1; d < 0.9; d += 0.1) {
            for (double d2 = 0.1; d2 < 0.9; d2 += 0.1) {
                for (double d3 = 0.1; d3 < 0.9; d3 += 0.1) {
                    WVec3 wVec3 = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
                    WVec3 wVec32 = new WVec3(wBlockPos).addVector(d, d2, d3);
                    double d4 = wVec3.distanceTo(wVec32);
                    double d5 = wVec32.getXCoord() - wVec3.getXCoord();
                    double d6 = wVec32.getYCoord() - wVec3.getYCoord();
                    double d7 = wVec32.getZCoord() - wVec3.getZCoord();
                    double d8 = Math.sqrt(d5 * d5 + d7 * d7);
                    Rotation rotation = new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(d7, d5)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(d6, d8)))));
                    WVec3 wVec33 = RotationUtils.getVectorForRotation(rotation);
                    WVec3 wVec34 = wVec3.addVector(wVec33.getXCoord() * d4, wVec33.getYCoord() * d4, wVec33.getZCoord() * d4);
                    IMovingObjectPosition iMovingObjectPosition = mc.getTheWorld().rayTraceBlocks(wVec3, wVec34, false, false, true);
                    if (iMovingObjectPosition == null || iMovingObjectPosition.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec32, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(vecRotation2.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = vecRotation2;
                }
            }
        }
        return vecRotation;
    }

    public static void faceBow(IEntity iEntity, boolean bl, boolean bl2, float f) {
        IEntityPlayerSP iEntityPlayerSP = mc.getThePlayer();
        double d = iEntity.getPosX() + (bl2 ? (iEntity.getPosX() - iEntity.getPrevPosX()) * (double)f : 0.0) - (iEntityPlayerSP.getPosX() + (bl2 ? iEntityPlayerSP.getPosX() - iEntityPlayerSP.getPrevPosX() : 0.0));
        double d2 = iEntity.getEntityBoundingBox().getMinY() + (bl2 ? (iEntity.getEntityBoundingBox().getMinY() - iEntity.getPrevPosY()) * (double)f : 0.0) + (double)iEntity.getEyeHeight() - 0.15 - (iEntityPlayerSP.getEntityBoundingBox().getMinY() + (bl2 ? iEntityPlayerSP.getPosY() - iEntityPlayerSP.getPrevPosY() : 0.0)) - (double)iEntityPlayerSP.getEyeHeight();
        double d3 = iEntity.getPosZ() + (bl2 ? (iEntity.getPosZ() - iEntity.getPrevPosZ()) * (double)f : 0.0) - (iEntityPlayerSP.getPosZ() + (bl2 ? iEntityPlayerSP.getPosZ() - iEntityPlayerSP.getPrevPosZ() : 0.0));
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f2 = LiquidBounce.moduleManager.getModule(FastBow.class).getState() ? 1.0f : (float)iEntityPlayerSP.getItemInUseDuration() / 20.0f;
        if ((f2 = (f2 * f2 + f2 * 2.0f) / 3.0f) > 1.0f) {
            f2 = 1.0f;
        }
        Rotation rotation = new Rotation((float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f, (float)(-Math.toDegrees(Math.atan(((double)(f2 * f2) - Math.sqrt((double)(f2 * f2 * f2 * f2) - (double)0.006f * ((double)0.006f * (d4 * d4) + 2.0 * d2 * (double)(f2 * f2)))) / ((double)0.006f * d4)))));
        if (bl) {
            RotationUtils.setTargetRotation(rotation);
        } else {
            RotationUtils.limitAngleChange(new Rotation(iEntityPlayerSP.getRotationYaw(), iEntityPlayerSP.getRotationPitch()), rotation, 10 + new Random().nextInt(6)).toPlayer(mc.getThePlayer());
        }
    }

    public static Rotation getNewRotations(WVec3 wVec3, boolean bl) {
        WVec3 wVec32 = new WVec3(RotationUtils.mc2.field_71439_g.field_70165_t, RotationUtils.mc2.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc2.field_71439_g.func_70047_e(), RotationUtils.mc2.field_71439_g.field_70161_v);
        double d = wVec3.getXCoord() - wVec32.getXCoord();
        double d2 = wVec3.getYCoord() - wVec32.getYCoord();
        double d3 = wVec3.getZCoord() - wVec32.getZCoord();
        double d4 = MathHelper.func_76133_a((double)(d * d + d3 * d3));
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-Math.atan2(d2, d4) * 180.0 / Math.PI);
        return new Rotation(f, f2);
    }

    public static Rotation getRotationFromPosition(double d, double d2, double d3) {
        double d4 = d - mc.getThePlayer().getPosX();
        double d5 = d2 - mc.getThePlayer().getPosZ();
        double d6 = d3 - mc.getThePlayer().getPosY() - 1.2;
        double d7 = MathHelper.func_76133_a((double)(d4 * d4 + d5 * d5));
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-Math.atan2(d6, d7) * 180.0 / Math.PI);
        return new Rotation(f, f2);
    }

    public static VecRotation calculateCenter(String string, String string2, double d, IAxisAlignedBB iAxisAlignedBB, boolean bl, boolean bl2) {
        double d2;
        double d3;
        double d4;
        VecRotation vecRotation = null;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 0.0;
        double d10 = 0.0;
        double d11 = 0.0;
        double d12 = 0.0;
        double d13 = 0.0;
        d5 = 0.15;
        d8 = 0.85;
        d11 = 0.1;
        d6 = 0.15;
        d9 = 1.0;
        d12 = 0.1;
        d7 = 0.15;
        d10 = 0.85;
        d13 = 0.1;
        WVec3 wVec3 = null;
        switch (string) {
            case "LiquidBounce": {
                d5 = 0.15;
                d8 = 0.85;
                d11 = 0.1;
                d6 = 0.15;
                d9 = 1.0;
                d12 = 0.1;
                d7 = 0.15;
                d10 = 0.85;
                d13 = 0.1;
                break;
            }
            case "Full": {
                d5 = 0.0;
                d8 = 1.0;
                d11 = 0.1;
                d6 = 0.0;
                d9 = 1.0;
                d12 = 0.1;
                d7 = 0.0;
                d10 = 1.0;
                d13 = 0.1;
                break;
            }
            case "HalfUp": {
                d5 = 0.1;
                d8 = 0.9;
                d11 = 0.1;
                d6 = 0.5;
                d9 = 0.9;
                d12 = 0.1;
                d7 = 0.1;
                d10 = 0.9;
                d13 = 0.1;
                break;
            }
            case "HalfDown": {
                d5 = 0.1;
                d8 = 0.9;
                d11 = 0.1;
                d6 = 0.1;
                d9 = 0.5;
                d12 = 0.1;
                d7 = 0.1;
                d10 = 0.9;
                d13 = 0.1;
                break;
            }
            case "CenterSimple": {
                d5 = 0.45;
                d8 = 0.55;
                d11 = 0.0125;
                d6 = 0.65;
                d9 = 0.75;
                d12 = 0.0125;
                d7 = 0.45;
                d10 = 0.55;
                d13 = 0.0125;
                break;
            }
            case "CenterLine": {
                d5 = 0.45;
                d8 = 0.55;
                d11 = 0.0125;
                d6 = 0.1;
                d9 = 0.9;
                d12 = 0.1;
                d7 = 0.45;
                d10 = 0.55;
                d13 = 0.0125;
            }
        }
        for (d4 = d5; d4 < d8; d4 += d11) {
            for (d3 = d6; d3 < d9; d3 += d12) {
                for (d2 = d7; d2 < d10; d2 += d13) {
                    WVec3 wVec32 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * d4, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * d3, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * d2);
                    Rotation rotation = RotationUtils.toRotation(wVec32, bl);
                    if (!bl2 && wVec32 != null) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec32, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(vecRotation2.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = vecRotation2;
                    wVec3 = wVec32;
                }
            }
        }
        if (vecRotation == null || string2 == "Off") {
            return vecRotation;
        }
        d4 = random.nextDouble();
        d3 = random.nextDouble();
        d2 = random.nextDouble();
        double d14 = iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX();
        double d15 = iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY();
        double d16 = iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ();
        double d17 = 999999.0;
        if (d14 <= d17) {
            d17 = d14;
        }
        if (d15 <= d17) {
            d17 = d15;
        }
        if (d16 <= d17) {
            d17 = d16;
        }
        d4 = d4 * d17 * d;
        d3 = d3 * d17 * d;
        d2 = d2 * d17 * d;
        double d18 = d17 * d / d14;
        double d19 = d17 * d / d15;
        double d20 = d17 * d / d16;
        WVec3 wVec33 = new WVec3(wVec3.getXCoord() - d18 * (wVec3.getXCoord() - iAxisAlignedBB.getMinX()) + d4, wVec3.getYCoord() - d19 * (wVec3.getYCoord() - iAxisAlignedBB.getMinY()) + d3, wVec3.getZCoord() - d20 * (wVec3.getZCoord() - iAxisAlignedBB.getMinZ()) + d2);
        switch (string2) {
            case "Horizonal": {
                wVec33 = new WVec3(wVec3.getXCoord() - d18 * (wVec3.getXCoord() - iAxisAlignedBB.getMinX()) + d4, wVec3.getYCoord(), wVec3.getZCoord() - d20 * (wVec3.getZCoord() - iAxisAlignedBB.getMinZ()) + d2);
                break;
            }
            case "Vertical": {
                wVec33 = new WVec3(wVec3.getXCoord(), wVec3.getYCoord() - d19 * (wVec3.getYCoord() - iAxisAlignedBB.getMinY()) + d3, wVec3.getZCoord());
            }
        }
        Object object = RotationUtils.toRotation(wVec33, bl);
        vecRotation = new VecRotation(wVec33, (Rotation)object);
        return vecRotation;
    }

    public static VecRotation searchCenter2(IAxisAlignedBB iAxisAlignedBB, boolean bl, boolean bl2, boolean bl3, boolean bl4, float f, boolean bl5, boolean bl6) {
        if (bl) {
            WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * (x * 0.3 + 1.0), iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * (y * 0.3 + 1.0), iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(wVec3, RotationUtils.toRotation(wVec3, bl3));
        }
        WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * x * 0.8, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * y * 0.8, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * z * 0.8);
        Rotation rotation = RotationUtils.toRotation(wVec3, bl3);
        WVec3 wVec32 = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (double d = 0.15; d < 0.85; d += 0.1) {
            for (double d2 = 0.15; d2 < 1.0; d2 += 0.1) {
                for (double d3 = 0.15; d3 < 0.85; d3 += 0.1) {
                    WVec3 wVec33 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * (bl5 ? d : 0.5), iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * (bl6 ? d2 : 0.5), iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * (bl5 ? d3 : 0.5));
                    Rotation rotation2 = RotationUtils.toRotation(wVec33, bl3);
                    double d4 = wVec32.distanceTo(wVec33);
                    if (d4 > (double)f || !bl4 && wVec33 == null) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec33, rotation2);
                    if (vecRotation != null) {
                        if (bl2) {
                            if (!(RotationUtils.getRotationDifference(vecRotation2.getRotation(), rotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), rotation))) continue;
                        } else if (!(RotationUtils.getRotationDifference(vecRotation2.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    }
                    vecRotation = vecRotation2;
                }
            }
        }
        return vecRotation;
    }

    public static Rotation toRotation(WVec3 wVec3, boolean bl) {
        WVec3 wVec32 = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        if (bl) {
            wVec32.addVector(mc.getThePlayer().getMotionX(), mc.getThePlayer().getMotionY(), mc.getThePlayer().getMotionZ());
        }
        double d = wVec3.getXCoord() - wVec32.getXCoord();
        double d2 = wVec3.getYCoord() - wVec32.getYCoord();
        double d3 = wVec3.getZCoord() - wVec32.getZCoord();
        return new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(d2, Math.sqrt(d * d + d3 * d3))))));
    }

    public static double getRotationDifference(Rotation rotation, Rotation rotation2) {
        return Math.hypot(RotationUtils.getAngleDifference(rotation.getYaw(), rotation2.getYaw()), rotation.getPitch() - rotation2.getPitch());
    }

    public static Rotation getRotations(Entity entity) {
        double d = entity.field_70165_t;
        double d2 = entity.field_70161_v;
        double d3 = entity.field_70163_u + (double)(entity.func_70047_e() / 2.0f);
        return RotationUtils.getRotationFromPosition(d, d2, d3);
    }

    public static WVec3 getCenter(IAxisAlignedBB iAxisAlignedBB) {
        return new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * 0.5, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * 0.5, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * 0.5);
    }

    public static WVec3 getVectorForRotation(Rotation rotation) {
        float f = (float)Math.cos(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = (float)Math.sin(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = (float)(-Math.cos(-rotation.getPitch() * ((float)Math.PI / 180)));
        float f4 = (float)Math.sin(-rotation.getPitch() * ((float)Math.PI / 180));
        return new WVec3(f2 * f3, f4, f * f3);
    }

    public static Rotation limitAngleChange(Rotation rotation, Rotation rotation2, float f) {
        float f2 = RotationUtils.getAngleDifference(rotation2.getYaw(), rotation.getYaw());
        float f3 = RotationUtils.getAngleDifference(rotation2.getPitch(), rotation.getPitch());
        return new Rotation(rotation.getYaw() + (f2 > f ? f : Math.max(f2, -f)), rotation.getPitch() + (f3 > f ? f : Math.max(f3, -f)));
    }

    public static void setTargetRotation(Rotation rotation, int n) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(mc.getGameSettings().getMouseSensitivity());
        targetRotation = rotation;
        keepLength = n;
    }

    public static Rotation OtherRotation(IAxisAlignedBB iAxisAlignedBB, WVec3 wVec3, boolean bl, boolean bl2, float f) {
        double d;
        double d2;
        double d3;
        WVec3 wVec32 = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        WVec3 wVec33 = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (d3 = 0.15; d3 < 0.85; d3 += 0.1) {
            for (d2 = 0.15; d2 < 1.0; d2 += 0.1) {
                for (d = 0.15; d < 0.85; d += 0.1) {
                    WVec3 wVec34 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * d3, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * d2, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * d);
                    Rotation rotation = RotationUtils.toRotation(wVec34, bl);
                    double d4 = wVec33.distanceTo(wVec34);
                    if (d4 > (double)f || !bl2 && wVec34 != null) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec34, rotation);
                    if (vecRotation != null) continue;
                    vecRotation = vecRotation2;
                }
            }
        }
        if (bl) {
            wVec32.addVector(mc.getThePlayer().getMotionX(), mc.getThePlayer().getMotionY(), mc.getThePlayer().getMotionZ());
        }
        d3 = wVec3.getXCoord() - wVec32.getXCoord();
        d2 = wVec3.getYCoord() - wVec32.getYCoord();
        d = wVec3.getZCoord() - wVec32.getZCoord();
        return new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(d, d3)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(d2, Math.sqrt(d3 * d3 + d * d))))));
    }

    public static Rotation getRotationsEntity(EntityLivingBase entityLivingBase) {
        return RotationUtils.getRotations(entityLivingBase.field_70165_t, entityLivingBase.field_70163_u + (double)entityLivingBase.func_70047_e() - 0.4, entityLivingBase.field_70161_v);
    }

    public static Rotation getNCPRotations(WVec3 wVec3, boolean bl) {
        WVec3 wVec32 = new WVec3(RotationUtils.mc2.field_71439_g.field_70165_t, RotationUtils.mc2.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc2.field_71439_g.func_70047_e(), RotationUtils.mc2.field_71439_g.field_70161_v);
        if (bl) {
            wVec32.addVector(RotationUtils.mc2.field_71439_g.field_70159_w, RotationUtils.mc2.field_71439_g.field_70181_x, RotationUtils.mc2.field_71439_g.field_70179_y);
        }
        double d = wVec3.getXCoord() - wVec32.getXCoord();
        double d2 = wVec3.getYCoord() - wVec32.getYCoord();
        double d3 = wVec3.getZCoord() - wVec32.getZCoord();
        double d4 = MathHelper.func_76133_a((double)(d * d + d3 * d3));
        return new Rotation((float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f, (float)(-Math.atan2(d2, d4) * 180.0 / Math.PI));
    }

    public static Rotation getRotationsNonLivingEntity(Entity entity) {
        return RotationUtils.getRotations(entity.field_70165_t, entity.field_70163_u + (entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 0.5, entity.field_70161_v);
    }

    public static VecRotation lockView(IAxisAlignedBB iAxisAlignedBB, boolean bl, boolean bl2, boolean bl3, boolean bl4, float f) {
        if (bl) {
            WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * (x * 0.3 + 1.0), iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * (y * 0.3 + 1.0), iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(wVec3, RotationUtils.toRotation(wVec3, bl3));
        }
        WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * x * 0.8, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * y * 0.8, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * z * 0.8);
        Rotation rotation = RotationUtils.toRotation(wVec3, bl3);
        WVec3 wVec32 = mc.getThePlayer().getPositionEyes(1.0f);
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 0.0;
        VecRotation vecRotation = null;
        d = 0.45;
        d4 = 0.55;
        d7 = 0.0125;
        d2 = 0.65;
        d5 = 0.75;
        d8 = 0.0125;
        d3 = 0.45;
        d6 = 0.55;
        d9 = 0.0125;
        for (double d10 = d; d10 < d4; d10 += d7) {
            for (double d11 = d2; d11 < d5; d11 += d8) {
                for (double d12 = d3; d12 < d6; d12 += d9) {
                    WVec3 wVec33 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * d10, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * d11, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * d12);
                    Rotation rotation2 = RotationUtils.toRotation(wVec33, bl3);
                    double d13 = wVec32.distanceTo(wVec33);
                    if (d13 > (double)f || !bl4 && wVec33 != null) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec33, rotation2);
                    if (vecRotation != null && !(bl2 ? RotationUtils.getRotationDifference(vecRotation2.getRotation(), rotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), rotation) : RotationUtils.getRotationDifference(vecRotation2.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = vecRotation2;
                }
            }
        }
        return vecRotation;
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (classProvider.isCPacketPlayer(iPacket)) {
            ICPacketPlayer iCPacketPlayer = iPacket.asCPacketPlayer();
            if (!(targetRotation == null || keepCurrentRotation || targetRotation.getYaw() == serverRotation.getYaw() && targetRotation.getPitch() == serverRotation.getPitch())) {
                iCPacketPlayer.setYaw(targetRotation.getYaw());
                iCPacketPlayer.setPitch(targetRotation.getPitch());
                iCPacketPlayer.setRotating(true);
            }
            if (iCPacketPlayer.isRotating()) {
                serverRotation = new Rotation(iCPacketPlayer.getYaw(), iCPacketPlayer.getPitch());
            }
        }
    }

    public static void reset() {
        keepLength = 0;
        targetRotation = revTick > 0 ? new Rotation(targetRotation.getYaw() - RotationUtils.getAngleDifference(targetRotation.getYaw(), mc.getThePlayer().getRotationYaw()) / (float)revTick, targetRotation.getPitch() - RotationUtils.getAngleDifference(targetRotation.getPitch(), mc.getThePlayer().getRotationPitch()) / (float)revTick) : null;
    }

    public static VecRotation searchCenter(IAxisAlignedBB iAxisAlignedBB, boolean bl, boolean bl2, boolean bl3, boolean bl4, float f) {
        if (bl) {
            WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * (x * 0.3 + 1.0), iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * (y * 0.3 + 1.0), iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(wVec3, RotationUtils.toRotation(wVec3, bl3));
        }
        WVec3 wVec3 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * x * 0.8, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * y * 0.8, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * z * 0.8);
        Rotation rotation = RotationUtils.toRotation(wVec3, bl3);
        WVec3 wVec32 = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (double d = 0.15; d < 0.85; d += 0.1) {
            for (double d2 = 0.15; d2 < 1.0; d2 += 0.1) {
                for (double d3 = 0.15; d3 < 0.85; d3 += 0.1) {
                    WVec3 wVec33 = new WVec3(iAxisAlignedBB.getMinX() + (iAxisAlignedBB.getMaxX() - iAxisAlignedBB.getMinX()) * d, iAxisAlignedBB.getMinY() + (iAxisAlignedBB.getMaxY() - iAxisAlignedBB.getMinY()) * d2, iAxisAlignedBB.getMinZ() + (iAxisAlignedBB.getMaxZ() - iAxisAlignedBB.getMinZ()) * d3);
                    Rotation rotation2 = RotationUtils.toRotation(wVec33, bl3);
                    double d4 = wVec32.distanceTo(wVec33);
                    if (d4 > (double)f || !bl4 && wVec33 != null) continue;
                    VecRotation vecRotation2 = new VecRotation(wVec33, rotation2);
                    if (vecRotation != null && !(bl2 ? RotationUtils.getRotationDifference(vecRotation2.getRotation(), rotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), rotation) : RotationUtils.getRotationDifference(vecRotation2.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = vecRotation2;
                }
            }
        }
        return vecRotation;
    }

    static {
        serverRotation = new Rotation(0.0f, 0.0f);
        keepCurrentRotation = false;
        x = random.nextDouble();
        y = random.nextDouble();
        z = random.nextDouble();
    }

    public static boolean isFaced(IEntity iEntity, double d) {
        return RaycastUtils.raycastEntity(d, arg_0 -> RotationUtils.lambda$isFaced$0(iEntity, arg_0)) != null;
    }

    public static Rotation getRotations(double d, double d2, double d3) {
        EntityPlayerSP entityPlayerSP = RotationUtils.mc2.field_71439_g;
        double d4 = d - entityPlayerSP.field_70165_t;
        double d5 = d2 - (entityPlayerSP.field_70163_u + (double)entityPlayerSP.func_70047_e());
        double d6 = d3 - entityPlayerSP.field_70161_v;
        double d7 = MathHelper.func_76133_a((double)(d4 * d4 + d6 * d6));
        float f = (float)(Math.atan2(d6, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d5, d7) * 180.0 / Math.PI));
        return new Rotation(f, f2);
    }

    public static double getRotationDifference(Rotation rotation) {
        return serverRotation == null ? 0.0 : RotationUtils.getRotationDifference(rotation, serverRotation);
    }

    private static boolean lambda$isFaced$0(IEntity iEntity, IEntity iEntity2) {
        return iEntity != null && iEntity.equals(iEntity2);
    }

    @EventTarget
    public void onTick(TickEvent tickEvent) {
        if (targetRotation != null && --keepLength <= 0) {
            if (revTick > 0) {
                --revTick;
                RotationUtils.reset();
            } else {
                RotationUtils.reset();
            }
        }
        if (random.nextGaussian() > 0.8) {
            x = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            y = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            z = Math.random();
        }
    }

    public static double getRotationDifference(IEntity iEntity) {
        Rotation rotation = RotationUtils.toRotation(RotationUtils.getCenter(iEntity.getEntityBoundingBox()), true);
        return RotationUtils.getRotationDifference(rotation, new Rotation(mc.getThePlayer().getRotationYaw(), mc.getThePlayer().getRotationPitch()));
    }

    public static void setTargetRotationReverse(Rotation rotation, int n, int n2) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(mc.getGameSettings().getMouseSensitivity());
        targetRotation = rotation;
        keepLength = n;
        revTick = n2 + 1;
    }

    public static Rotation getRotationsNonLivingEntity(IEntity iEntity) {
        return RotationUtils.getRotations(iEntity.getPosX(), iEntity.getPosY() + (iEntity.getEntityBoundingBox().getMaxY() - iEntity.getEntityBoundingBox().getMinY()) * 0.5, iEntity.getPosZ());
    }

    public static void setTargetRotation(Rotation rotation) {
        RotationUtils.setTargetRotation(rotation, 0);
    }

    public static float getAngleDifference(float f, float f2) {
        return ((f - f2) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
}

