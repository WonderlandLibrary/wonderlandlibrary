package com.ohare.client.module.modules.movement;

import java.awt.Color;

import com.ohare.client.Client;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class MemeMachine extends Module {
    private NumberValue<Double> speed = new NumberValue("Speed", 3.0, 0.1, 10.0, 0.1);

    private boolean laggedback, felldown;
    private float[] lastrot = new float[2];

    public MemeMachine() {
        super("MemeMachine", Category.MOVEMENT, new Color(255, 0, 0, 255).getRGB());
        setDescription("Hypixel disabler");
        setRenderlabel("Meme Machine");
        addValues(speed);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (felldown) {
            if (event.isPre()) {
                if (mc.thePlayer.movementInput.jump) {
                    mc.thePlayer.motionY = speed.getValue();
                } else if (mc.thePlayer.movementInput.sneak) {
                    mc.thePlayer.motionY = -speed.getValue();
                } else {
                    mc.thePlayer.motionY = 0.0;
                }
                if (laggedback) {
                    setMoveSpeed(speed.getValue());
                } else {
                    mc.thePlayer.movementInput.moveForward = 0;
                    mc.thePlayer.movementInput.moveStrafe = 0;
                    event.setYaw(lastrot[0]);
                    event.setPitch(lastrot[1]);
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ);
                    } else {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ);
                    }
                }
            }
        } else if (mc.thePlayer.fallDistance > 0.0) {
            felldown = true;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!event.isSending() && felldown && !mc.isSingleplayer() && mc.theWorld != null) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
                if (mc.thePlayer.posY > packet.getY()) {
                    Printer.print("Gerald Figley started a call. Today at 9:11 AM");
                    Client.INSTANCE.getNotificationManager().addNotification("WatchDog disabled for 3 seconds!",2000);
                    laggedback = true;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            laggedback = false;
            felldown = false;
            lastrot[0] = mc.thePlayer.rotationYaw;
            lastrot[1] = mc.thePlayer.rotationPitch;
            if (mc.thePlayer.onGround) mc.thePlayer.jump();
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

    private void setMoveSpeed(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
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
            mc.thePlayer.motionX = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            mc.thePlayer.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
        }
    }
}
