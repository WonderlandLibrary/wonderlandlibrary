/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.movement;

import java.util.List;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.module.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class MovementUtil {
    public static final double WALK_SPEED = 0.221;
    public static Minecraft mc = Minecraft.getMinecraft();

    public static int getJumpBoostModifier() {
        PotionEffect effect = Minecraft.player.getActivePotionEffect(MobEffects.JUMP_BOOST);
        if (effect != null) {
            return effect.getAmplifier() + 1;
        }
        return 0;
    }

    public static float getMovementDirection() {
        float forward = MovementInput.moveForward;
        float strafe = MovementInput.moveStrafe;
        float direction = 0.0f;
        if (forward < 0.0f) {
            direction += 180.0f;
            if (strafe > 0.0f) {
                direction += 45.0f;
            } else if (strafe < 0.0f) {
                direction -= 45.0f;
            }
        } else if (forward > 0.0f) {
            if (strafe > 0.0f) {
                direction -= 45.0f;
            } else if (strafe < 0.0f) {
                direction += 45.0f;
            }
        } else if (strafe > 0.0f) {
            direction -= 90.0f;
        } else if (strafe < 0.0f) {
            direction += 90.0f;
        }
        return MathHelper.wrapAngleTo180_float(direction += Minecraft.player.rotationYaw);
    }

    public static boolean isBlockAbove() {
        for (double height = 0.0; height <= 1.0; height += 0.5) {
            List<AxisAlignedBB> collidingList = MovementUtil.mc.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0.0, height, 0.0));
            if (collidingList.isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static float getDirection() {
        Minecraft mc = Minecraft.getMinecraft();
        float var1 = Minecraft.player.rotationYaw;
        if (Minecraft.player.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.player.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Minecraft.player.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= (float)Math.PI / 180;
    }

    public static double getXDirAt(float angle) {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = 90.0;
        return Math.cos((rot += angle) * Math.PI / 180.0);
    }

    public static double getZDirAt(float angle) {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = 90.0;
        return Math.sin((rot += angle) * Math.PI / 180.0);
    }

    public static void setSpeedAt(MoveEvent e, float angle, double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            if (Minecraft.player.onGround) {
                if (!(Minecraft.player.getDistanceToEntity(KillAura.target) <= 1.0f)) {
                    e.setX(MovementUtil.getXDirAt(angle) * speed);
                    e.setZ(MovementUtil.getZDirAt(angle) * speed);
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isOnGround() {
        if (!Minecraft.player.onGround) return false;
        return Minecraft.player.isCollidedVertically;
    }

    public static void setMotion(MoveEvent e, double speed, float pseudoYaw, double aa, double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? -45 : 45);
            } else if (aa < 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            } else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }

    public static void setSpeed(double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.player.motionX = 0.0;
            Minecraft.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Minecraft.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Minecraft.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void strafe() {
        if (MovementUtil.mc.gameSettings.keyBindBack.isKeyDown()) {
            return;
        }
        MovementUtil.strafe(MovementUtil.getSpeed());
    }

    public static float getSpeed() {
        return (float)Math.sqrt(Minecraft.player.motionX * Minecraft.player.motionX + Minecraft.player.motionZ * Minecraft.player.motionZ);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isMoving() {
        if (Minecraft.player == null) return false;
        if (MovementInput.moveForward != 0.0f) return true;
        return MovementInput.moveStrafe != 0.0f;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean hasMotion() {
        if (Minecraft.player.motionX == 0.0) return false;
        if (Minecraft.player.motionZ == 0.0) return false;
        return Minecraft.player.motionY != 0.0;
    }

    public static void strafe(float speed) {
        if (!MovementUtil.isMoving()) {
            return;
        }
        double yaw = MovementUtil.getDirection();
        Minecraft.player.motionX = -Math.sin(yaw) * (double)speed;
        Minecraft.player.motionZ = Math.cos(yaw) * (double)speed;
    }

    public static double getMoveSpeed(MoveEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        double xspeed = e.getX();
        double zspeed = e.getZ();
        return Math.sqrt(xspeed * xspeed + zspeed * zspeed);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean moveKeysDown() {
        Minecraft mc = Minecraft.getMinecraft();
        if (MovementInput.moveForward != 0.0f) return true;
        return MovementInput.moveStrafe != 0.0f;
    }

    public static double getPressedMoveDir() {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = Math.atan2(Minecraft.player.moveForward, Minecraft.player.moveStrafing) / Math.PI * 180.0;
        if (rot == 0.0) {
            if (Minecraft.player.moveStrafing == 0.0f) {
                rot = 90.0;
            }
        }
        return (rot += Minecraft.player.rotationYaw) - 90.0;
    }

    public static double getPlayerMoveDir() {
        Minecraft mc = Minecraft.getMinecraft();
        double xspeed = Minecraft.player.motionX;
        double zspeed = Minecraft.player.motionZ;
        double direction = Math.atan2(xspeed, zspeed) / Math.PI * 180.0;
        return -direction;
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(Minecraft.player.posX - 0.3, Minecraft.player.posY + (double)Minecraft.player.getEyeHeight(), Minecraft.player.posZ + 0.3, Minecraft.player.posX + 0.3, Minecraft.player.posY + 2.5, Minecraft.player.posZ - 0.3);
        return !MovementUtil.mc.world.getCollisionBoxes(Minecraft.player, bb).isEmpty();
    }

    public static void setMotionEvent(MoveEvent event, double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static void startFakePos() {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + 0.3, Minecraft.player.posZ);
        Minecraft.getMinecraft();
        double x = Minecraft.player.posX;
        Minecraft.getMinecraft();
        double y = Minecraft.player.posY;
        Minecraft.getMinecraft();
        double z = Minecraft.player.posZ;
        for (int i = 0; i < 3000; ++i) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.09999999999999, z, false));
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, true));
        }
        Minecraft.getMinecraft();
        Minecraft.player.motionY = 0.0;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = Minecraft.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}

