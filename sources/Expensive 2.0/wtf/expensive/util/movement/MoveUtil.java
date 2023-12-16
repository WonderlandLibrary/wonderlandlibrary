package wtf.expensive.util.movement;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventMove;
import wtf.expensive.util.FreeCamera;
import wtf.expensive.util.IMinecraft;

import static net.minecraft.util.math.MathHelper.atan2;

public class MoveUtil implements IMinecraft {

    public static boolean isMoving() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.movementInput.moveForward != 0.0;
    }
    public static float adjustFloatValue(float value) {
        int ticksExisted = mc.player.ticksExisted;
        return ticksExisted % 2 == 0 ? value - 1.0E-4f : value;
    }
    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double direction() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.player.moveForward < 0) {
            forward = -0.5F;
        } else if (mc.player.moveForward > 0) {
            forward = 0.5F;
        }

        if (mc.player.moveStrafing > 0) {
            rotationYaw -= 90 * forward;
        }

        if (mc.player.moveStrafing < 0) {
            rotationYaw += 90 * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public static void fixMovement(final EventInput event, float yaw) {
        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(MoveUtil.direction(mc.player.isElytraFlying() ? yaw : mc.player.rotationYaw, forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(MoveUtil.direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float strafeFactor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            strafeFactor = 1;
        if (mc.player.movementInput.moveForward < 0)
            strafeFactor = -1;

        if (strafeFactor == 0) {
            rotationYaw = mc.player.movementInput.moveStrafe > 0 ? -90 : 90;
        } else {
            rotationYaw = mc.player.movementInput.moveStrafe > 0 ? -(45 * strafeFactor) : 45 * strafeFactor;
        }

        if (strafeFactor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }

    public static float getMotion() {
        return (float) Math.sqrt(mc.player.motion.x * mc.player.motion.x + mc.player.motion.z * mc.player.motion.z);
    }

    public static void setMotion(final double motion) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            mc.player.motion.x = 0;
            mc.player.motion.z = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            mc.player.motion.x = forward * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f));
            mc.player.motion.z = forward * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void setMotion(final double motion, FreeCamera player) {
        double forward = player.movementInput.moveForward;
        double strafe = player.movementInput.moveStrafe;
        float yaw = player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            player.motion.x = 0;
            player.motion.z = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            player.motion.x = forward * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f));
            player.motion.z = forward * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static class MoveEvent {
        public static void setMoveMotion(final EventMove move, final double motion) {
            double forward = mc.player.movementInput.moveForward;
            double strafe = mc.player.movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if (forward == 0 && strafe == 0) {
                move.motion().x = 0;
                move.motion().z = 0;
            } else {
                if (forward != 0) {
                    if (strafe > 0) {
                        yaw += (float) (forward > 0 ? -45 : 45);
                    } else if (strafe < 0) {
                        yaw += (float) (forward > 0 ? 45 : -45);
                    }
                    strafe = 0;
                    if (forward > 0) {
                        forward = 1;
                    } else if (forward < 0) {
                        forward = -1;
                    }
                }
                move.motion().x = forward * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f))
                        + strafe * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f));
                move.motion().z = forward * motion * MathHelper.sin(Math.toRadians(yaw + 90.0f))
                        - strafe * motion * MathHelper.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }

    public static class StrafeMovement {
        public static double oldSpeed, contextFriction;
        public static boolean needSwap;
        public static boolean needSprintState;
        public static int counter, noSlowTicks;

        public static double    calculateSpeed(final EventMove move, boolean damageBoost, boolean hasTime, float damageSpeed) {

            final boolean fromGround = mc.player.isOnGround();
            final boolean toGround = move.toGround();
            final boolean jump = move.motion().y > 0;
            final float speedAttributes = getAIMoveSpeed(mc.player);
            final float frictionFactor = getFrictionFactor(mc.player, move);
            float n6 = mc.player.isPotionActive(Effects.JUMP_BOOST) && mc.player.isHandActive() ? 0.88f : 0.91F;

            if (fromGround) {
                n6 = frictionFactor;
            }
            final float n7 = 0.16277136f / (n6 * n6 * n6);
            float n8;
            if (fromGround) {
                n8 = speedAttributes * n7;
                if (jump) {
                    n8 += 0.2f;
                }
            } else {
                n8 = (damageBoost && hasTime && mc.gameSettings.keyBindJump.isKeyDown() ? damageSpeed : 0.0255f);
            }
            boolean noslow = false;
            double max2 = oldSpeed + n8;
            double max = 0.0;
            if (mc.player.isHandActive() && !jump) {
                double n10 = oldSpeed + n8 * 0.25;
                double motionY2 = move.motion().y;
                if (motionY2 != 0.0 && Math.abs(motionY2) < 0.08) {
                    n10 += 0.055;
                }
                if (max2 > (max = Math.max(0.043, n10))) {
                    noslow = true;
                    ++noSlowTicks;
                } else {
                    noSlowTicks = Math.max(noSlowTicks - 1, 0);
                }
            } else {
                noSlowTicks = 0;
            }
            if (noSlowTicks > 3) {
                max2 = max - (mc.player.isPotionActive(Effects.JUMP_BOOST) && mc.player.isHandActive() ? 0.3 : 0.019);
            } else {
                max2 = Math.max(noslow ? 0 : 0.25, max2) - (counter++ % 2 == 0 ? 0.001 : 0.002);
            }
            contextFriction = n6;
            if (!toGround && !fromGround) {
                needSwap = true;
            }
            if (!fromGround && !toGround) {
                needSprintState = !mc.player.serverSprintState;
            }
            if (toGround && fromGround) {
                needSprintState = false;
            }
            return max2;
        }

        public static void postMove(final double horizontal) {
            oldSpeed = horizontal * contextFriction;
        }

        public static float getAIMoveSpeed(final ClientPlayerEntity contextPlayer) {
            boolean prevSprinting = contextPlayer.isSprinting();
            contextPlayer.setSprinting(false);
            float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
            contextPlayer.setSprinting(prevSprinting);
            return speed;
        }


        private static float getFrictionFactor(final ClientPlayerEntity contextPlayer, final EventMove move) {
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
            blockpos$mutable.setPos(move.from().x, move.getAABBFrom().minY - 1.0D, move.from().z);

            return contextPlayer.world.getBlockState(blockpos$mutable).getBlock().slipperiness * 0.91F;
        }
    }
}
