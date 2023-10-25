/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventAir;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.movement.TargetStrafe;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.MoveUtil;
import me.Tengoku.Terror.util.PlayerUtils;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Speed
extends Module {
    private int boost = 0;
    public boolean reset;
    TimerUtils timer3;
    public boolean doSlow;
    private boolean move;
    int ncpStage;
    private double prevY;
    private double nextMotionSpeed;
    private int direction = 1;
    private double moveSpeed;
    TimerUtils timer2;
    private double zDist;
    private TargetStrafe targetStrafe;
    int stage;
    Timer timer = new Timer();
    private double xMotionSpeed;
    private boolean hop;

    public static double getDirection() {
        boolean bl;
        float f = 1.0f;
        float f2 = Minecraft.thePlayer.rotationYaw;
        boolean bl2 = Minecraft.thePlayer.moveForward < 0.0f;
        boolean bl3 = Minecraft.thePlayer.moveForward > 0.0f;
        boolean bl4 = Minecraft.thePlayer.moveStrafing > 0.0f;
        boolean bl5 = bl = Minecraft.thePlayer.moveStrafing < 0.0f;
        if (bl2) {
            f2 += 180.0f;
        }
        if (bl2) {
            f = -0.5f;
        }
        if (bl3) {
            f = 0.5f;
        }
        if (bl4) {
            f2 = (float)((double)f2 - 90.0 * (double)f);
        }
        if (bl) {
            f2 = (float)((double)f2 + 90.0 * (double)f);
        }
        return Math.toRadians(f2);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string;
        block28: {
            block29: {
                string = Exodus.INSTANCE.settingsManager.getSettingByName("Speed Mode").getValString();
                double d = Exodus.INSTANCE.settingsManager.getSettingByClass("Boost Factor", Speed.class).getValDouble();
                double d2 = Exodus.INSTANCE.settingsManager.getSettingByName("Slow Strafe Speed").getValDouble();
                this.setDisplayName("Speed \ufffdf" + string);
                String string2 = Exodus.INSTANCE.settingsManager.getSettingByModule("Strafe", this).getValString();
                if (string.equalsIgnoreCase("Vanilla_BHop")) {
                    if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                        Minecraft.thePlayer.setSprinting(true);
                    } else {
                        Minecraft.thePlayer.setSprinting(false);
                    }
                    if (Minecraft.thePlayer.onGround) {
                        if (Minecraft.gameSettings.keyBindForward.isKeyDown() | Minecraft.gameSettings.keyBindBack.isKeyDown() | Minecraft.gameSettings.keyBindLeft.isKeyDown() | Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                            Minecraft.thePlayer.jump();
                        }
                    }
                    if (string2.equalsIgnoreCase("Regular")) {
                        MoveUtil.setMotion(d / 4.0);
                    }
                    if (string2.equalsIgnoreCase("None")) {
                        Minecraft.gameSettings.keyBindRight.pressed = false;
                        Minecraft.gameSettings.keyBindLeft.pressed = false;
                        MoveUtil.setMotion(d / 4.0);
                    }
                    if (string2.equalsIgnoreCase("Slow")) {
                        if (Minecraft.gameSettings.keyBindLeft.isKeyDown() | Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                            Speed.mc.timer.timerSpeed = (float)d2;
                            MoveUtil.setMotion(d / 4.0);
                        }
                    }
                }
                if (!string.equalsIgnoreCase("AAC")) break block28;
                if (Minecraft.gameSettings.keyBindForward.pressed) break block29;
                if (Minecraft.gameSettings.keyBindBack.pressed) break block29;
                if (Minecraft.gameSettings.keyBindLeft.pressed) break block29;
                if (!Minecraft.gameSettings.keyBindRight.pressed) break block28;
                if (!Minecraft.gameSettings.keyBindJump.pressed) break block28;
            }
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            }
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
            }
            if (Minecraft.thePlayer.isAirBorne) {
                Minecraft.thePlayer.motionX *= 1.01;
                Minecraft.thePlayer.motionZ *= 1.01;
            }
            if (Minecraft.thePlayer.motionX > 1.2) {
                Minecraft.thePlayer.motionX = 1.01;
            }
        }
        if (string.equalsIgnoreCase("Hypixel")) {
            Minecraft.thePlayer.setSprinting(false);
            Speed.mc.timer.timerSpeed = 0.92178273f;
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld != null) {
                    Minecraft.gameSettings.keyBindJump.pressed = false;
                    if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                        if (!Minecraft.thePlayer.isCollidedHorizontally) {
                            boolean cfr_ignored_0 = Minecraft.thePlayer.onGround;
                        }
                    }
                }
            }
        }
        if (string.equalsIgnoreCase("WatchdogGround")) {
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.motionY = 0.0;
                Speed.mc.timer.timerSpeed = 2.0210426f;
            } else {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
        }
        if (string.equalsIgnoreCase("BlocksMC")) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            }
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                Minecraft.thePlayer.setSpeed(0.3f);
            }
            if (Minecraft.thePlayer.moveStrafing > 0.0f) {
                Minecraft.thePlayer.setSpeed(0.3f);
            }
        }
    }

    @EventTarget
    public void onAir(EventAir eventAir) {
        String string = Exodus.INSTANCE.getSettingsManager().getSettingByClass("Speed Mode", Speed.class).getValString();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.speedInAir = 0.02f;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Speed Mode").getValString();
        Speed.mc.timer.timerSpeed = 1.0f;
        Minecraft.gameSettings.keyBindJump.pressed = false;
    }

    private void hop(float f) {
        Minecraft.thePlayer.jump();
        Minecraft.thePlayer.motionY -= (double)f;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Speed Mode").getValString();
    }

    @EventTarget
    public void onRender(EventRenderGUI eventRenderGUI) {
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByModule("Show BPS", this).getValBoolean();
        if (bl) {
            String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Font").getValString();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            if (string.equalsIgnoreCase("Arial")) {
                FontUtil.normal.drawString(String.valueOf(PlayerUtils.getBPS()) + " BPS", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 - 15, -1);
            } else {
                Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(PlayerUtils.getBPS()) + " BPS", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 - 15, -1);
            }
        }
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        arrayList.add("BlocksMC");
        arrayList.add("Ground");
        arrayList.add("Vanilla_BHop");
        arrayList.add("LowHop");
        arrayList.add("Y-Port");
        arrayList.add("BHop");
        arrayList.add("Watchdog_LowHop");
        arrayList.add("Watchdog");
        arrayList2.add("Regular");
        arrayList2.add("Slow");
        arrayList2.add("None");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("KeepSprint", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Show BPS", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Speed Mode", (Module)this, "LowHop", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Boost Factor", this, 3.0, 1.0, 10.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Strafe", (Module)this, "Regular", arrayList2));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Slow Strafe Speed", this, 0.5, 0.1f, 0.99f, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Watchdog Timer Boost", this, false));
    }

    @EventTarget
    public void onMove(EventMotion eventMotion) {
        float f;
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("Speed Mode", this).getValString();
        if (string.equalsIgnoreCase("Y-Port")) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            } else {
                Minecraft.thePlayer.setSprinting(false);
            }
            if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                if (eventMotion.isOnGround()) {
                    double d = Exodus.INSTANCE.getSettingsManager().getSettingByModule("Boost Factor", this).getValDouble() / 2.0;
                    double d2 = Math.toRadians(Minecraft.thePlayer.rotationYaw);
                    double d3 = -Math.sin(d2) * d;
                    double d4 = Math.cos(d2) * d;
                    Minecraft.thePlayer.motionX = d3;
                    Minecraft.thePlayer.motionZ = d4;
                    Minecraft.thePlayer.jump();
                } else {
                    Minecraft.thePlayer.motionY -= (double)0.15f;
                }
            }
        }
        if (string.equalsIgnoreCase("WatchdogLowHop3") && this.isToggled() && eventMotion.isPre()) {
            Minecraft.gameSettings.keyBindRight.pressed = false;
            Minecraft.gameSettings.keyBindLeft.pressed = false;
            Minecraft.gameSettings.keyBindJump.pressed = false;
            if (eventMotion.isOnGround()) {
                Minecraft.thePlayer.jump();
                Minecraft.thePlayer.setSprinting(true);
                Minecraft.thePlayer.motionY -= (double)0.2582571f;
            }
            float f2 = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
            f = (float)Math.cos((double)(f2 + 90.0f) * Math.PI / 180.0);
            float f3 = (float)Math.sin((double)(f2 + 90.0f) * Math.PI / 180.0);
            if (eventMotion.isOnGround()) {
                Minecraft.thePlayer.setSpeed(0.55f);
                Speed.mc.timer.timerSpeed = 1.50205f;
            } else {
                Speed.mc.timer.timerSpeed = 0.85f;
            }
        }
        if (string.equalsIgnoreCase("Watchdog_LowHop") && this.isToggled() && eventMotion.isPre()) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            } else {
                Minecraft.thePlayer.setSprinting(false);
            }
            if (Minecraft.gameSettings.keyBindForward.isKeyDown() && eventMotion.isOnGround()) {
                float f4 = (float)(1.1632689389165578 * MathUtils.getBaseMovementSpeed() * Exodus.INSTANCE.settingsManager.getSettingByModule("Boost Factor", this).getValDouble() / (double)2.05f) + 0.05f;
                Minecraft.thePlayer.posY += 0.25;
                Minecraft.thePlayer.motionY = 0.0;
                Minecraft.thePlayer.motionX = -Math.sin(Math.toRadians(Minecraft.thePlayer.rotationYaw)) * (double)f4;
                Minecraft.thePlayer.motionZ = Math.cos(Math.toRadians(Minecraft.thePlayer.rotationYaw)) * (double)f4;
            }
        }
        if (string.equalsIgnoreCase("BHop") && this.isToggled() && eventMotion.isPre()) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            } else {
                Minecraft.thePlayer.setSprinting(false);
            }
            boolean bl = Exodus.INSTANCE.getSettingsManager().getSettingByModule("Watchdog Timer Boost", this).getValBoolean();
            f = Minecraft.thePlayer.rotationYaw + (float)(Minecraft.thePlayer.moveForward < 0.0f ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0f ? -90.0f * (Minecraft.thePlayer.moveForward < 0.0f ? -0.5f : (Minecraft.thePlayer.moveForward > 0.0f ? 0.4f : 1.0f)) : 0.0f);
            float f5 = (float)Math.cos((double)(f + 90.0f) * Math.PI / 180.0);
            float f6 = (float)Math.sin((double)(f + 90.0f) * Math.PI / 180.0);
            float f7 = (float)(1.1632689389165578 * MathUtils.getBaseMovementSpeed() * Exodus.INSTANCE.settingsManager.getSettingByModule("Boost Factor", this).getValDouble() / (double)2.05f) + 0.05f;
            if (Minecraft.gameSettings.keyBindForward.isKeyDown() | Minecraft.gameSettings.keyBindLeft.isKeyDown() | Minecraft.gameSettings.keyBindRight.isKeyDown() | Minecraft.gameSettings.keyBindBack.isKeyDown()) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                    Minecraft.thePlayer.motionX = f5 * f7 / 2.0f;
                    Minecraft.thePlayer.motionZ = f6 * f7 / 2.0f;
                }
                Minecraft.gameSettings.keyBindJump.pressed = true;
                if (bl && this.timer2.hasReached(10000.0)) {
                    if (Minecraft.thePlayer.onGround) {
                        Speed.mc.timer.timerSpeed = 1.05f;
                        this.timer2.reset();
                    }
                }
            }
        }
        if (string.equalsIgnoreCase("Ground") && eventMotion.isPre()) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            } else {
                Minecraft.thePlayer.setSprinting(false);
            }
            if (eventMotion.isOnGround()) {
                Minecraft.thePlayer.motionY = 0.0;
            } else {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
            Speed.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Boost Factor", this).getValDouble();
            if (this.timer.hasTimeElapsed(150L, true)) {
                Speed.mc.timer.timerSpeed = 0.9f;
            }
        }
        if (string.equalsIgnoreCase("Watchdog")) {
            boolean bl = Exodus.INSTANCE.getSettingsManager().getSettingByModule("Watchdog Timer Boost", this).getValBoolean();
            if (eventMotion.isPre()) {
                if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                    Minecraft.thePlayer.setSprinting(true);
                } else {
                    Minecraft.thePlayer.setSprinting(false);
                }
                if (bl) {
                    Speed.mc.timer.timerSpeed = 1.2f;
                }
                if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.jump();
                        Minecraft.gameSettings.keyBindJump.pressed = true;
                        Minecraft.thePlayer.setSpeedStrafeless((float)Exodus.INSTANCE.getSettingsManager().getSettingByModule("Boost Factor", this).getValDouble() - 1.0f);
                    }
                }
            }
        }
        if (string.equalsIgnoreCase("LowHop") && this.isToggled() && eventMotion.isPre()) {
            if (Exodus.INSTANCE.settingsManager.getSettingByModule("KeepSprint", this).getValBoolean()) {
                Minecraft.thePlayer.setSprinting(true);
            } else {
                Minecraft.thePlayer.setSprinting(false);
            }
            boolean bl = Exodus.INSTANCE.settingsManager.getSettingByClass("Strafe", Speed.class).getValBoolean();
            f = (float)Exodus.INSTANCE.getSettingsManager().getSettingByModule("Boost Factor", this).getValDouble();
            if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                    this.hop(0.17592919f);
                    if (!bl) {
                        Minecraft.thePlayer.motionZ *= (double)(f / 2.0f);
                        Minecraft.thePlayer.motionZ *= (double)(f / 2.0f);
                    } else {
                        MoveUtil.setMotion(f);
                    }
                }
            }
        }
    }

    public Speed() {
        super("Speed", 47, Category.MOVEMENT, "Gotta go fast.");
        this.timer3 = new TimerUtils();
        this.timer2 = new TimerUtils();
    }
}

