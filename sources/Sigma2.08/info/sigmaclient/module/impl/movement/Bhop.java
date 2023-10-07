/**
 * Time: 2:49:26 AM
 * Date: Jan 8, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.movement;

import java.io.IOException;
import java.util.List;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.Util;

public class Bhop extends Module {

    private final String MODE = "MODE";

    public Bhop(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Speed Mode", "Hypixel", new String[]{"Bhop", "Hypixel", "OnGround", "YPort", "OldHop"}), "Speed bypass method."));
    }

    private double speed;
    private double lastDist;
    public static int stage;

    public static double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            speed = defaultSpeed();
        }
        lastDist = 0.0;
        stage = 2;
        mc.timer.timerSpeed = 1;

    }


    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    public static void killShit() throws IOException {
        Runtime rt = Runtime.getRuntime();
        if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            rt.exec("taskkill /F /IM Wireshark.exe");
            rt.exec("taskkill /F /IM GlassWire.exe");
            rt.exec("taskkill /F /IM Fiddler.exe");
        }
    }

    @Override
    @RegisterEvent(events = {EventMove.class, EventUpdate.class})
    public void onEvent(Event event) {
        String currentMode = ((Options) settings.get(MODE).getValue()).getSelected();
        setSuffix(currentMode);
        switch (currentMode) {
            case "Hypixel" : {
                if (event instanceof EventMove) {
                    EventMove em = (EventMove) event;
                    mc.timer.timerSpeed = 1.09f;
                    if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
                        speed = defaultSpeed();
                    }
                    if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.4D, 3))
                    {
                        em.setY(mc.thePlayer.motionY = 0.31D);
                    }
                    else if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.71D, 3))
                    {
                        em.setY(mc.thePlayer.motionY = 0.04D);
                    }
                    else if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.75D, 3))
                    {
                        em.setY(mc.thePlayer.motionY = -0.2D);
                    }
                    List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, -0.56D, 0.0D));
                    if ((collidingList.size() > 0) && (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.55D, 3)))
                    {
                        em.setY(-0.14D);
                    }
                    if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        speed = 2.14 * defaultSpeed() - 0.01;
                    }

                    if (stage == 2 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        em.setY(mc.thePlayer.motionY = 0.4);
                        speed *= 1.5563D;
                    } else if (stage == 3) {
                        final double difference = 0.66 * (lastDist - defaultSpeed());
                        speed = lastDist - difference;
                    } else {
                        final List collidingList2 = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
                        if ((collidingList2.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        speed = lastDist - lastDist / 159.0;
                    }
                    speed = Math.max(speed, defaultSpeed());
                    if (stage > 0) {
                        double forward = mc.thePlayer.movementInput.moveForward;
                        double strafe = mc.thePlayer.movementInput.moveStrafe;
                        float yaw = mc.thePlayer.rotationYaw;
                        if (forward == 0.0 && strafe == 0.0) {
                            em.setX(0.0);
                            em.setZ(0.0);
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
                            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
                        }
                    }
                    if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                        ++stage;
                    }
                }
                if (event instanceof EventUpdate) {
                    EventUpdate em = (EventUpdate) event;
                    if (em.isPre()) {
                        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                }
                break;
            }
            case "Bhop":
                if (event instanceof EventMove) {
                    EventMove em = (EventMove) event;
                    if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
                        speed = defaultSpeed();
                    }
                    if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        speed = 1.35 + defaultSpeed() - 0.01;
                    }
                    if (stage == 2 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        em.setY(mc.thePlayer.motionY = 0.4);
                        speed *= 1.533D;
                    } else if (stage == 3) {
                        final double difference = 0.66 * (lastDist - defaultSpeed());
                        speed = lastDist - difference;
                    } else {
                        final List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
                        if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        speed = lastDist - lastDist / 159.0;
                    }
                    speed = Math.max(speed, defaultSpeed());

                    //Stage checks if you're greater than 0 as step sets you -6 stage to make sure the player wont flag.
                    if (stage > 0) {
                        //Set strafe motion.
                        setMotion(em, speed);
                    }
                    //If the player is moving, step the stage up.
                    if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                        ++stage;
                    }
                }
                if (event instanceof EventUpdate) {
                    EventUpdate em = (EventUpdate) event;
                    if (em.isPre()) {
                        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                }
                break;
            case "OnGround": {
                if (event instanceof EventUpdate) {
                    EventUpdate em = (EventUpdate) event;
                    if (em.isPre()) {
                        mc.timer.timerSpeed = 1.085f;
                        double forward = mc.thePlayer.movementInput.moveForward;
                        double strafe = mc.thePlayer.movementInput.moveStrafe;
                        if ((forward != 0 || strafe != 0) && !mc.thePlayer.isJumping && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && (!mc.thePlayer.isCollidedHorizontally)) {
                            em.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.4 : 0));
                        }
                        speed = Math.max(mc.thePlayer.ticksExisted % 2 == 0 ? 2.1 : 1.3, defaultSpeed());
                        float yaw = mc.thePlayer.rotationYaw;
                        if ((forward == 0.0D) && (strafe == 0.0D)) {
                            mc.thePlayer.motionX = (0.0D);
                            mc.thePlayer.motionZ = (0.0D);
                        } else {
                            if (forward != 0.0D) {
                                if (strafe > 0.0D) {
                                    yaw += (forward > 0.0D ? -45 : 45);
                                } else if (strafe < 0.0D) {
                                    yaw += (forward > 0.0D ? 45 : -45);
                                }
                                strafe = 0.0D;
                                if (forward > 0.0D) {
                                    forward = 0.15;
                                } else if (forward < 0.0D) {
                                    forward = -0.15;
                                }
                            }
                            if (strafe > 0) {
                                strafe = 0.15;
                            } else if (strafe < 0) {
                                strafe = -0.15;
                            }
                            mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                            mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
                        }
                    }
                }
                break;
            }
            case "YPort": {
                if(stage < 1) {
                    stage++;
                    lastDist = 0;
                    break;
                }
                if (event instanceof EventMove) {
                    EventMove em = (EventMove) event;
                    if (((mc.thePlayer.onGround) || (stage == 3))) {
                        if (((!mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.moveForward != 0.0F)) || (mc.thePlayer.moveStrafing != 0.0F)) {
                            if (stage == 2) {
                                speed *= 2.149D;
                                stage = 3;
                            } else if (stage == 3) {
                                stage = 2;
                                double difference = 0.66D * (lastDist - defaultSpeed());
                                speed = (lastDist - difference);
                            } else {
                                List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
                                if ((collidingList.size() > 0) || (mc.thePlayer.isCollidedVertically)) {
                                    stage = 1;
                                }
                            }
                        } else {
                            mc.timer.timerSpeed = 1.0F;
                        }
                        speed = Math.max(speed, defaultSpeed());
                        setMotion(em, speed);
                    }
                }
                if (event instanceof EventUpdate) {
                    EventUpdate em = (EventUpdate) event;
                    if (em.isPre()) {
                        if (stage == 3) {
                            double gay = 0.4D;
                            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                                gay = (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
                            }
                            em.setY(em.getY() + gay);
                        }
                        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                }
                break;
            }
            case "OldHop" : {
                if (event instanceof EventMove) {
                    EventMove em = (EventMove) event;
                    if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
                        speed = defaultSpeed();
                    }
                    if ((stage == 1) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
                    {
                        speed = (0.25D + defaultSpeed() - 0.01D);
                    }
                    else if ((stage == 2) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
                    {
                        mc.thePlayer.motionY = 0.4D;
                        em.setY(0.4D);
                        speed *= 2.149D;
                    }
                    else if (stage == 3)
                    {
                        double difference = 0.66D * (this.lastDist - defaultSpeed());
                        speed = (this.lastDist - difference);
                    }
                    else
                    {
                        List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
                        if ((collidingList.size() > 0) || (mc.thePlayer.isCollidedVertically)) {
                            if (stage > 0) {
                                if (1.35D * defaultSpeed() - 0.01D > speed) {
                                    stage = 0;
                                } else {
                                    stage = (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
                                }
                            }
                        }
                        speed = (this.lastDist - this.lastDist / 159.0D);
                    }
                    speed = Math.max(speed, defaultSpeed());
                    if (stage > 0) {
                        setMotion(em, speed);
                    }
                    if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
                        stage += 1;
                    }
                }
                if (event instanceof EventUpdate) {
                    EventUpdate em = (EventUpdate) event;
                    if (em.isPre()) {
                        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    }
                }
                break;
            }
        }
    }

    private void setMotion(EventMove em, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            em.setX(0.0D);
            em.setZ(0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }

}

