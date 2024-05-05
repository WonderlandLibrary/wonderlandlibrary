// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.movement;

import ru.fluger.client.event.events.impl.player.EventMove;
import java.util.Objects;
import ru.fluger.client.helpers.Helper;

public class MovementHelper implements Helper
{
    public static boolean isMoving() {
        return MovementHelper.mc.h.e.moveForward != 0.0f || MovementHelper.mc.h.e.a != 0.0f;
    }
    
    public static int getSpeedEffect() {
        if (MovementHelper.mc.h.a(vb.a)) {
            return Objects.requireNonNull(MovementHelper.mc.h.b(vb.a)).c() + 1;
        }
        return 0;
    }
    
    public static boolean isInsideBlock2() {
        for (int i = rk.c(MovementHelper.mc.h.av.a); i < rk.c(MovementHelper.mc.h.av.d) + 1; ++i) {
            for (int j = rk.c(MovementHelper.mc.h.av.b + 1.0); j < rk.c(MovementHelper.mc.h.av.e) + 2; ++j) {
                for (int k = rk.c(MovementHelper.mc.h.av.c); k < rk.c(MovementHelper.mc.h.av.f) + 1; ++k) {
                    final aow block = MovementHelper.mc.f.o(new et(i, j, k)).u();
                    if (block != null) {
                        if (!(block instanceof aom)) {
                            bhb axisAlignedBB = block.b(MovementHelper.mc.f.o(new et(i, j, k)), MovementHelper.mc.f, new et(i, j, k));
                            if (block instanceof arl) {
                                axisAlignedBB = new bhb(i, j, k, i + 1, j + 1, k + 1);
                            }
                            if (axisAlignedBB != null) {
                                if (MovementHelper.mc.h.av.c(axisAlignedBB)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static double[] getSpeed(final double speed) {
        float moveForward = MovementHelper.mc.h.e.moveForward;
        float moveStrafe = MovementHelper.mc.h.e.a;
        float rotationYaw = MovementHelper.mc.h.x + (MovementHelper.mc.h.v - MovementHelper.mc.h.x) * MovementHelper.mc.aj();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        final double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[] { posX, posZ };
    }
    
    public static void teleport(final lk player, final double dist) {
        double forward = MovementHelper.mc.h.e.moveForward;
        double strafe = MovementHelper.mc.h.e.a;
        float yaw = MovementHelper.mc.h.v;
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
        final double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0f));
        final double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0f));
        player.a = xspeed;
        player.c = zspeed;
    }
    
    public static void teleport(final double dist) {
        double forward = MovementHelper.mc.h.e.moveForward;
        double strafe = MovementHelper.mc.h.e.a;
        float yaw = MovementHelper.mc.h.v;
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
        final double x = MovementHelper.mc.h.p;
        final double y = MovementHelper.mc.h.q;
        final double z = MovementHelper.mc.h.r;
        final double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0f));
        final double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0f));
        MovementHelper.mc.h.d.a(new lk.a(x + xspeed, y, z + zspeed, true));
        MovementHelper.mc.h.d.a(new lk.a(x + xspeed, y + 0.1, z + zspeed, true));
        MovementHelper.mc.h.d.a(new lk.a(x + xspeed, y, z + zspeed, true));
        MovementHelper.mc.h.d.a(new lk.a(x + xspeed * 2.0, y, z + zspeed * 2.0, true));
    }
    
