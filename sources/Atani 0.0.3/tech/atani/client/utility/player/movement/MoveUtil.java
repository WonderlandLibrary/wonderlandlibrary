package tech.atani.client.utility.player.movement;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.listener.event.minecraft.player.movement.MovePlayerEvent;
import tech.atani.client.utility.player.PlayerHandler;

public class MoveUtil implements Methods {

    public static double getPredictedMotion(double motion, int ticks) {
        if (ticks == 0) return motion;
        double predicted = motion;

        for (int i = 0; i < ticks; i++) {
            predicted = (predicted - 0.08) * 0.98F;
        }

        return predicted;
    }

    public static double[] getMotion(final double speed, final float strafe, final float forward, final float yaw) {
        final float friction = (float)speed;
        final float f1 = MathHelper.sin(yaw * (float)Math.PI / 180.0f);
        final float f2 = MathHelper.cos(yaw * (float)Math.PI / 180.0f);
        final double motionX = strafe * friction * f2 - forward * friction * f1;
        final double motionZ = forward * friction * f2 + strafe * friction * f1;
        return new double[] { motionX, motionZ };
    }

    public static float getSpeedBoost(float times) {
        float boost = (float) ((MoveUtil.getBaseMoveSpeed() - 0.2875F) * times);
        if(0 > boost) {
            boost = 0;
        }

        return boost;
    }

    public static double getSpeed() {
        return mc.thePlayer == null ? 0 : Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX
                + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static void strafe(MovePlayerEvent event) {
        strafe(event, getSpeed());
    }

    public static void strafe(double movementSpeed) {
        strafe(null, movementSpeed);
    }

    public static void strafe(MovePlayerEvent moveEvent, double movementSpeed) {
        if (mc.thePlayer.movementInput.moveForward > 0.0) {
            mc.thePlayer.movementInput.moveForward = (float) 1.0;
        } else if (mc.thePlayer.movementInput.moveForward < 0.0) {
            mc.thePlayer.movementInput.moveForward = (float) -1.0;
        }

        if (mc.thePlayer.movementInput.moveStrafe > 0.0) {
            mc.thePlayer.movementInput.moveStrafe = (float) 1.0;
        } else if (mc.thePlayer.movementInput.moveStrafe < 0.0) {
            mc.thePlayer.movementInput.moveStrafe = (float) -1.0;
        }

        if (mc.thePlayer.movementInput.moveForward == 0.0 && mc.thePlayer.movementInput.moveStrafe == 0.0) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }

        if (mc.thePlayer.movementInput.moveForward != 0.0 && mc.thePlayer.movementInput.moveStrafe != 0.0) {
            mc.thePlayer.movementInput.moveForward *= Math.sin(0.6398355709958845);
            mc.thePlayer.movementInput.moveStrafe *= Math.cos(0.6398355709958845);
        }

        if (moveEvent != null) {
            moveEvent.setX(mc.thePlayer.motionX = mc.thePlayer.movementInput.moveForward * movementSpeed * -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw))
                    + mc.thePlayer.movementInput.moveStrafe * movementSpeed * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)));
            moveEvent.setZ(mc.thePlayer.motionZ = mc.thePlayer.movementInput.moveForward * movementSpeed * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw))
                    - mc.thePlayer.movementInput.moveStrafe * movementSpeed * -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)));
        } else {
            mc.thePlayer.motionX = mc.thePlayer.movementInput.moveForward * movementSpeed * -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw))
                    + mc.thePlayer.movementInput.moveStrafe * movementSpeed * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            mc.thePlayer.motionZ = mc.thePlayer.movementInput.moveForward * movementSpeed * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw))
                    - mc.thePlayer.movementInput.moveStrafe * movementSpeed * -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
        }
    }

    public static void setMoveSpeed(final MovePlayerEvent event, final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = PlayerHandler.moveFix && PlayerHandler.currentMode == PlayerHandler.MoveFixMode.AGGRESSIVE ? PlayerHandler.yaw : mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            if(event == null) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            } else {
                event.setX(0.0);
                event.setZ(0.0);
            }
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double X = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            double Z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            if(event == null) {
                mc.thePlayer.motionX = X;
                mc.thePlayer.motionZ = Z;
            } else {
                event.setX(X);
                event.setZ(Z);
            }
        }
    }

    public static void setMoveSpeed(double moveSpeed, float yaw, double strafe, double forward) {
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
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setMoveSpeed(double moveSpeed) {
        setMoveSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + (0.2 * amplifier);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        return getDirection(mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw);
    }

    public static float getDirection(float forward, float strafing, float yaw) {
        if (forward == 0.0 && strafing == 0.0) return yaw;
        boolean reversed = (forward < 0.0);
        float strafingYaw = 90f * ((forward > 0) ? 0.5f : (reversed ? -0.5f : 1));
        if (reversed) yaw += 180;
        if (strafing > 0) {
            yaw -= strafingYaw;
        } else if (strafing < 0) {
            yaw += strafingYaw;
        }
        return yaw;
    }

}
