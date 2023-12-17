// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.movement;

import ru.tuskevich.event.events.impl.EventPreMove;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;

public class MovementUtils implements Utility
{
    public static final double WALK_SPEED = 0.221;
    
    public static void setMotion(final double speed) {
        final Minecraft mc = MovementUtils.mc;
        double forward = Minecraft.player.movementInput.moveForward;
        final Minecraft mc2 = MovementUtils.mc;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        final Minecraft mc3 = MovementUtils.mc;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            final Minecraft mc4 = MovementUtils.mc;
            Minecraft.player.motionX = 0.0;
            final Minecraft mc5 = MovementUtils.mc;
            Minecraft.player.motionZ = 0.0;
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
            final double sin = MathHelper.sin((float)Math.toRadians(yaw + 90.0f));
            final double cos = MathHelper.cos((float)Math.toRadians(yaw + 90.0f));
            final Minecraft mc6 = MovementUtils.mc;
            Minecraft.player.motionX = forward * speed * cos + strafe * speed * sin;
            final Minecraft mc7 = MovementUtils.mc;
            Minecraft.player.motionZ = forward * speed * sin - strafe * speed * cos;
        }
    }
    
    public static boolean isUnderBedrock() {
        final Minecraft mc = MovementUtils.mc;
        if (Minecraft.player.posY <= 3.0) {
            final WorldClient world = MovementUtils.mc.world;
            final Minecraft mc2 = MovementUtils.mc;
            final Vec3d positionVector = Minecraft.player.getPositionVector();
            final Minecraft mc3 = MovementUtils.mc;
            final double posX = Minecraft.player.posX;
            final double yIn = 0.0;
            final Minecraft mc4 = MovementUtils.mc;
            final RayTraceResult trace = world.rayTraceBlocks(positionVector, new Vec3d(posX, yIn, Minecraft.player.posZ), false, false, false);
            return trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK;
        }
        return false;
    }
    
    public static double[] forward(final double speed) {
        final Minecraft mc = MovementUtils.mc;
        float forward = Minecraft.player.movementInput.moveForward;
        final Minecraft mc2 = MovementUtils.mc;
        float side = Minecraft.player.movementInput.moveStrafe;
        final Minecraft mc3 = MovementUtils.mc;
        final float prevRotationYaw = Minecraft.player.prevRotationYaw;
        final Minecraft mc4 = MovementUtils.mc;
        final float rotationYaw = Minecraft.player.rotationYaw;
        final Minecraft mc5 = MovementUtils.mc;
        float yaw = prevRotationYaw + (rotationYaw - Minecraft.player.prevRotationYaw) * MovementUtils.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static void setSpeed(final double speed) {
        final Minecraft mc = MovementUtils.mc;
        float f = Minecraft.player.movementInput.moveForward;
        final Minecraft mc2 = MovementUtils.mc;
        float f2 = Minecraft.player.movementInput.moveStrafe;
        final Minecraft mc3 = MovementUtils.mc;
        float f3 = Minecraft.player.rotationYaw;
        if (f == 0.0f && f2 == 0.0f) {
            final Minecraft mc4 = MovementUtils.mc;
            Minecraft.player.motionX = 0.0;
            final Minecraft mc5 = MovementUtils.mc;
            Minecraft.player.motionZ = 0.0;
        }
        else if (f != 0.0f) {
            if (f2 >= 1.0f) {
                f3 += ((f > 0.0f) ? -35 : 35);
                f2 = 0.0f;
            }
            else if (f2 <= -1.0f) {
                f3 += ((f > 0.0f) ? 35 : -35);
                f2 = 0.0f;
            }
            if (f > 0.0f) {
                f = 1.0f;
            }
            else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        final double d0 = Math.cos(Math.toRadians(f3 + 90.0f));
        final double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        final Minecraft mc6 = MovementUtils.mc;
        Minecraft.player.motionX = f * speed * d0 + f2 * speed * d2;
        final Minecraft mc7 = MovementUtils.mc;
        Minecraft.player.motionZ = f * speed * d2 - f2 * speed * d0;
    }
    
    public static float getMoveDirection() {
        final Minecraft mc = MovementUtils.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = MovementUtils.mc;
        final double motionZ = Minecraft.player.motionZ;
        final float direction = (float)(Math.atan2(motionX, motionZ) / 3.141592653589793 * 180.0);
        return -direction;
    }
    
    public static boolean airBlockAboveHead() {
        final Minecraft mc = MovementUtils.mc;
        final double x1 = Minecraft.player.posX - 0.3;
        final Minecraft mc2 = MovementUtils.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc3 = MovementUtils.mc;
        final double y1 = posY + Minecraft.player.getEyeHeight();
        final Minecraft mc4 = MovementUtils.mc;
        final double z1 = Minecraft.player.posZ + 0.3;
        final Minecraft mc5 = MovementUtils.mc;
        final double x2 = Minecraft.player.posX + 0.3;
        final Minecraft mc6 = MovementUtils.mc;
        final double posY2 = Minecraft.player.posY;
        final Minecraft mc7 = MovementUtils.mc;
        final double y2 = posY2 + (Minecraft.player.onGround ? 2.5 : 1.5);
        final Minecraft mc8 = MovementUtils.mc;
        final AxisAlignedBB bb = new AxisAlignedBB(x1, y1, z1, x2, y2, Minecraft.player.posZ - 0.3);
        final WorldClient world = MovementUtils.mc.world;
        final Minecraft mc9 = MovementUtils.mc;
        return world.getCollisionBoxes(Minecraft.player, bb).isEmpty();
    }
    
    public static void setMotion(final EventPreMove e, final double speed, final float pseudoYaw, final double aa, final double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + ((po4 > 0.0) ? -45 : 45);
            }
            else if (aa < 0.0) {
                yaw = pseudoYaw + ((po4 > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            }
            else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        final double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }
    
    public static void setEventSpeed(final EventPreMove event, final double speed) {
        final Minecraft mc = MovementUtils.mc;
        double forward = Minecraft.player.movementInput.moveForward;
        final Minecraft mc2 = MovementUtils.mc;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        final Minecraft mc3 = MovementUtils.mc;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
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
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static boolean isBlockAboveHead() {
        final Minecraft mc = MovementUtils.mc;
        final double x1 = Minecraft.player.posX - 0.3;
        final Minecraft mc2 = MovementUtils.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc3 = MovementUtils.mc;
        final double y1 = posY + Minecraft.player.getEyeHeight();
        final Minecraft mc4 = MovementUtils.mc;
        final double z1 = Minecraft.player.posZ + 0.3;
        final Minecraft mc5 = MovementUtils.mc;
        final double x2 = Minecraft.player.posX + 0.3;
        final Minecraft mc6 = MovementUtils.mc;
        final double posY2 = Minecraft.player.posY;
        final Minecraft mc7 = MovementUtils.mc;
        final double y2 = posY2 + (Minecraft.player.onGround ? 2.5 : 1.5);
        final Minecraft mc8 = MovementUtils.mc;
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x1, y1, z1, x2, y2, Minecraft.player.posZ - 0.3);
        final WorldClient world = MovementUtils.mc.world;
        final Minecraft mc9 = MovementUtils.mc;
        return world.getCollisionBoxes(Minecraft.player, axisAlignedBB).isEmpty();
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static float getSpeed() {
        final Minecraft mc = MovementUtils.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = MovementUtils.mc;
        final double n = motionX * Minecraft.player.motionX;
        final Minecraft mc3 = MovementUtils.mc;
        final double motionZ = Minecraft.player.motionZ;
        final Minecraft mc4 = MovementUtils.mc;
        return (float)Math.sqrt(n + motionZ * Minecraft.player.motionZ);
    }
    
    public static float getAllDirection() {
        final Minecraft mc = MovementUtils.mc;
        float rotationYaw = Minecraft.player.rotationYaw;
        float factor = 0.0f;
        final Minecraft mc2 = MovementUtils.mc;
        if (Minecraft.player.movementInput.moveForward > 0.0f) {
            factor = 1.0f;
        }
        final Minecraft mc3 = MovementUtils.mc;
        if (Minecraft.player.movementInput.moveForward < 0.0f) {
            factor = -1.0f;
        }
        if (factor == 0.0f) {
            final Minecraft mc4 = MovementUtils.mc;
            if (Minecraft.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 90.0f;
            }
            final Minecraft mc5 = MovementUtils.mc;
            if (Minecraft.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 90.0f;
            }
        }
        else {
            final Minecraft mc6 = MovementUtils.mc;
            if (Minecraft.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 45.0f * factor;
            }
            final Minecraft mc7 = MovementUtils.mc;
            if (Minecraft.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 45.0f * factor;
            }
        }
        if (factor < 0.0f) {
            rotationYaw -= 180.0f;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static double getPlayerMotion() {
        final Minecraft mc = MovementUtils.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = MovementUtils.mc;
        return Math.hypot(motionX, Minecraft.player.motionZ);
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getAllDirection();
        final Minecraft mc = MovementUtils.mc;
        Minecraft.player.motionX = -Math.sin(yaw) * speed;
        final Minecraft mc2 = MovementUtils.mc;
        Minecraft.player.motionZ = Math.cos(yaw) * speed;
    }
    
    public static boolean isMoving() {
        final Minecraft mc = MovementUtils.mc;
        if (Minecraft.player.movementInput.moveStrafe == 0.0) {
            final Minecraft mc2 = MovementUtils.mc;
            if (Minecraft.player.movementInput.moveForward == 0.0) {
                return false;
            }
        }
        return true;
    }
}
