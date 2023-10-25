/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector2d
 */
package me.Tengoku.Terror.util;

import javax.vecmath.Vector2d;
import me.Tengoku.Terror.event.events.EventMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class MoveUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public double getDirection() {
        float f = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f2 = -0.5f;
        } else if (Minecraft.thePlayer.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }

    public void strafe() {
        this.strafe(this.getSpeed());
    }

    public void setMoveEvent(EventMotion eventMotion, double d, float f, double d2, double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;
        if (d4 != 0.0) {
            if (d5 > 0.0) {
                f2 += (float)(d4 > 0.0 ? -45 : 45);
            } else if (d5 < 0.0) {
                f2 += (float)(d4 > 0.0 ? 45 : -45);
            }
            d5 = 0.0;
            if (d4 > 0.0) {
                d4 = 1.0;
            } else if (d4 < 0.0) {
                d4 = -1.0;
            }
        }
        if (d5 > 0.0) {
            d5 = 1.0;
        } else if (d5 < 0.0) {
            d5 = -1.0;
        }
        double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
        double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
        eventMotion.setX(d4 * d * d6 + d5 * d * d7);
        eventMotion.setZ(d4 * d * d7 - d5 * d * d6);
    }

    public double getPosForSetPosX(double d) {
        Minecraft.getMinecraft();
        double d2 = Math.toRadians(Minecraft.thePlayer.rotationYaw);
        double d3 = -Math.sin(d2) * d;
        return d3;
    }

    public void setMoveEventSpeed(EventMotion eventMotion, double d) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        this.setMoveEvent(eventMotion, d, Minecraft.thePlayer.rotationYaw, MovementInput.moveStrafe, MovementInput.moveForward);
    }

    public double getDirectionWrappedTo90() {
        float f = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f && Minecraft.thePlayer.moveStrafing == 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            f -= 90.0f;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            f += 90.0f;
        }
        return Math.toRadians(f);
    }

    public static double getBaseMoveSpeed() {
        double d = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            d *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return d;
    }

    public double getPosForSetPosZ(double d) {
        Minecraft.getMinecraft();
        double d2 = Math.toRadians(Minecraft.thePlayer.rotationYaw);
        double d3 = Math.cos(d2) * d;
        return d3;
    }

    public double getPredictedMotionY(double d) {
        return (d - 0.08) * (double)0.98f;
    }

    public void strafe(double d) {
        if (!this.isMoving()) {
            return;
        }
        double d2 = this.getDirection();
        Minecraft.thePlayer.motionX = (double)(-MathHelper.sin((float)d2)) * d;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos((float)d2) * d;
    }

    public double getJumpMotion(float f) {
        Potion potion = Potion.jump;
        if (Minecraft.thePlayer.isPotionActive(potion)) {
            int n = Minecraft.thePlayer.getActivePotionEffect(potion).getAmplifier();
            f += (float)(n + 1) * 0.1f;
        }
        return f;
    }

    public void forward(double d) {
        double d2 = this.getDirection();
        Minecraft.thePlayer.motionX = -Math.sin(d2) * d;
        Minecraft.thePlayer.motionZ = Math.cos(d2) * d;
    }

    public double getBaseMoveSpeedOther() {
        double d = 0.2875;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            d *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return d;
    }

    public void strafe(double d, float f) {
        if (!this.isMoving()) {
            return;
        }
        if (Minecraft.thePlayer != null) {
            f = Minecraft.thePlayer.rotationYaw;
        }
        Minecraft.thePlayer.motionX = (double)(-MathHelper.sin(f)) * d;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos(f) * d;
    }

    public void sendMotion(double d, double d2) {
        Vector2d vector2d = this.getMotion(d2);
        double d3 = d2;
        while (d3 < d) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
            d3 += d2;
        }
    }

    public void stop() {
        Minecraft.thePlayer.motionZ = 0.0;
        Minecraft.thePlayer.motionX = 0.0;
    }

    public Vector2d getMotion(double d) {
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        double d2 = MovementInput.moveForward;
        double d3 = MovementInput.moveStrafe;
        double d4 = Minecraft.thePlayer.rotationYaw;
        if (d2 != 0.0 || d3 != 0.0) {
            if (d3 > 0.0) {
                d3 = 1.0;
            } else if (d3 < 0.0) {
                d3 = -1.0;
            }
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    d4 += (double)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    d4 += (double)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            double d5 = Math.cos(Math.toRadians((d4 *= 0.995) + 90.0));
            double d6 = Math.sin(Math.toRadians(d4 + 90.0));
            return new Vector2d(d2 * d * d5 + d3 * d * d6, d2 * d * d6 - d3 * d * d5);
        }
        return new Vector2d(0.0, 0.0);
    }

    public boolean isMoving() {
        block2: {
            block3: {
                if (Minecraft.thePlayer == null) break block2;
                MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
                if (MovementInput.moveForward != 0.0f) break block3;
                MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
                if (MovementInput.moveStrafe == 0.0f) break block2;
            }
            return true;
        }
        return false;
    }

    public double getDirection(float f) {
        float f2 = f;
        if (Minecraft.thePlayer != null) {
            f2 = Minecraft.thePlayer.rotationYaw;
        }
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f2 += 180.0f;
        }
        float f3 = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f3 = -0.5f;
        } else if (Minecraft.thePlayer.moveForward > 0.0f) {
            f3 = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            f2 -= 90.0f * f3;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            f2 += 90.0f * f3;
        }
        return Math.toRadians(f2);
    }

    public double moveSpeed() {
        if (Minecraft.gameSettings.keyBindSprint.isKeyDown()) {
            if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                    return 0.18386012061481244;
                }
                return 0.21450346015841276;
            }
            return 0.15321676228437875;
        }
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                return 0.14143085686761;
            }
            return 0.16500264553372018;
        }
        return 0.11785905094607611;
    }

    public boolean isOnGround(double d) {
        return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -d, 0.0)).isEmpty();
    }

    public static void setMotion(double d) {
        float f = Minecraft.thePlayer.rotationYaw;
        double d2 = Minecraft.thePlayer.moveForward;
        double d3 = Minecraft.thePlayer.moveStrafing;
        if (d2 == 0.0 && d3 == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
            Minecraft.thePlayer.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
        }
    }

    public double getSpeed() {
        return Math.hypot(Minecraft.thePlayer.motionX, Minecraft.thePlayer.motionZ);
    }

    public void strafe(float f) {
        if (!this.isMoving()) {
            return;
        }
        double d = this.getDirection();
        Minecraft.thePlayer.motionX = -MathHelper.sin((float)d) * f;
        Minecraft.thePlayer.motionZ = MathHelper.cos((float)d) * f;
    }
}

