/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.client.Minecraft;

public class Speed
extends Module {
    private final Setting strafing;
    private int counter;
    TimerUtils timerUtils = new TimerUtils();

    public Speed() {
        super("Speed", Category.Movement);
        ArrayList<String> speed = new ArrayList<String>();
        speed.add("Matrix Latest");
        speed.add("Matrix 6.0.4");
        speed.add("Matrix");
        speed.add("OnGround");
        speed.add("YPort");
        Main.instance.setmgr.rSetting(new Setting("Speed Mode", this, "Matrix Latest", speed));
        this.strafing = new Setting("Strafing", this, false);
        Main.instance.setmgr.rSetting(this.strafing);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (this.getState()) {
            String mode = Main.instance.setmgr.getSettingByName("Speed Mode").getValString();
            this.setSuffix(mode);
            if (mode.equalsIgnoreCase("Matrix 6.0.4")) {
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                } else {
                    if (Minecraft.player.ticksExisted % 5 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.0f;
                        Speed.mc.timer.timerSpeed = 0.6f;
                    }
                    if (Minecraft.player.ticksExisted % 5 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.28f;
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                    if (Minecraft.player.ticksExisted % 10 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.38f;
                    }
                    if (Minecraft.player.ticksExisted % 20 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.35f;
                        Speed.mc.timer.timerSpeed = 1.1f;
                    }
                }
            }
            if (mode.equalsIgnoreCase("YPort")) {
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                } else {
                    Minecraft.player.motionY = -20.0;
                    Speed.mc.timer.timerSpeed = 2.0f;
                }
            }
            if (mode.equalsIgnoreCase("Matrix Latest")) {
                if (!Minecraft.player.isOnLadder()) {
                    if (!Minecraft.player.isInWater()) {
                        if (!Minecraft.player.isInLava()) {
                            Speed.mc.gameSettings.keyBindJump.pressed = false;
                            if (this.strafing.getValue()) {
                                MovementUtil.strafe();
                            }
                            if (Minecraft.player.onGround) {
                                Minecraft.player.jump();
                                this.counter = 5;
                            } else if (this.counter < 5) {
                                if (this.counter == 1) {
                                    Speed.mc.timer.timerSpeed = 1.2f;
                                }
                                ++this.counter;
                            } else {
                                this.counter = 0;
                                Minecraft.player.speedInAir = 0.02079f;
                                Minecraft.player.jumpMovementFactor = 0.027f;
                            }
                        }
                    }
                }
                Speed.mc.timer.timerSpeed = Minecraft.player.motionY == 0.0030162615090425808 ? 1.6f : 1.0f;
                if (Minecraft.player.ticksExisted % 60 > 39) {
                    Speed.mc.timer.timerSpeed = 3.0f;
                }
            }
            if (mode.equalsIgnoreCase("OnGround")) {
                MovementUtil.setSpeed(4.0);
            }
            if (mode.equalsIgnoreCase("Matrix")) {
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                }
                float ticks = 1.0f;
                Minecraft.player.jumpMovementFactor = ticks * 0.026f;
                Speed.mc.timer.timerSpeed = 1.0f;
                if (Minecraft.player.ticksExisted % 10 == 0) {
                    Minecraft.player.jumpMovementFactor = ticks * 0.027f;
                    Speed.mc.timer.timerSpeed = 1.1f;
                }
                if (Minecraft.player.ticksExisted % 10 == 0) {
                    Minecraft.player.jumpMovementFactor = ticks * 0.026f;
                }
                if (Minecraft.player.ticksExisted % 5 == 0) {
                    Minecraft.player.jumpMovementFactor = ticks * 0.0265f;
                    Speed.mc.timer.timerSpeed = 1.09f;
                }
                float f = ticks + 1.0f;
            }
        }
    }

    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        Minecraft.player.speedInAir = 0.02f;
        super.onDisable();
    }
}

