// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import java.util.List;
import exhibition.event.impl.EventMotion;
import net.minecraft.entity.Entity;
import exhibition.util.MathUtils;
import exhibition.event.impl.EventMove;
import exhibition.event.Event;
import net.minecraft.potion.Potion;
import exhibition.module.data.Setting;
import exhibition.module.data.Options;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Bhop extends Module
{
    private final String MODE = "MODE";
    private double speed;
    private double lastDist;
    public static int stage;
    
    public Bhop(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Options>>)this.settings).put("MODE", new Setting<Options>("MODE", new Options("Speed Mode", "Hypixel", new String[] { "Bhop", "Hypixel", "OnGround", "YPort", "OldHop" }), "Speed bypass method."));
    }
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Bhop.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Bhop.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @Override
    public void onEnable() {
        if (Bhop.mc.thePlayer != null) {
            this.speed = defaultSpeed();
        }
        this.lastDist = 0.0;
        Bhop.stage = 2;
        Bhop.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onDisable() {
        Bhop.mc.timer.timerSpeed = 1.0f;
    }
    
    @RegisterEvent(events = { EventMove.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final String currentMode = ((HashMap<K, Setting<Options>>)this.settings).get("MODE").getValue().getSelected();
        this.setSuffix(currentMode);
        final String s = currentMode;
        switch (s) {
            case "Hypixel": {
                if (event instanceof EventMove) {
                    final EventMove em = (EventMove)event;
                    Bhop.mc.timer.timerSpeed = 1.09f;
                    if (Bhop.mc.thePlayer.moveForward == 0.0f && Bhop.mc.thePlayer.moveStrafing == 0.0f) {
                        this.speed = defaultSpeed();
                    }
                    if (MathUtils.roundToPlace(Bhop.mc.thePlayer.posY - (int)Bhop.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                        em.setY(Bhop.mc.thePlayer.motionY = 0.31);
                    }
                    else if (MathUtils.roundToPlace(Bhop.mc.thePlayer.posY - (int)Bhop.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.71, 3)) {
                        em.setY(Bhop.mc.thePlayer.motionY = 0.04);
                    }
                    else if (MathUtils.roundToPlace(Bhop.mc.thePlayer.posY - (int)Bhop.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.75, 3)) {
                        em.setY(Bhop.mc.thePlayer.motionY = -0.2);
                    }
                    final List collidingList = Bhop.mc.theWorld.getCollidingBoundingBoxes(Bhop.mc.thePlayer, Bhop.mc.thePlayer.boundingBox.offset(0.0, -0.56, 0.0));
                    if (collidingList.size() > 0 && MathUtils.roundToPlace(Bhop.mc.thePlayer.posY - (int)Bhop.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.55, 3)) {
                        em.setY(-0.14);
                    }
                    if (Bhop.stage == 1 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        this.speed = 2.14 * defaultSpeed() - 0.01;
                    }
                    if (Bhop.stage == 2 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        em.setY(Bhop.mc.thePlayer.motionY = 0.4);
                        this.speed *= 1.5563;
                    }
                    else if (Bhop.stage == 3) {
                        final double difference = 0.66 * (this.lastDist - defaultSpeed());
                        this.speed = this.lastDist - difference;
                    }
                    else {
                        final List collidingList2 = Bhop.mc.theWorld.getCollidingBoundingBoxes(Bhop.mc.thePlayer, Bhop.mc.thePlayer.boundingBox.offset(0.0, Bhop.mc.thePlayer.motionY, 0.0));
                        if ((collidingList2.size() > 0 || Bhop.mc.thePlayer.isCollidedVertically) && Bhop.stage > 0) {
                            Bhop.stage = ((Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        this.speed = this.lastDist - this.lastDist / 159.0;
                    }
                    this.speed = Math.max(this.speed, defaultSpeed());
                    if (Bhop.stage > 0) {
                        double forward = Bhop.mc.thePlayer.movementInput.moveForward;
                        double strafe = Bhop.mc.thePlayer.movementInput.moveStrafe;
                        float yaw = Bhop.mc.thePlayer.rotationYaw;
                        if (forward == 0.0 && strafe == 0.0) {
                            em.setX(0.0);
                            em.setZ(0.0);
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
                            em.setX(forward * this.speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * this.speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                            em.setZ(forward * this.speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * this.speed * Math.cos(Math.toRadians(yaw + 90.0f)));
                        }
                    }
                    if (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) {
                        ++Bhop.stage;
                    }
                }
                if (event instanceof EventMotion) {
                    final EventMotion em2 = (EventMotion)event;
                    if (em2.isPre()) {
                        final double xDist = Bhop.mc.thePlayer.posX - Bhop.mc.thePlayer.prevPosX;
                        final double zDist = Bhop.mc.thePlayer.posZ - Bhop.mc.thePlayer.prevPosZ;
                        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                    break;
                }
                break;
            }
            case "Bhop": {
                if (event instanceof EventMove) {
                    final EventMove em = (EventMove)event;
                    if (Bhop.mc.thePlayer.moveForward == 0.0f && Bhop.mc.thePlayer.moveStrafing == 0.0f) {
                        this.speed = defaultSpeed();
                    }
                    if (Bhop.stage == 1 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        this.speed = 1.35 + defaultSpeed() - 0.01;
                    }
                    if (Bhop.stage == 2 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        em.setY(Bhop.mc.thePlayer.motionY = 0.4);
                        this.speed *= 1.533;
                    }
                    else if (Bhop.stage == 3) {
                        final double difference2 = 0.66 * (this.lastDist - defaultSpeed());
                        this.speed = this.lastDist - difference2;
                    }
                    else {
                        final List collidingList = Bhop.mc.theWorld.getCollidingBoundingBoxes(Bhop.mc.thePlayer, Bhop.mc.thePlayer.boundingBox.offset(0.0, Bhop.mc.thePlayer.motionY, 0.0));
                        if ((collidingList.size() > 0 || Bhop.mc.thePlayer.isCollidedVertically) && Bhop.stage > 0) {
                            Bhop.stage = ((Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        this.speed = this.lastDist - this.lastDist / 159.0;
                    }
                    this.speed = Math.max(this.speed, defaultSpeed());
                    if (Bhop.stage > 0) {
                        this.setMotion(em, this.speed);
                    }
                    if (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) {
                        ++Bhop.stage;
                    }
                }
                if (event instanceof EventMotion) {
                    final EventMotion em2 = (EventMotion)event;
                    if (em2.isPre()) {
                        final double xDist = Bhop.mc.thePlayer.posX - Bhop.mc.thePlayer.prevPosX;
                        final double zDist = Bhop.mc.thePlayer.posZ - Bhop.mc.thePlayer.prevPosZ;
                        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                    break;
                }
                break;
            }
            case "OnGround": {
                if (event instanceof EventMotion) {
                    final EventMotion em2 = (EventMotion)event;
                    if (em2.isPre()) {
                        Bhop.mc.timer.timerSpeed = 1.085f;
                        double forward2 = Bhop.mc.thePlayer.movementInput.moveForward;
                        double strafe2 = Bhop.mc.thePlayer.movementInput.moveStrafe;
                        if ((forward2 != 0.0 || strafe2 != 0.0) && !Bhop.mc.thePlayer.isJumping && !Bhop.mc.thePlayer.isInWater() && !Bhop.mc.thePlayer.isOnLadder() && !Bhop.mc.thePlayer.isCollidedHorizontally) {
                            em2.setY(Bhop.mc.thePlayer.posY + ((Bhop.mc.thePlayer.ticksExisted % 2 != 0) ? 0.4 : 0.0));
                        }
                        this.speed = Math.max((Bhop.mc.thePlayer.ticksExisted % 2 == 0) ? 2.1 : 1.3, defaultSpeed());
                        float yaw2 = Bhop.mc.thePlayer.rotationYaw;
                        if (forward2 == 0.0 && strafe2 == 0.0) {
                            Bhop.mc.thePlayer.motionX = 0.0;
                            Bhop.mc.thePlayer.motionZ = 0.0;
                        }
                        else {
                            if (forward2 != 0.0) {
                                if (strafe2 > 0.0) {
                                    yaw2 += ((forward2 > 0.0) ? -45 : 45);
                                }
                                else if (strafe2 < 0.0) {
                                    yaw2 += ((forward2 > 0.0) ? 45 : -45);
                                }
                                strafe2 = 0.0;
                                if (forward2 > 0.0) {
                                    forward2 = 0.15;
                                }
                                else if (forward2 < 0.0) {
                                    forward2 = -0.15;
                                }
                            }
                            if (strafe2 > 0.0) {
                                strafe2 = 0.15;
                            }
                            else if (strafe2 < 0.0) {
                                strafe2 = -0.15;
                            }
                            Bhop.mc.thePlayer.motionX = forward2 * this.speed * Math.cos(Math.toRadians(yaw2 + 90.0f)) + strafe2 * this.speed * Math.sin(Math.toRadians(yaw2 + 90.0f));
                            Bhop.mc.thePlayer.motionZ = forward2 * this.speed * Math.sin(Math.toRadians(yaw2 + 90.0f)) - strafe2 * this.speed * Math.cos(Math.toRadians(yaw2 + 90.0f));
                        }
                    }
                    break;
                }
                break;
            }
            case "YPort": {
                if (Bhop.stage < 1) {
                    ++Bhop.stage;
                    this.lastDist = 0.0;
                    break;
                }
                if (event instanceof EventMove) {
                    final EventMove em = (EventMove)event;
                    if (Bhop.mc.thePlayer.onGround || Bhop.stage == 3) {
                        if ((!Bhop.mc.thePlayer.isCollidedHorizontally && Bhop.mc.thePlayer.moveForward != 0.0f) || Bhop.mc.thePlayer.moveStrafing != 0.0f) {
                            if (Bhop.stage == 2) {
                                this.speed *= 2.149;
                                Bhop.stage = 3;
                            }
                            else if (Bhop.stage == 3) {
                                Bhop.stage = 2;
                                final double difference2 = 0.66 * (this.lastDist - defaultSpeed());
                                this.speed = this.lastDist - difference2;
                            }
                            else {
                                final List collidingList = Bhop.mc.theWorld.getCollidingBoundingBoxes(Bhop.mc.thePlayer, Bhop.mc.thePlayer.boundingBox.offset(0.0, Bhop.mc.thePlayer.motionY, 0.0));
                                if (collidingList.size() > 0 || Bhop.mc.thePlayer.isCollidedVertically) {
                                    Bhop.stage = 1;
                                }
                            }
                        }
                        else {
                            Bhop.mc.timer.timerSpeed = 1.0f;
                        }
                        this.setMotion(em, this.speed = Math.max(this.speed, defaultSpeed()));
                    }
                }
                if (event instanceof EventMotion) {
                    final EventMotion em2 = (EventMotion)event;
                    if (em2.isPre()) {
                        if (Bhop.stage == 3) {
                            double gay = 0.4;
                            if (Bhop.mc.thePlayer.isPotionActive(Potion.jump)) {
                                gay = (Bhop.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                            }
                            em2.setY(em2.getY() + gay);
                        }
                        final double xDist = Bhop.mc.thePlayer.posX - Bhop.mc.thePlayer.prevPosX;
                        final double zDist = Bhop.mc.thePlayer.posZ - Bhop.mc.thePlayer.prevPosZ;
                        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                    break;
                }
                break;
            }
            case "OldHop": {
                if (event instanceof EventMove) {
                    final EventMove em = (EventMove)event;
                    if (Bhop.mc.thePlayer.moveForward == 0.0f && Bhop.mc.thePlayer.moveStrafing == 0.0f) {
                        this.speed = defaultSpeed();
                    }
                    if (Bhop.stage == 1 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        this.speed = 0.25 + defaultSpeed() - 0.01;
                    }
                    else if (Bhop.stage == 2 && Bhop.mc.thePlayer.isCollidedVertically && (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f)) {
                        em.setY(Bhop.mc.thePlayer.motionY = 0.4);
                        this.speed *= 2.149;
                    }
                    else if (Bhop.stage == 3) {
                        final double difference2 = 0.66 * (this.lastDist - defaultSpeed());
                        this.speed = this.lastDist - difference2;
                    }
                    else {
                        final List collidingList = Bhop.mc.theWorld.getCollidingBoundingBoxes(Bhop.mc.thePlayer, Bhop.mc.thePlayer.boundingBox.offset(0.0, Bhop.mc.thePlayer.motionY, 0.0));
                        if ((collidingList.size() > 0 || Bhop.mc.thePlayer.isCollidedVertically) && Bhop.stage > 0) {
                            if (1.35 * defaultSpeed() - 0.01 > this.speed) {
                                Bhop.stage = 0;
                            }
                            else {
                                Bhop.stage = ((Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                            }
                        }
                        this.speed = this.lastDist - this.lastDist / 159.0;
                    }
                    this.speed = Math.max(this.speed, defaultSpeed());
                    if (Bhop.stage > 0) {
                        this.setMotion(em, this.speed);
                    }
                    if (Bhop.mc.thePlayer.moveForward != 0.0f || Bhop.mc.thePlayer.moveStrafing != 0.0f) {
                        ++Bhop.stage;
                    }
                }
                if (!(event instanceof EventMotion)) {
                    break;
                }
                final EventMotion em2 = (EventMotion)event;
                if (em2.isPre()) {
                    final double xDist = Bhop.mc.thePlayer.posX - Bhop.mc.thePlayer.prevPosX;
                    final double zDist = Bhop.mc.thePlayer.posZ - Bhop.mc.thePlayer.prevPosZ;
                    this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
                }
                break;
            }
        }
    }
    
    private void setMotion(final EventMove em, final double speed) {
        double forward = Bhop.mc.thePlayer.movementInput.moveForward;
        double strafe = Bhop.mc.thePlayer.movementInput.moveStrafe;
        float yaw = Bhop.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
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
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
}