    public static float getPlayerDirection() {
        float rotationYaw = MovementHelper.mc.h.v;
        if (MovementHelper.mc.h.bf < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementHelper.mc.h.bf < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementHelper.mc.h.bf > 0.0f) {
            forward = 0.5f;
        }
        if (MovementHelper.mc.h.be > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementHelper.mc.h.be < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static float getEntityDirection(final vp entity) {
        float rotationYaw = entity.v;
        if (entity.bf < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (entity.bf < 0.0f) {
            forward = -0.5f;
        }
        else if (entity.bf > 0.0f) {
            forward = 0.5f;
        }
        if (entity.be > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (entity.be < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static boolean airBlockAbove2() {
        return !MovementHelper.mc.f.c(MovementHelper.mc.h.bw().b(0.0, MovementHelper.mc.h.z ? 0.3 : 0.0, 0.0));
    }
    
    public static boolean isUnderBedrock() {
        if (MovementHelper.mc.h.q <= 3.0) {
            final bhc trace = MovementHelper.mc.f.a(MovementHelper.mc.h.d(), new bhe(MovementHelper.mc.h.p, 0.0, MovementHelper.mc.h.r), false, false, false);
            return trace == null || trace.a != bhc.a.b;
        }
        return false;
    }
    
    public static boolean isBlockUnder(final float value) {
        if (MovementHelper.mc.h.q < 0.0) {
            return false;
        }
        final bhb bb = MovementHelper.mc.h.bw().d(0.0, -value, 0.0);
        return MovementHelper.mc.f.a(MovementHelper.mc.h, bb).isEmpty();
    }
    
    public static float getMoveDirection() {
        final double motionX = MovementHelper.mc.h.s;
        final double motionZ = MovementHelper.mc.h.u;
        final float direction = (float)(Math.atan2(motionX, motionZ) / 3.141592653589793 * 180.0);
        return -direction;
    }
    
    public static boolean airBlockAboveHead() {
        final bhb bb = new bhb(MovementHelper.mc.h.p - 0.3, MovementHelper.mc.h.q + MovementHelper.mc.h.by(), MovementHelper.mc.h.r + 0.3, MovementHelper.mc.h.p + 0.3, MovementHelper.mc.h.q + (MovementHelper.mc.h.z ? 2.5 : 1.5), MovementHelper.mc.h.r - 0.3);
        return MovementHelper.mc.f.a(MovementHelper.mc.h, bb).isEmpty();
    }
    
    public static void setEventSpeed(final EventMove event, final double speed) {
        double forward = MovementHelper.mc.h.e.moveForward;
        double strafe = MovementHelper.mc.h.e.a;
        float yaw = MovementHelper.mc.h.v;
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
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementHelper.mc.h.s * MovementHelper.mc.h.s + MovementHelper.mc.h.u * MovementHelper.mc.h.u);
    }
    
    public static void setMotion(final double speed) {
        double forward = MovementHelper.mc.h.e.moveForward;
        double strafe = MovementHelper.mc.h.e.a;
        float yaw = MovementHelper.mc.h.v;
        if (forward == 0.0 && strafe == 0.0) {
            MovementHelper.mc.h.s = 0.0;
            MovementHelper.mc.h.u = 0.0;
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
            final double sin = rk.a((float)Math.toRadians(yaw + 90.0f));
            final double cos = rk.b((float)Math.toRadians(yaw + 90.0f));
            MovementHelper.mc.h.s = forward * speed * cos + strafe * speed * sin;
            MovementHelper.mc.h.u = forward * speed * sin - strafe * speed * cos;
        }
    }
    
    public static void setSpeed(final float speed) {
        float yaw = MovementHelper.mc.h.v;
        float forward = MovementHelper.mc.h.e.moveForward;
        float strafe = MovementHelper.mc.h.e.a;
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (strafe < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        MovementHelper.mc.h.s = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
        MovementHelper.mc.h.u = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
    }
    
    public static float getAllDirection() {
        float rotationYaw = MovementHelper.mc.h.v;
        float factor = 0.0f;
        if (MovementHelper.mc.h.e.moveForward > 0.0f) {
            factor = 1.0f;
        }
        if (MovementHelper.mc.h.e.moveForward < 0.0f) {
            factor = -1.0f;
        }
        if (factor == 0.0f) {
            if (MovementHelper.mc.h.e.a > 0.0f) {
                rotationYaw -= 90.0f;
            }
            if (MovementHelper.mc.h.e.a < 0.0f) {
                rotationYaw += 90.0f;
            }
        }
        else {
            if (MovementHelper.mc.h.e.a > 0.0f) {
                rotationYaw -= 45.0f * factor;
            }
            if (MovementHelper.mc.h.e.a < 0.0f) {
                rotationYaw += 45.0f * factor;
            }
        }
        if (factor < 0.0f) {
            rotationYaw -= 180.0f;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static double getDirectionAll() {
        float rotationYaw = MovementHelper.mc.h.v;
        float forward = 1.0f;
        if (MovementHelper.mc.h.bf < 0.0f) {
            rotationYaw += 180.0f;
        }
        if (MovementHelper.mc.h.bf < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementHelper.mc.h.bf > 0.0f) {
            forward = 0.5f;
        }
        if (MovementHelper.mc.h.be > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementHelper.mc.h.be < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void strafe2() {
        if (MovementHelper.mc.t.V.e()) {
            return;
        }
        strafe2(getSpeed());
    }
    
    public static void strafePlayer() {
        final double yaw = getDirectionAll();
        final float speed = getSpeed();
        MovementHelper.mc.h.s = -Math.sin(yaw) * speed;
        MovementHelper.mc.h.u = Math.cos(yaw) * speed;
    }
    
    public static double[] forward(final double speed) {
        float forward = MovementHelper.mc.h.e.moveForward;
        float side = MovementHelper.mc.h.e.a;
        float yaw = MovementHelper.mc.h.x + (MovementHelper.mc.h.v - MovementHelper.mc.h.x) * MovementHelper.mc.aj();
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
    
    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (MovementHelper.mc.h.a(uz.a(1))) {
            final int amplifier = MovementHelper.mc.h.b(uz.a(1)).c();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getPlayerDirection();
        MovementHelper.mc.h.s = -Math.sin(yaw) * speed;
        MovementHelper.mc.h.u = Math.cos(yaw) * speed;
    }
    
    public static void strafe2(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getAllDirection();
        MovementHelper.mc.h.s = -Math.sin(yaw) * speed;
        MovementHelper.mc.h.u = Math.cos(yaw) * speed;
    }
}
