// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import java.util.List;
import exhibition.event.impl.EventMotion;
import net.minecraft.entity.Entity;
import exhibition.event.impl.EventMove;
import exhibition.event.Event;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Speed extends Module
{
    private static final String LATEST = "LATEST";
    private double moveSpeed;
    private double lastDist;
    public static int stage;
    
    public Speed(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Boolean>>)this.settings).put("LATEST", new Setting<Boolean>("LATEST", true, "Latest NCP?"));
    }
    
    @Override
    public void onEnable() {
        if (Speed.mc.thePlayer != null) {
            this.moveSpeed = getBaseMoveSpeed();
            this.lastDist = 0.0;
            Speed.stage = 2;
        }
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @RegisterEvent(events = { EventMove.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMove) {
            final EventMove em = (EventMove)event;
            if ((Speed.mc.thePlayer.onGround || Speed.stage == 3) && !((HashMap<K, Setting<Boolean>>)this.settings).get("LATEST").getValue()) {
                if ((!Speed.mc.thePlayer.isCollidedHorizontally && Speed.mc.thePlayer.moveForward != 0.0f) || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                    if (Speed.stage == 2) {
                        this.moveSpeed *= 2.149;
                        Speed.stage = 3;
                    }
                    else if (Speed.stage == 3) {
                        Speed.stage = 2;
                        final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    }
                    else {
                        final List collidingList = Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0));
                        if (collidingList.size() > 0 || Speed.mc.thePlayer.isCollidedVertically) {
                            Speed.stage = 1;
                        }
                    }
                }
                else {
                    Speed.mc.timer.timerSpeed = 1.0f;
                }
                this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
                double forward = Speed.mc.thePlayer.movementInput.moveForward;
                double strafe = Speed.mc.thePlayer.movementInput.moveStrafe;
                float yaw = Speed.mc.thePlayer.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    em.setX(0.0);
                    em.setZ(0.0);
                }
                else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            strafe = 1.0;
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
                    em.setX(forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f)));
                    em.setZ(forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f)));
                }
            }
        }
        if (event instanceof EventMotion) {
            final EventMotion em2 = (EventMotion)event;
            if (em2.isPre()) {
                Speed.mc.timer.timerSpeed = 1.085f;
                if (((HashMap<K, Setting<Boolean>>)this.settings).get("LATEST").getValue()) {
                    double forward = Speed.mc.thePlayer.movementInput.moveForward;
                    double strafe = Speed.mc.thePlayer.movementInput.moveStrafe;
                    if ((forward != 0.0 || strafe != 0.0) && !Speed.mc.thePlayer.isJumping && !Speed.mc.thePlayer.isInWater() && !Speed.mc.thePlayer.isOnLadder() && !Speed.mc.thePlayer.isCollidedHorizontally) {
                        em2.setY(Speed.mc.thePlayer.posY + ((Speed.mc.thePlayer.ticksExisted % 2 != 0) ? 0.4 : 0.0));
                    }
                    this.moveSpeed = Math.max((Speed.mc.thePlayer.ticksExisted % 2 == 0) ? 2.1 : 1.3, getBaseMoveSpeed());
                    float yaw = Speed.mc.thePlayer.rotationYaw;
                    if (forward == 0.0 && strafe == 0.0) {
                        Speed.mc.thePlayer.motionX = 0.0;
                        Speed.mc.thePlayer.motionZ = 0.0;
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
                                forward = 0.15;
                            }
                            else if (forward < 0.0) {
                                forward = -0.15;
                            }
                        }
                        if (strafe > 0.0) {
                            strafe = 0.15;
                        }
                        else if (strafe < 0.0) {
                            strafe = -0.15;
                        }
                        Speed.mc.thePlayer.motionX = forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f));
                        Speed.mc.thePlayer.motionZ = forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f));
                    }
                }
                else {
                    if (Speed.stage == 3) {
                        double gay = 0.4;
                        if (Speed.mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay = (Speed.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                        }
                        em2.setY(em2.getY() + gay);
                    }
                    final double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                    final double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                    this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                }
            }
        }
    }
}
