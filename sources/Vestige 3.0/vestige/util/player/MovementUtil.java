package vestige.util.player;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.event.impl.MoveEvent;
import vestige.module.impl.combat.Killaura;
import vestige.module.impl.combat.TargetStrafe;
import vestige.util.IMinecraft;

public class MovementUtil implements IMinecraft {

    public static void strafe() {
        strafe(getHorizontalMotion());
    }

    public static void strafe(MoveEvent event) {
        strafe(event, getHorizontalMotion());
    }

    public static void strafe(double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        if (isMoving()) {
            mc.thePlayer.motionX = -Math.sin(direction) * speed;
            mc.thePlayer.motionZ = Math.cos(direction) * speed;
        } else {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }
    }

    public static void strafe(MoveEvent event, double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        Killaura killaura = Vestige.instance.getModuleManager().getModule(Killaura.class);
        TargetStrafe targetStrafe = Vestige.instance.getModuleManager().getModule(TargetStrafe.class);

        if(killaura.isEnabled() && killaura.getTarget() != null && targetStrafe.isEnabled() && (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) || !targetStrafe.whilePressingSpace.isEnabled())) {
            direction = targetStrafe.getDirection();
        }

        if (isMoving()) {
            event.setX(mc.thePlayer.motionX = -Math.sin(direction) * speed);
            event.setZ(mc.thePlayer.motionZ = Math.cos(direction) * speed);
        } else {
            event.setX(mc.thePlayer.motionX = 0);
            event.setZ(mc.thePlayer.motionZ = 0);
        }
    }

    public static void strafeNoTargetStrafe(MoveEvent event, double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        if (isMoving()) {
            event.setX(mc.thePlayer.motionX = -Math.sin(direction) * speed);
            event.setZ(mc.thePlayer.motionZ = Math.cos(direction) * speed);
        } else {
            event.setX(mc.thePlayer.motionX = 0);
            event.setZ(mc.thePlayer.motionZ = 0);
        }
    }

    public static float getPlayerDirection() {
        float direction = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward > 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (mc.thePlayer.moveForward < 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }

    public static float getPlayerDirection(float baseYaw) {
        float direction = baseYaw;

        if (mc.thePlayer.moveForward > 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (mc.thePlayer.moveForward < 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }

    public static double getHorizontalMotion() {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    public static int getSpeedAmplifier() {
        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return 1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }

        return 0;
    }

    public static boolean isGoingDiagonally() {
        return Math.abs(mc.thePlayer.motionX) > 0.08 && Math.abs(mc.thePlayer.motionZ) > 0.08;
    }

    public static void motionMult(double mult) {
        mc.thePlayer.motionX *= mult;
        mc.thePlayer.motionZ *= mult;
    }

    public static void motionMult(MoveEvent event, double mult) {
        event.setX(mc.thePlayer.motionX *= mult);
        event.setZ(mc.thePlayer.motionZ *= mult);
    }

    public static void boost(double amount) {
        float f = getPlayerDirection() * 0.017453292F;
        mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * amount);
        mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * amount);
    }

    public static void boost(MoveEvent event, double amount) {
        float f = getPlayerDirection() * 0.017453292F;
        event.setX(mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * amount));
        event.setZ(mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * amount));
    }

    public static void jump(MoveEvent event) {
        double jumpY = (double) mc.thePlayer.getJumpUpwardsMotion();

        if(mc.thePlayer.isPotionActive(Potion.jump)) {
            jumpY += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        event.setY(mc.thePlayer.motionY = jumpY);
    }

    public static void hclip(double dist) {
        float direction = (float) Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(direction) * dist, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(direction) * dist);
    }

    public static float[] incrementMoveDirection(float forward, float strafe) {
        if(forward != 0 || strafe != 0) {
            float value = forward != 0 ? Math.abs(forward) : Math.abs(strafe);

            if(forward > 0) {
                if(strafe > 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = -value;
                } else if(strafe < 0) {
                    forward = 0;
                }
            } else if(forward == 0) {
                if(strafe > 0) {
                    forward = value;
                } else {
                    forward = -value;
                }
            } else {
                if(strafe < 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = value;
                } else if(strafe > 0) {
                    forward = 0;
                }
            }
        }

        return new float[] {forward, strafe};
    }

}